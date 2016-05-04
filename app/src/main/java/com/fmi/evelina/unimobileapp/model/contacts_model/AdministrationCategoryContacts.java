package com.fmi.evelina.unimobileapp.model.contacts_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AdministrationCategoryContacts {

    @Expose @SerializedName("category")
    public String Category;
    @Expose @SerializedName("data")
    public List<AdministrationContactData> Contacts;
}
