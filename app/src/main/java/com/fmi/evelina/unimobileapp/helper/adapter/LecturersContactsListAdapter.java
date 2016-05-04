package com.fmi.evelina.unimobileapp.helper.adapter;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fmi.evelina.unimobileapp.R;
import com.fmi.evelina.unimobileapp.model.contacts_model.AdministrationContactData;
import com.fmi.evelina.unimobileapp.model.contacts_model.LecturerContact;

import java.util.List;

public class LecturersContactsListAdapter extends ArrayAdapter<LecturerContact> {
    private final Activity context;
    private final List<LecturerContact> contacts;

    public LecturersContactsListAdapter(Activity context, List<LecturerContact> contacts) {
        super(context, R.layout.news_list_item, contacts);

        this.context=context;
        this.contacts=contacts;
    }

    public View getView(int position,View view,ViewGroup parent) {

        LecturerContact contact = contacts.get(position);

        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.lecturers_contact_list_item, null, true);

        TextView txtName = (TextView) rowView.findViewById(R.id.lecturers_contact_list_item_name);

        txtName.setText(contact.Name);

        return rowView;
    }
}
