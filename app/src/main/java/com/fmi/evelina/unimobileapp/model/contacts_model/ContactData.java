package com.fmi.evelina.unimobileapp.model.contacts_model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContactData implements Parcelable {


    @Expose @SerializedName("phone")
    public String Phone;
    @Expose @SerializedName("email")
    public String Email;
    @Expose @SerializedName("room")
    public String Room;
    @Expose @SerializedName("time")
    public String Time;

    protected ContactData(Parcel in) {
        Phone = in.readString();
        Email = in.readString();
        Room = in.readString();
        Time = in.readString();
    }

    public ContactData(){

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Phone);
        dest.writeString(Email);
        dest.writeString(Room);
        dest.writeString(Time);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ContactData> CREATOR = new Parcelable.Creator<ContactData>() {
        @Override
        public ContactData createFromParcel(Parcel in) {
            return new ContactData(in);
        }

        @Override
        public ContactData[] newArray(int size) {
            return new ContactData[size];
        }
    };
}