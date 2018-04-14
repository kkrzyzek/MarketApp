package com.kkrzyzek.marketapp.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Setting up Retrofit ApiInterface
 */
public class ApiClient {

    private static final String REQUEST_BASE_URL = "https://api.ig.com/deal/samples/markets/ANDROID_PHONE/";
    private static Retrofit retrofit = null;

    public static Retrofit getApiClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().
                    baseUrl(REQUEST_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}
