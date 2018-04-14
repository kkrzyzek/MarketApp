package com.kkrzyzek.marketapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kkrzyzek.marketapp.R;
import com.kkrzyzek.marketapp.model.Instrument;

import java.util.ArrayList;

/**
 * Custom ArrayAdapter for displaying objects in listView
 */
public class InstrumentAdapter extends ArrayAdapter<Instrument> {

    public InstrumentAdapter(@NonNull Context context, ArrayList<Instrument> instruments) {
        super(context, 0, instruments);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;

        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.instrument_list_item, parent, false);
        }

        Instrument activeInstrument = getItem(position);

        if (activeInstrument != null) {
            TextView instrumentName = listItemView.findViewById(R.id.instrument_name_text);
            instrumentName.setText(activeInstrument.getInstrumentName());

            TextView instrumentOffer = listItemView.findViewById(R.id.instrument_offer_text);
            instrumentOffer.setText(activeInstrument.getOffer());
        }
        return listItemView;
    }

}
