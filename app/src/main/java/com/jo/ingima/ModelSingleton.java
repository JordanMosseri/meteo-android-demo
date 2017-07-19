package com.jo.ingima;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by jo on 06/07/2017.
 */

public class ModelSingleton {

    //todo isoler (creer ReceiverBuilder)
    public static final String RECEIVER_MESSAGES_SYNCHRONISED = BuildConfig.APPLICATION_ID + ".receiver.MESSAGES_SYNCHRONISED";
    public static IntentFilter buildFilterReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(RECEIVER_MESSAGES_SYNCHRONISED);
        return filter;
    }
    public static BroadcastReceiver buildRootReceiver(final InteractionsReceiver interactionsReceiver) {
        return new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(RECEIVER_MESSAGES_SYNCHRONISED)) {

                    interactionsReceiver.onReceiveMessagesSynchronised();

                }
            }
        };
    }
    public interface InteractionsReceiver {
        void onReceiveMessagesSynchronised();
    }

    private static ModelSingleton mInstance;
    private Context mContext;
    private List<ViewModel> mItems;

    public static synchronized ModelSingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ModelSingleton(context);
        }
        return mInstance;
    }

    ModelSingleton(Context context) {
        this.mContext = context;
        mItems = new ArrayList<ViewModel>();
    }

    public void initAtLaunch() {
        createMockList();
    }

    //doit etre rooté...:
    //~/Library/Android/sdk/platform-tools/adb shell 'date $(date +%m%d%H%M) ; am broadcast -a android.intent.action.TIME_SET'
    //EN FAIT NON: ajouter '-timezone Europe/Helsinki' dans: edit configs>launch options>launch flags

    private void createMockList() {
        mItems.clear();
        /*mItems.add(new ViewModel("01:23", "Los Angeles", 12));
        mItems.add(new ViewModel("21:45", "San Fransisco", 05));
        mItems.add(new ViewModel("03:54", "New York", 19));*/
        addFromServer("Paris");
        addFromServer("Nice");
        addFromServer("Dublin");
        addFromServer("Los Angeles");
        addFromServer("San Fransisco");
        addFromServer("New York");
    }

    public void addFromServer(final String queryCity) {
        //final long currentDateTimestamp = new Date().getTime();
        final long currentDateTimestamp = System.currentTimeMillis();

        RequestManager.getInstance(mContext).queryData(queryCity, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(final JSONObject response) {
                try {
                    //todo
                    RequestManager.getInstance(mContext).queryHour(response.getJSONObject("coord").getDouble("lat"), response.getJSONObject("coord").getDouble("lon"),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject hourResponse) {
                                    try {
                                        //time pour l'autre fournisseurApi
                                        //new Date(response.getLong("dt"))
                                        /*theirDate = new Date(currentDateTimestamp
                                                +Long.parseLong(hourResponse.getString("rawOffset"))*1000L
                                                +Long.parseLong(hourResponse.getString("dstOffset"))*1000L
                                                -TimeZone.getDefault().getOffset(System.currentTimeMillis())
                                        );*/

                                        //"yyyy-MM-dd HH:mm:ss z"
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.FRANCE);
                                        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(hourResponse.getString("timeZoneId")));//TimeZone.getTimeZone("UTC")
                                        String hour = simpleDateFormat.format(new Date());
                                        ViewModel viewModel = new ViewModel(queryCity, hour,
                                                response.getString("name"),
                                                (int) response.getJSONObject("main").getDouble("temp"));
                                        ModelSingleton.this.addItem(viewModel);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    //todo a placer dans RequestManager?...
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            //on operation success
                                            Intent i = new Intent();
                                            i.setAction(ModelSingleton.RECEIVER_MESSAGES_SYNCHRONISED);
                                            mContext.sendBroadcast(i);
                                        }
                                    });
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            });

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    public int getItemsSize() {
        return mItems.size();
    }

    public ViewModel getItemPosition(int position) {
        return mItems.get(position);
    }

    public void addItem(ViewModel viewModel) {
        mItems.add(viewModel);
    }
}
