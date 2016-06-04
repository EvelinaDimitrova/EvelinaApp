package com.fmi.evelina.unimobileapp.model.calendar_events_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Room {

    @Expose
    @SerializedName("id")
    public Integer Id;

    @Expose
    @SerializedName("location")
    public String Location;

}
