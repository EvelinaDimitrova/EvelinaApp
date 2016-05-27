package com.fmi.evelina.unimobileapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.fmi.evelina.unimobileapp.R;
import com.fmi.evelina.unimobileapp.controller.ApplicationController;
import com.fmi.evelina.unimobileapp.fragment.SettingFragment;

public class SettingsActivity extends AppCompatActivity {

    public static final String KEY_PREF_SERVER_URL = "pref_server_url";
    public static final String KEY_PREF_SEND_NETWORK_MODE = "pref_send_network_mode";
    public static final String KEY_PREF_LOCALE = "pref_language";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingFragment())
                .commit();
    }

    @Override
    public void onBackPressed() {
        Intent home = new Intent(SettingsActivity.this, HomeActivity.class);
        startActivity(home);
    }

    //Reset the title
    @Override
    protected void onResume() {
        super.onResume();
        this.setTitle(getString(R.string.title_activity_settings));
    }
}
