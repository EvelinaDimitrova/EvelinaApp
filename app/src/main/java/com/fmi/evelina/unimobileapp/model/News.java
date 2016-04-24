package com.fmi.evelina.unimobileapp.model;

import android.graphics.Bitmap;

import com.alamkanak.weekview.WeekViewEvent;
import com.fmi.evelina.unimobileapp.helper.EventColorMap;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.sql.Time;
import java.util.Date;

/**
 * A Calendar News model object.
 */
public class News {

    @Expose
    @SerializedName("id")
    public int Id;

    @Expose
    @SerializedName("title")
    public String Title;

    @Expose
    @SerializedName("creation_datetime")
    public Date Date;

    @Expose
    @SerializedName("text")
    public String Text;

    @Expose
    @SerializedName("image")
    public String ImageName;

    public Bitmap Image;

}
