package com.fmi.evelina.unimobileapp.model.contacts_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdministrationContactData {


    @Expose @SerializedName("name")
    public String Name;
    @Expose @SerializedName("job")
    public String Job;
    @Expose @SerializedName("contactData")
    public ContactData ContactInfo;
}
