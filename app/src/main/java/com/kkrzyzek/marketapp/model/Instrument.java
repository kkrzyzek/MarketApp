package com.kkrzyzek.marketapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Model POJO class - single market instrument object
 */
public class Instrument {

    @SerializedName("instrumentName")
    private String mInstrumentName;
    @SerializedName("displayOffer")
    private String mOffer;

    public Instrument(String instrumentName, String offer) {
        this.mInstrumentName = instrumentName;
        this.mOffer = offer;
    }

    public String getInstrumentName() {
        return mInstrumentName;
    }

    public String getOffer() {
        return mOffer;
    }

}
