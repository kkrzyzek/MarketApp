package com.kkrzyzek.marketapp.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.kkrzyzek.marketapp.model.Instrument;
import com.kkrzyzek.marketapp.model.JSONResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Custom Loader for handling device rotations
 */
public class InstrumentLoader extends AsyncTaskLoader<List<Instrument>> {

    private static final String LOG_TAG = InstrumentLoader.class.getName();
    private Call<JSONResponse> mCall;

    public InstrumentLoader (Context context, Call<JSONResponse> call) {
        super(context);
        mCall = call;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        Log.i(LOG_TAG, "onStartLoading() method executed");
    }

    @Override
    public List<Instrument> loadInBackground() {
        Log.i(LOG_TAG, "loadInBackground() method executed");

        //Performing Retrofit synchronous request (loadInBackground is already asynchronous)
        try {
            Response<JSONResponse> response = mCall.clone().execute();
            if (response.isSuccessful()) {
                JSONResponse jsonResponse = response.body();
                if(jsonResponse != null) {
                    return new ArrayList<>(Arrays.asList(jsonResponse.getInstruments()));
                }
            } else {
                Log.i(LOG_TAG, "Error loading from API. Response error body: " + response.errorBody());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
