package com.fmi.evelina.unimobileapp.controller;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.fmi.evelina.unimobileapp.R;
import com.fmi.evelina.unimobileapp.helper.DateDeserializer;
import com.fmi.evelina.unimobileapp.helper.TimeDeserializer;
import com.fmi.evelina.unimobileapp.model.ElectionCampaign;
import com.fmi.evelina.unimobileapp.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Time;
import java.util.Date;

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

    public static boolean isLoggedIn() {
        return _user != null;
    }
    public static String getServerURL() {return getInstance().getString(R.string.server_url);}

    private static User _user = null;
    private static Gson _gson;


    @Override
    public void onCreate() {
        super.onCreate();

        //Create the Gson object
        GsonBuilder gSonBuilder = new GsonBuilder();
        gSonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
        gSonBuilder.registerTypeAdapter(Time.class, new TimeDeserializer());
        _gson = gSonBuilder.create();

        // initialize the singleton
        _sInstance = this;

        Log.v("onCreate ", (_sInstance == null ? "true" : "false"));
    }

    /**
     * @return ApplicationController singleton instance
     */
    public static synchronized ApplicationController getInstance() {

        Log.v("getInstance ", (_sInstance == null ? "true" : "false"));

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
}