package com.fmi.evelina.unimobileapp.model.contacts_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LecturerContact {

    @Expose @SerializedName("name")
    public String Name;
    @Expose @SerializedName("department")
    public String Department;
    @Expose @SerializedName("contacts")
    public ContactData ContactData;
}
