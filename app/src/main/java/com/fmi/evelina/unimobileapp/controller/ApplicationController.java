package com.fmi.evelina.unimobileapp.controller;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.fmi.evelina.unimobileapp.R;
import com.fmi.evelina.unimobileapp.activity.SettingsActivity;
import com.fmi.evelina.unimobileapp.helper.DateDeserializer;
import com.fmi.evelina.unimobileapp.helper.TimeDeserializer;
import com.fmi.evelina.unimobileapp.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ApplicationController extends Application {

    /**
     * Log or request TAG
     */
    public static final String TAG = "VolleyPatterns";

    /**
     * Global request queue for Volley
     */
    private RequestQueue _mRequestQueue;

    /**
     * A singleton instance of the application class for easy access in other places
     */
    private static ApplicationController _sInstance;
    private static User _user = null;
    private static Gson _gson;
    private static DataProvider _dataProvider;

    private static String _serverURLPref;
    private static String _localePref;
    private static String _contentModePref;

    public static SimpleDateFormat fullDateTimeFormat = new SimpleDateFormat("dd MMM yyyy HH:mm");

    public static boolean isLoggedIn() {
        return _user != null;
    }

    public static String getServerURLPref() {
        return _serverURLPref;
    }

    public static void setServerURLPref(String url) {
        ApplicationController._serverURLPref = url;
    }

    public static String getContentPref() {
        return _contentModePref;
    }

    public static void setContentModePref(String send) {
        ApplicationController._contentModePref = send;
    }

    public static String getLocalePref() {
        return _localePref;
    }

    public static void setLocalePref(String _localePref) {
        ApplicationController._localePref = _localePref;
        changeLocale();
    }

    private static void changeLocale() {
        Configuration config = getInstance().getBaseContext().getResources().getConfiguration();
        Locale locale = new Locale(getLocalePref());
        Locale.setDefault(locale);
        config.locale = locale;
        getInstance().getBaseContext().getResources().updateConfiguration(config, getInstance().getBaseContext().getResources().getDisplayMetrics());
    }

    public static void showErrorToast(){
        Toast.makeText(getInstance(), R.string.error_message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //Create the Gson object
        GsonBuilder gSonBuilder = new GsonBuilder();
        gSonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
        gSonBuilder.registerTypeAdapter(Time.class, new TimeDeserializer());

        _gson = gSonBuilder.serializeNulls().create();
        // initialize the singleton
        _sInstance = this;

        _dataProvider = new DataProvider(getApplicationContext());

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        _serverURLPref = sharedPref.getString(getString(R.string.PREF_SERVER_URL), getString(R.string.pref_default_url));
        _contentModePref = sharedPref.getString(getString(R.string.PREF_CONTENT_MODE), getString(R.string.pref_content_mode_default));
        setLocalePref(sharedPref.getString(getString(R.string.PREF_LANGUAGE), getString(R.string.pref_lang_default)));

    }

    /**
     * @return ApplicationController singleton instance
     */
    public static synchronized ApplicationController getInstance() {
        return _sInstance;
    }

    /**
     * @return The Volley Request queue, the queue will be created if it is null
     */
    public RequestQueue getRequestQueue() {
        // lazy initialize the request queue, the queue instance will be
        // created when it is accessed for the first time
        if (_mRequestQueue == null) {
            _mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return _mRequestQueue;
    }

    /**
     * Adds the specified request to the global queue, if tag is specified
     * then it is used else Default TAG is used.
     *
     * @param req
     * @param tag
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

        VolleyLog.d("Adding request to queue: %s", req.getUrl());

        getRequestQueue().add(req);
    }

    /**
     * Adds the specified request to the global queue using the Default TAG.
     *
     * @param req
     */
    public <T> void addToRequestQueue(Request<T> req) {
        // set the default tag if tag is empty
        req.setTag(TAG);

        getRequestQueue().add(req);
    }

    /**
     * Cancels all pending requests by the specified TAG, it is important
     * to specify a TAG so that the pending/ongoing requests can be cancelled.
     *
     * @param tag
     */
    public void cancelPendingRequests(Object tag) {
        if (_mRequestQueue != null) {
            _mRequestQueue.cancelAll(tag);
        }
    }

    public static User getLoggedUser() {
        return _user;
    }

    public static void setLoggedUser(User user) {
        _user = user;
    }

    public static Gson getGson() {
        return _gson;
    }

    public static File getFileDir() {
        return getInstance().getCacheDir();
    }

    public static DataProvider getDataProvider() {
        return _dataProvider;
    }

    public static boolean hasNetwork() {
        ConnectivityManager cm = (ConnectivityManager) getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public static int getNetworkType() {
        if (hasNetwork()) {
            ConnectivityManager cm = (ConnectivityManager) getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork.getType();
        }
        return -1;

    }
}