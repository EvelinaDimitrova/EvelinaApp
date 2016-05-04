package com.fmi.evelina.unimobileapp.model.election_camaign_model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;


public class ElectionCampaign implements Parcelable {

    @Expose
    @SerializedName("id")
    public int Id;

    @Expose
    @SerializedName("start_date")
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
        return today.after(CloseDate) && today.before(EndDate);
    }

    public boolean IsEnded(){
        Date today = new Date();
        return today.after(EndDate);
    }


    protected ElectionCampaign(Parcel in) {
        Id = in.readInt();
        long tmpOpenDate = in.readLong();
        OpenDate = tmpOpenDate != -1 ? new Date(tmpOpenDate) : null;
        long tmpCloseDate = in.readLong();
        CloseDate = tmpCloseDate != -1 ? new Date(tmpCloseDate) : null;
        long tmpEndDate = in.readLong();
        EndDate = tmpEndDate != -1 ? new Date(tmpEndDate) : null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeLong(OpenDate != null ? OpenDate.getTime() : -1L);
        dest.writeLong(CloseDate != null ? CloseDate.getTime() : -1L);
        dest.writeLong(EndDate != null ? EndDate.getTime() : -1L);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ElectionCampaign> CREATOR = new Parcelable.Creator<ElectionCampaign>() {
        @Override
        public ElectionCampaign createFromParcel(Parcel in) {
            return new ElectionCampaign(in);
        }

        @Override
        public ElectionCampaign[] newArray(int size) {
            return new ElectionCampaign[size];
        }
    };
}