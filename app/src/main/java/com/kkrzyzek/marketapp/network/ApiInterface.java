package com.kkrzyzek.marketapp.network;

import com.kkrzyzek.marketapp.model.JSONResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    //method performing request for UK markets data
    @GET("en_GB/igi")
    Call<JSONResponse> getENInstrumentData();

    //method performing request for German markets data
    @GET("de_DE/dem")
    Call<JSONResponse> getDEInstrumentData();

    //method performing request for French markets data
    @GET("fr_FR/frm")
    Call<JSONResponse> getFRInstrumentData();
}
