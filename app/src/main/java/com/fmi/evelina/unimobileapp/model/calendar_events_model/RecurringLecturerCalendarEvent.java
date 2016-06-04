package com.fmi.evelina.unimobileapp.model.calendar_events_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecurringLecturerCalendarEvent extends RecurringCalendarEvent {

    @Expose
    @SerializedName("program")
    public String Program;

}
