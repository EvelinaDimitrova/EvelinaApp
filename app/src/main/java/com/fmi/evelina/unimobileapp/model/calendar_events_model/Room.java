package com.fmi.evelina.unimobileapp.model.calendar_events_model;

import android.graphics.Bitmap;

import com.fmi.evelina.unimobileapp.controller.ApplicationController;
import com.fmi.evelina.unimobileapp.helper.ImageHelper;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class Room {

    @Expose
    @SerializedName("id")
    public Integer Id;

    @Expose
    @SerializedName("location")
    public String Location;

}
