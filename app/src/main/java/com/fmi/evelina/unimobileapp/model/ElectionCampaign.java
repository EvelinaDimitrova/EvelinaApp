package com.fmi.evelina.unimobileapp.model;

import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;


public class ElectionCampaign {

    @Expose
    @SerializedName("id")
    public int Id;

    @Expose
    @SerializedName("open_date")
    public Date OpenDate;

    @Expose
    @SerializedName("close_date")
    public Date CloseDate;

    @Expose
    @SerializedName("end_date")
    public Date EndDate;

    public boolean IsOpen(){
        Date today = new Date();
        return today.after(OpenDate) && today.before(CloseDate);
    }

    public boolean IsClosed(){
        Date today = new Date();
        return today.after(CloseDate);
    }

    public boolean IsEnded(){
        Date today = new Date();
        return today.after(EndDate);
    }

}
