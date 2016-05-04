package com.fmi.evelina.unimobileapp.model.calendar_events_model;

import android.util.Log;

import com.alamkanak.weekview.WeekViewEvent;
import com.fmi.evelina.unimobileapp.helper.EventColorMap;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.List;

public class RecurringLecturerCalendarEvent extends RecurringCalendarEvent {

    @Expose
    @SerializedName("program")
    public String Program;

}
