package com.fmi.evelina.unimobileapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * The User model
 */
public class User {

    public User(String userId, String password) {
        this.UserId = userId;
        this.Password = password;
    }

    @Expose @SerializedName("username")
    public String UserId;
    @Expose @SerializedName("password")
    public String Password;
    @Expose @SerializedName("role")
    public String Role;
}
