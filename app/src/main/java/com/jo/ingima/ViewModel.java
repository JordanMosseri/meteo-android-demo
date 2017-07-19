package com.jo.ingima;

/**
 * Created by jo on 06/07/2017.
 */

public class ViewModel {
    String queryCode;
    String hour;
    String city;
    int temp;

    public ViewModel(String queryCode, String hour, String city, int temp) {
        this.queryCode = queryCode;
        this.hour = hour;
        this.city = city;
        this.temp = temp;
    }
}
