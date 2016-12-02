package com.example.paper.paperinteractive.Objects;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.paper.paperinteractive.R;
import com.google.android.gms.vision.text.Text;

/**
 * Created by Eric on 2016-11-30.
 */

public class CustomSpinnerAdapter extends ArrayAdapter<Child> {

    private Context context;

    private Child[] values;

    public CustomSpinnerAdapter(Context context, int resource, Child[] objects) {
        super(context, resource, objects);
        this.context = context;
        this.values = objects;
    }

    public int getCount(){
        return values.length;
    }

    public Child getItem(int position){
        return values[position];
    }

    public long getItemId(int position){
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Inflate the view
        View v = convertView;

        if (v == null){
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.spinner_layout, null);
        }

        TextView tv = (TextView) v.findViewById(R.id.customSpinnerItemTextView);
        tv.setText(values[position].getName());

        return v;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        // Inflate the view
        View v = convertView;

        if (v == null){
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.spinner_layout, null);
        }

        TextView tv = (TextView) v.findViewById(R.id.customSpinnerItemTextView);
        tv.setText(values[position].getName());

        return v;
    }
}
