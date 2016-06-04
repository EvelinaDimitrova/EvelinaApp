package com.fmi.evelina.unimobileapp.model.calendar_events_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecurringStudentCalendarEvent extends RecurringCalendarEvent {

    @Expose
    @SerializedName("lecturer")
    public String Lecturer;

}
