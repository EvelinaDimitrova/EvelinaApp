package com.fmi.evelina.unimobileapp.helper.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(values.get(position).Location);

        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(values.get(position).Location);

        return label;
    }
}
