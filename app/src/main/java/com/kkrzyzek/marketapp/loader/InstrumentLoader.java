package com.kkrzyzek.marketapp.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.kkrzyzek.marketapp.model.JSONResponse;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Custom AsyncTaskLoader for handling device rotations
 */
public class InstrumentLoader extends AsyncTaskLoader<JSONResponse> {

    private static final String LOG_TAG = InstrumentLoader.class.getName();

    private Call<JSONResponse> mCall;
    //Global variable for storing data cache
    private JSONResponse mJsonResponse;

    public InstrumentLoader (Context context, Call<JSONResponse> call) {
        super(context);
        mCall = call;
    }

    @Override
    protected void onStartLoading() {
        //Deliver cached data if it is not null
        if (mJsonResponse != null) {
            deliverResult(mJsonResponse);
        } else {
            forceLoad();
        }

        Log.i(LOG_TAG, "onStartLoading() method executed");
    }

    @Override
    public JSONResponse loadInBackground() {
        Log.i(LOG_TAG, "loadInBackground() method executed");

        //Perform Retrofit synchronous request (loadInBackground is already asynchronous)
        try {
            Response<JSONResponse> response = mCall.clone().execute();
            if (response.isSuccessful()) {
                JSONResponse jsonResponse = response.body();
                if(jsonResponse != null) {
                    return jsonResponse;
                }
            } else {
                Log.i(LOG_TAG, "Error loading from API. Response error body: " + response.errorBody());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Save fetched data in cache mJsonResponse
    @Override
    public void deliverResult(JSONResponse data) {
        super.deliverResult(data);
        mJsonResponse = data;
        Log.i(LOG_TAG, "deliverResult() method executed");
    }
}