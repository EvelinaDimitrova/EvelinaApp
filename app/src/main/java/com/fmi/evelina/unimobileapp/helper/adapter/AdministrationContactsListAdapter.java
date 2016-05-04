package com.fmi.evelina.unimobileapp.helper.adapter;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fmi.evelina.unimobileapp.R;
import com.fmi.evelina.unimobileapp.model.contacts_model.AdministrationContactData;

import java.util.List;

public class AdministrationContactsListAdapter extends ArrayAdapter<AdministrationContactData> {
    private final Activity context;
    private final List<AdministrationContactData> contacts;

    public AdministrationContactsListAdapter(Activity context, List<AdministrationContactData> contacts) {
        super(context, R.layout.news_list_item, contacts);

        this.context=context;
        this.contacts=contacts;
    }

    public View getView(int position,View view,ViewGroup parent) {

        AdministrationContactData contact = contacts.get(position);

        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.administration_contact_list_item, null, true);

        TextView txtName = (TextView) rowView.findViewById(R.id.admin_contact_list_item_name);
        TextView txtJob = (TextView) rowView.findViewById(R.id.admin_contact_list_item_job);

        txtName.setText(contact.Name);
        txtJob.setText(contact.Job);

        return rowView;
    }
}
