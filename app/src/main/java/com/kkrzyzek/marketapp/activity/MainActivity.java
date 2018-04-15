/*
 * MIT License
 *
 *                            Copyright (c) 2018. Krzysztof Krzyzek
 *
 *                            Permission is hereby granted, free of charge, to any person obtaining a copy
 *                            of this software and associated documentation files (the "Software"), to deal
 *                            in the Software without restriction, including without limitation the rights
 *                            to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *                            copies of the Software, and to permit persons to whom the Software is
 *                            furnished to do so, subject to the following conditions:
 *
 *                            The above copyright notice and this permission notice shall be included in all
 *                            copies or substantial portions of the Software.
 *
 *                            THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *                            IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *                            FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *                            AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *                            LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *                            OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *                            SOFTWARE.
 */

package com.kkrzyzek.marketapp.activity;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.DialogInterface;
import android.content.Loader;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.kkrzyzek.marketapp.R;
import com.kkrzyzek.marketapp.adapter.InstrumentAdapter;
import com.kkrzyzek.marketapp.loader.InstrumentLoader;
import com.kkrzyzek.marketapp.model.Instrument;
import com.kkrzyzek.marketapp.model.JSONResponse;
import com.kkrzyzek.marketapp.network.ApiClient;
import com.kkrzyzek.marketapp.network.ApiInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<JSONResponse> {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    //Keys and values for storing info about toolbar state and Call in onSaveInstanceState()
    private static final String KEY_TOOLBAR_TEXT = "toolbar_text";
    private String mToolbarTextString;
    private static final String KEY_TOOLBAR_IMAGE_ID = "toolbar_image";
    private int mToolbarImageId;
    private static final String KEY_CURRENTCALL_FLAG = "current_call_flag";
    private int  mCurrentCallFlag;


    //Adapter for list of objects to display on screen
    private InstrumentAdapter mInstrumentAdapter;

    //Retrofit interface
    private ApiInterface mApiInterface;
    //Returned by API call
    private Call<JSONResponse> mCurrentCall;

    //ProgressBar for loading data and STHWentWrong view for errors
    private ProgressBar mProgressBar;
    private LinearLayout mSthWentWrong;

    //Floating Action Menu for choosing country
    private FloatingActionMenu mFloatingActionMenu;

    //ListView SwipeRefresh Layout
    private SwipeRefreshLayout mSwipeRefreshLayout;

    //Toolbar views
    private TextView mToolbarText;
    private ImageView mToolbarImage;

    //Loader ID
    private int INSTRUMENT_LOADER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //Toolbar config
        Toolbar toolbar = findViewById(R.id.actionbar);
        setSupportActionBar(toolbar);

        mToolbarText = findViewById(R.id.actionbar_text);
        mToolbarImage = findViewById(R.id.actionbar_image);

        //Handling screen rotation
        //Getting saved data from savedInstanceState
        if (savedInstanceState != null) {
            //Get toolbar text and image id; get mCurrentCallFlag - flag for Retrofit Call object
            mToolbarTextString = savedInstanceState.getString(KEY_TOOLBAR_TEXT);
            mToolbarImageId = savedInstanceState.getInt(KEY_TOOLBAR_IMAGE_ID);
            mCurrentCallFlag = savedInstanceState.getInt(KEY_CURRENTCALL_FLAG);
        } else {
            //If no saved data => get default values
            mToolbarTextString = getResources().getString(R.string.uk_markets);
            mToolbarImageId = this
                    .getResources()
                    .getIdentifier("uk", "drawable", this.getPackageName());
            mCurrentCallFlag = 0;
        }
        //Set proper text and image in toolbar
        mToolbarText.setText(mToolbarTextString);
        mToolbarImage.setImageResource(mToolbarImageId);

        //Get a reference to progressBar view
        mProgressBar = findViewById(R.id.progress_bar);

        //STHWentWrong view config - hide
        mSthWentWrong = findViewById(R.id.sth_went_wrong_view);
        mSthWentWrong.setVisibility(View.GONE);


        //Get instance of Retrofit interface
        mApiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        //Call proper GET method in interface to get data (based on mCurrentCall flag)
        switch(mCurrentCallFlag) {
            case 0:
                mCurrentCall = mApiInterface.getENInstrumentData();
                break;
            case 1:
                mCurrentCall = mApiInterface.getDEInstrumentData();
                break;
            case 2:
                mCurrentCall = mApiInterface.getFRInstrumentData();
                break;
            default:
                mCurrentCall = mApiInterface.getENInstrumentData();
        }

        //Set adapter and populate ListView with data
        ListView instrumentListView = findViewById(R.id.instruments_list_view);
        mInstrumentAdapter = new InstrumentAdapter(this, new ArrayList<Instrument>());
        instrumentListView.setAdapter(mInstrumentAdapter);

        //Initiate Loader
        getLoaderManager().initLoader(INSTRUMENT_LOADER_ID, null, MainActivity.this);
        Log.i(LOG_TAG, "initLoader() method executed");


        //Floating Action Menu config
        mFloatingActionMenu = findViewById(R.id.fab);
        mFloatingActionMenu.setIconAnimated(false);
        mFloatingActionMenu.getMenuIconView().setImageResource(R.drawable.ic_fab);

        FloatingActionButton englandButton = findViewById(R.id.en_fab);
        FloatingActionButton germanyButton = findViewById(R.id.ge_fab);
        FloatingActionButton franceButton = findViewById(R.id.fr_fab);

        //England FAB onClick
        englandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Hide Floating Action Menu
                mFloatingActionMenu.close(true);

                //Get toolbar image String
                mToolbarTextString = getResources().getString(R.string.uk_markets);
                //Set toolbar text
                mToolbarText.setText(mToolbarTextString);
                //Get toolbar image ID
                mToolbarImageId = getApplicationContext().getResources().getIdentifier("uk", "drawable", getApplicationContext().getPackageName());
                //Set toolbar image
                mToolbarImage.setImageResource(mToolbarImageId);

                //Check if adapter empty and if not -> clear current data
                clearAdapterIfNotEmpty(mInstrumentAdapter);
                mCurrentCall = mApiInterface.getENInstrumentData();
                //Set flag for Call object
                mCurrentCallFlag = 0;

                mProgressBar.setVisibility(View.VISIBLE);

                //Restart the loader and get new data
                getLoaderManager().restartLoader(INSTRUMENT_LOADER_ID, null, MainActivity.this);
            }
        });

        //Germany FAB onClick
        germanyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFloatingActionMenu.close(true);

                mToolbarTextString = getResources().getString(R.string.germany_markets);
                mToolbarText.setText(mToolbarTextString);
                mToolbarImageId = MainActivity.this
                        .getResources()
                        .getIdentifier("germany", "drawable", MainActivity.this.getPackageName());
                mToolbarImage.setImageResource(mToolbarImageId);

                clearAdapterIfNotEmpty(mInstrumentAdapter);
                mCurrentCall = mApiInterface.getDEInstrumentData();
                mCurrentCallFlag = 1;

                mProgressBar.setVisibility(View.VISIBLE);

                getLoaderManager().restartLoader(INSTRUMENT_LOADER_ID, null, MainActivity.this);
            }
        });

        //France FAB onClick
        franceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFloatingActionMenu.close(true);

                mToolbarTextString = getResources().getString(R.string.france_markets);
                mToolbarText.setText(mToolbarTextString);
                mToolbarImageId = MainActivity.this
                        .getResources()
                        .getIdentifier("france", "drawable", MainActivity.this.getPackageName());
                mToolbarImage.setImageResource(mToolbarImageId);

                clearAdapterIfNotEmpty(mInstrumentAdapter);
                mCurrentCall = mApiInterface.getFRInstrumentData();
                mCurrentCallFlag = 2;

                mProgressBar.setVisibility(View.VISIBLE);

                getLoaderManager().restartLoader(INSTRUMENT_LOADER_ID, null, MainActivity.this);
            }
        });

        //Implement swipe down refreshing functionality
        mSwipeRefreshLayout = findViewById(R.id.swiperefresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                clearAdapterIfNotEmpty(mInstrumentAdapter);

                mProgressBar.setVisibility(View.VISIBLE);
                getLoaderManager().restartLoader(INSTRUMENT_LOADER_ID, null, MainActivity.this);

                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    //Handling screen rotation - saving key-value pairs
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save toolbar text id
        outState.putString(KEY_TOOLBAR_TEXT, mToolbarTextString);
        //Save toolbar image id
        outState.putInt(KEY_TOOLBAR_IMAGE_ID, mToolbarImageId);
        //Save flag for proper Call object
        outState.putInt(KEY_CURRENTCALL_FLAG, mCurrentCallFlag);

        Log.i(LOG_TAG, "onSaveInstanceState executed successfully.");
    }

    //Instantiate proper layout xml into menu in toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    //Define actions for selected menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.info_menu_item:
                showInfoDialog();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //Helper method for building InfoDialog
    private void showInfoDialog () {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.about_app);
        builder.setMessage(R.string.about_app_message);
        builder.setPositiveButton(R.string.back, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                // dismiss the dialog
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //Helper method for clearing adapter
    private void clearAdapterIfNotEmpty (InstrumentAdapter adapter) {
        if (adapter != null && adapter.getCount() != 0) {
            adapter.clear();
        }
    }

    //Create Loader if not running
    @Override
    public Loader<JSONResponse> onCreateLoader(int i, Bundle bundle) {
        Log.i(LOG_TAG, "onCreateLoader() method executed.");
        return new InstrumentLoader(this, mCurrentCall);
    }


    //Update UI by updating data in Adapter
    @Override
    public void onLoadFinished(Loader<JSONResponse> loader, JSONResponse jsonResponse) {
        mInstrumentAdapter.clear();

        if (jsonResponse != null) {
            //Get instruments from jsonResponse and store in ArrayList
            ArrayList<Instrument> instruments = new ArrayList<>(Arrays.asList(jsonResponse.getInstruments()));
            //Sort instruments by displayName
            Collections.sort(instruments, new Comparator<Instrument>() {
                @Override
                public int compare(Instrument instrument1, Instrument instrument2) {
                    return instrument1.getInstrumentName().compareTo(instrument2.getInstrumentName());
                }
            });
            mSthWentWrong.setVisibility(View.GONE);
            //Update data in Adapter
            mInstrumentAdapter.addAll(instruments);
        } else {
            mSthWentWrong.setVisibility(View.VISIBLE);
        }

        mProgressBar.setVisibility(View.GONE);

        Log.e(LOG_TAG, "onLoadFinished() method executed.");
    }

    //Clear data from Loader ,when it is no longer valid
    @Override
    public void onLoaderReset(Loader<JSONResponse> loader) {
        mInstrumentAdapter.clear();
        Log.i(LOG_TAG, "onLoaderReset() method executed");
    }
}

