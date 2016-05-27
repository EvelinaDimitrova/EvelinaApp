package com.fmi.evelina.unimobileapp.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fmi.evelina.unimobileapp.R;
import com.fmi.evelina.unimobileapp.activity.SettingsActivity;
import com.fmi.evelina.unimobileapp.controller.ApplicationController;


public class SettingFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(SettingsActivity.KEY_PREF_SERVER_URL)) {
            String newUrl = sharedPreferences.getString(key, "");
            ApplicationController.setServerURLPref(newUrl);
        }
        if (key.equals(SettingsActivity.KEY_PREF_SEND_NETWORK_MODE)) {
            Boolean send = sharedPreferences.getBoolean(key, true);
            ApplicationController.setSendNetworkModePref(send);
        }
        if (key.equals(SettingsActivity.KEY_PREF_LOCALE)) {
            String locale = sharedPreferences.getString(key, "en");
            ApplicationController.setLocalePref(locale);

            //Reload the Activity
            this.getActivity().finish();
            Intent intent = this.getActivity().getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}
