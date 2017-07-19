package com.jo.ingima;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Date;

/**
 * Created by jo on 04/07/2017.
 */

public class RequestManager {
    private Context mCtx;
    private static RequestManager mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    RequestManager(Context context) {
        this.mCtx = context;
        mRequestQueue = getRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue,
            new ImageLoader.ImageCache() {
                private final LruCache<String, Bitmap>
                        cache = new LruCache<String, Bitmap>(20);

                @Override
                public Bitmap getBitmap(String url) {
                    return cache.get(url);
                }

                @Override
                public void putBitmap(String url, Bitmap bitmap) {
                    cache.put(url, bitmap);
                }
            });
    }

    public static synchronized RequestManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new RequestManager(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    public void queryData(String queryCity, Response.Listener<JSONObject> listenerOk, Response.ErrorListener listenerError) {
        String requestToSend = "http://api.openweathermap.org/data/2.5/weather?"
                +"q="+queryCity//,fr
                +"&units=metric&appid=" + mCtx.getResources().getString(R.string.api_key);

        queryGeneric(requestToSend, listenerOk, listenerError);
    }

    public void queryHour(double latitude, double longitude,
            Response.Listener<JSONObject> listenerOk, Response.ErrorListener listenerError) {
        //String requestToSend = "http://api.geonames.org/timezoneJSON?lat="+latitude+"&lng="+longitude+"&username=demo";
        String requestToSend = "https://maps.googleapis.com/maps/api/timezone/json?location="+latitude+","+longitude
                +"&timestamp="+new Date().getTime()/1000
                +"&key=%20AIzaSyD1g3Dn-vEBCe9amCPgb9mjeMs0wfrV7ts";

        queryGeneric(requestToSend, listenerOk, listenerError);
    }

    private void queryGeneric(String requestToSend, Response.Listener<JSONObject> listenerOk, Response.ErrorListener listenerError) {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, requestToSend, null, listenerOk, listenerError);

        // Access the RequestQueue through your singleton class.
        this.addToRequestQueue(jsObjRequest);
    }

}
