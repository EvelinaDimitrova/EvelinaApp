package com.fmi.evelina.unimobileapp.model.calendar_events_model;

import com.alamkanak.weekview.WeekViewEvent;
import com.fmi.evelina.unimobileapp.helper.EventColorMap;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

/**
 * A Calendar Event model object.
 */
public class CalendarEvent {

    public int Id;

    @Expose
    @SerializedName("id")
    public int DbKey;

    @Expose
    @SerializedName("start_time")
    public Time StartTime;

    @Expose
    @SerializedName("end_time")
    public Time EndTime;

    @Expose
    @SerializedName("event_date")
    public Date EventDate;

    @Expose
    @SerializedName("abbreviation")
    public String Abbreviation;

    @Expose
    @SerializedName("name")
    public String Name;

    @Expose
    @SerializedName("description")
    public String Description;

    @Expose
    @SerializedName("location")
    public String Location;

    @Expose
    @SerializedName("room_id")
    public Integer RoomId;

    @Expose
    @SerializedName("type_code")
    public String TypeCode;


    public WeekViewEvent getWeekViewEvent() {

        Calendar start = Calendar.getInstance();
        start.setTime(EventDate);
        start.set(Calendar.HOUR_OF_DAY, StartTime.getHours());
        start.set(Calendar.MINUTE,StartTime.getMinutes());

        Calendar end = Calendar.getInstance();
        end.setTime(EventDate);
        end.set(Calendar.HOUR_OF_DAY, EndTime.getHours());
        end.set(Calendar.MINUTE, EndTime.getMinutes());


        //Create a WeekViewEvent for the current date
        WeekViewEvent weekViewEvent = new WeekViewEvent(Id,
                Abbreviation,
                Location,
                start,
                end);

        weekViewEvent.setColor(EventColorMap.getEventColor(TypeCode));

        return weekViewEvent;
    }

}
