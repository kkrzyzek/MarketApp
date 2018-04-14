package com.kkrzyzek.marketapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Model POJO class - markets array
 */
public class JSONResponse {

    @SerializedName("markets")
    private Instrument[] mMarkets;

    public Instrument[] getInstruments() {
        return mMarkets;
    }
}
