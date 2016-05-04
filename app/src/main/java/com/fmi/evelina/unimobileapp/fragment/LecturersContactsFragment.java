package com.fmi.evelina.unimobileapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.fmi.evelina.unimobileapp.R;
import com.fmi.evelina.unimobileapp.activity.Contacts.ContactDetailsActivity;
import com.fmi.evelina.unimobileapp.helper.adapter.LecturersContactsListAdapter;
import com.fmi.evelina.unimobileapp.model.contacts_model.LecturerContact;
import com.fmi.evelina.unimobileapp.network.CallBack;
import com.fmi.evelina.unimobileapp.network.NetworkAPI;

import java.util.ArrayList;
import java.util.List;

public class LecturersContactsFragment extends Fragment {

    private LecturersContactsListAdapter lecturersContactsListAdapter;
    private Spinner categorySpinner;
    final List<LecturerContact> contactsList = new ArrayList<>();
    final List<LecturerContact> displayList = new ArrayList<>();
    final List<String> categories = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void updateDisplayedContacts() {

        displayList.clear();
        String selectedCat = categorySpinner.getSelectedItem().toString();
        for (LecturerContact lc : contactsList) {
            if (lc.Department.equals(selectedCat)) {
                displayList.add(lc);
            }
        }
        lecturersContactsListAdapter.notifyDataSetChanged();
    }

    private void populateCategorySpinner() {

        categories.clear();
        for (LecturerContact lc : contactsList) {
            if (!categories.contains(lc.Department)){
                categories.add(lc.Department);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contacts_lecturers_tab, container, false);

        lecturersContactsListAdapter = new LecturersContactsListAdapter(LecturersContactsFragment.this.getActivity(), displayList);
        ListView contactsListView = (ListView) view.findViewById(R.id.lecturers_contacts_listView);
        contactsListView.setAdapter(lecturersContactsListAdapter);

        categorySpinner = (Spinner) view.findViewById(R.id.lecturers_contact_category_spinner);
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
                LecturerContact selectedContact = displayList.get(position);

                Intent intent = new Intent(LecturersContactsFragment.this.getActivity(), ContactDetailsActivity.class);
                Bundle b = new Bundle();
                b.putString(ContactDetailsActivity.CONTACT_NAME_KEY, selectedContact.Name);
                b.putParcelable(ContactDetailsActivity.CONTACT_DATA_KEY, selectedContact.ContactData);
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        NetworkAPI.getLecturersContacts(new CallBack<List<LecturerContact>>() {
            @Override
            public void onSuccess(List<LecturerContact> data) {
                contactsList.clear();
                contactsList.addAll(data);

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