package com.fmi.evelina.unimobileapp.helper.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fmi.evelina.unimobileapp.R;
import com.fmi.evelina.unimobileapp.model.calendar_events_model.Room;
import com.fmi.evelina.unimobileapp.model.student_plan_model.Grade;

import java.util.List;

public class RoomsSpinnerAdapter extends ArrayAdapter<Room> {

    private Context context;
    private List<Room> values;

    public RoomsSpinnerAdapter(Context context, int textViewResourceId,
                               List<Room> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    public int getCount() {
        return values.size();
    }

    public Room getItem(int position) {
        return values.get(position);
    }

    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        View view = super.getView(position, convertView, parent);
        TextView textView = (TextView) view;
        if (textView != null){
            textView.setText(values.get(position).Location);
            //textView.setTextAppearance(this.getContext(), android.R.style.TextAppearance_Medium);
        }
        return view;
    }
}
