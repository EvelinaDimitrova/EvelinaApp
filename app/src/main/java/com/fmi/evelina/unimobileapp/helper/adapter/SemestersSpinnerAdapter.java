package com.fmi.evelina.unimobileapp.helper.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fmi.evelina.unimobileapp.R;
import com.fmi.evelina.unimobileapp.model.student_plan_model.Grade;
import com.fmi.evelina.unimobileapp.model.student_plan_model.Semester;

import java.util.List;

public class SemestersSpinnerAdapter extends ArrayAdapter<Semester> {

    // Your sent context
    private Context context;
    // Your custom values for the spinner (User)
    private List<Semester> values;

    public SemestersSpinnerAdapter(Context context, int textViewResourceId,
                                   List<Semester> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    public int getCount() {
        return values.size();
    }

    public Semester getItem(int position) {
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

//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View row = inflater.inflate(R.layout.spinner_list_item, parent, false);
//        TextView label = (TextView) row.findViewById(R.id.spItem);
//        label.setText(values.get(position).ID);
//
//        return row;

        View view = super.getView(position, convertView, parent);
        TextView textView = (TextView) view;
        if (textView != null){
            textView.setText(values.get(position).ID);
            //textView.setTextAppearance(this.getContext(), android.R.style.TextAppearance_Medium);
        }
        return view;
    }
}
