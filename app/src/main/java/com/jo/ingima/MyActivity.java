package com.jo.ingima;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyActivity extends AppCompatActivity {

    public static final String CITY_CODE = "CITY_CODE";
    TextView mainCityTextView;
    TextView mainDescrTextView;
    TextView mainTemperatureTextView;
    TextView mainMinTextView;
    TextView mainMaxTextView;
    NetworkImageView myImg;
    ProgressBar loadingProgress;
    private String currentCityCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        currentCityCode = extractCityCodeFromInternParameters();
        //todo if null

        mainCityTextView = (TextView) findViewById(R.id.main_city);
        mainDescrTextView = (TextView) findViewById(R.id.main_descr);
        mainTemperatureTextView = (TextView) findViewById(R.id.main_temperature);
        mainMinTextView = (TextView) findViewById(R.id.main_min_temp);
        mainMaxTextView = (TextView) findViewById(R.id.main_max_temp);
        myImg = (NetworkImageView) findViewById(R.id.my_img);
        loadingProgress = (ProgressBar) findViewById(R.id.loading_progress);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        reloadFromServer();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

    }

    private void reloadFromServer() {
        //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        loadingProgress.setVisibility(View.VISIBLE);

        RequestManager.getInstance(MyActivity.this).queryData(currentCityCode, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    mainCityTextView.setText(response.getString("name"));
                    double temp = response.getJSONObject("main").getDouble("temp");
                    double tempMin = response.getJSONObject("main").getDouble("temp_min");
                    double tempMax = response.getJSONObject("main").getDouble("temp_max");
                    JSONArray weatherArray = response.getJSONArray("weather");
                    if(weatherArray.length()>0) {
                        mainDescrTextView.setText(weatherArray.getJSONObject(0).getString("description"));
                        String iconString = weatherArray.getJSONObject(0).getString("icon");
                        myImg.setImageUrl("http://openweathermap.org/img/w/"+iconString+".png",RequestManager.getInstance(MyActivity.this).getImageLoader());
                    }
                    mainTemperatureTextView.setText(((int)temp)+"°");
                    mainMinTextView.setText("min "+tempMin+"°");
                    mainMaxTextView.setText("min "+tempMax+"°");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                loadingProgress.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO gerer la reponse
                loadingProgress.setVisibility(View.GONE);
            }
        });
    }

    public String extractCityCodeFromInternParameters() {
        if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().containsKey(CITY_CODE)) {
            return getIntent().getExtras().getString(CITY_CODE);
        }
        return null;
    }

}
