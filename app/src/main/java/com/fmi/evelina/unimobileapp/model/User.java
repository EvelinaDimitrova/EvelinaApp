package com.fmi.evelina.unimobileapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * The User model
 */
public class User {

    public User(String userId, String password) {
        this.mUserId = userId;
        this.mPassword = password;
    }

    @Expose @SerializedName("userid")
    private String mUserId;
    @Expose @SerializedName("password")
    private String mPassword;

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        this.mUserId = userId;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        this.mPassword = password;
    }
}
