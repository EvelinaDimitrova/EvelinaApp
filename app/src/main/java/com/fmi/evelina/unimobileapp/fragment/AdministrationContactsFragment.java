package com.fmi.evelina.unimobileapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.fmi.evelina.unimobileapp.R;
import com.fmi.evelina.unimobileapp.activity.Contacts.ContactDetailsActivity;
import com.fmi.evelina.unimobileapp.helper.adapter.AdministrationContactsListAdapter;
import com.fmi.evelina.unimobileapp.model.contacts_model.AdministrationContactData;
import com.fmi.evelina.unimobileapp.model.contacts_model.AdministrationCategoryContacts;
import com.fmi.evelina.unimobileapp.network.CallBack;
import com.fmi.evelina.unimobileapp.network.NetworkAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdministrationContactsFragment extends Fragment {

    private AdministrationContactsListAdapter administrationContactsListAdapter;
    private Spinner categorySpinner;
    final Map<String, List<AdministrationContactData>> contactsList = new HashMap<>();
    final List<String> categories = new ArrayList<>();
    final List<AdministrationContactData> displayList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void updateDisplayedContacts() {
        String selectedCat = categorySpinner.getSelectedItem().toString();
        Log.v("EVE_TRACE","updateDisplayedContacts" + selectedCat);
        if (contactsList.containsKey(selectedCat)){
            displayList.clear();
            displayList.addAll(contactsList.get(selectedCat));

            administrationContactsListAdapter.notifyDataSetChanged();
        }
    }

    private void populateCategorySpinner() {

        categories.clear();
        categories.addAll(contactsList.keySet());

        ArrayAdapter<String> adapter =  new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contacts_administration_tab, container, false);

        administrationContactsListAdapter = new AdministrationContactsListAdapter(AdministrationContactsFragment.this.getActivity(), displayList);
        ListView contactsListView = (ListView) view.findViewById(R.id.admin_contacts_listView);
        contactsListView.setAdapter(administrationContactsListAdapter);

        categorySpinner = (Spinner) view.findViewById(R.id.admin_contact_category_spinner);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateDisplayedContacts();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        contactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AdministrationContactData selectedContact = displayList.get(position);

                Intent intent = new Intent(AdministrationContactsFragment.this.getActivity(), ContactDetailsActivity.class);
                Bundle b = new Bundle();
                b.putString(ContactDetailsActivity.CONTACT_NAME_KEY, selectedContact.Name);
                b.putString(ContactDetailsActivity.CONTACT_JOB_KEY, selectedContact.Job);
                b.putParcelable(ContactDetailsActivity.CONTACT_DATA_KEY, selectedContact.ContactInfo);
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        NetworkAPI.getAdministrationContacts(new CallBack<List<AdministrationCategoryContacts>>() {
            @Override
            public void onSuccess(List<AdministrationCategoryContacts> data) {
                for (AdministrationCategoryContacts cont : data) {
                    contactsList.put(cont.Category, cont.Contacts);
                }
                populateCategorySpinner();
                updateDisplayedContacts();
            }

            @Override
            public void onFail(String msg) {

            }
        });

        return view;
    }
}