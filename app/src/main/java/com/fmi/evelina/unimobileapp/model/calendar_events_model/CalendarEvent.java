package com.fmi.evelina.unimobileapp.model.calendar_events_model;

import android.graphics.Color;

import com.alamkanak.weekview.WeekViewEvent;
import com.fmi.evelina.unimobileapp.helper.EventColorMap;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A Calendar Event model object.
 */
public class CalendarEvent {

    @Expose
    @SerializedName("id")
    public int Id;

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

        LocalTime startTime = new LocalTime(StartTime);
        LocalTime endTime = new LocalTime(EndTime);

        LocalDate eventDate = new LocalDate(EventDate);


        //Set the start date and time
        DateTime startDateTime = new DateTime(eventDate.getYear(),
                eventDate.getMonthOfYear(),
                eventDate.getDayOfMonth(),
                startTime.getHourOfDay(),
                startTime.getMinuteOfHour(),
                0);
        //Set the end date and time
        DateTime endDateTime = new DateTime(eventDate.getYear(),
                eventDate.getMonthOfYear(),
                eventDate.getDayOfMonth(),
                endTime.getHourOfDay(),
                endTime.getMinuteOfHour(),
                0);

        //Create a WeekViewEvent for the current date
        WeekViewEvent weekViewEvent = new WeekViewEvent(Id,
                Abbreviation,
                Location,
                startDateTime.toGregorianCalendar(),
                endDateTime.toGregorianCalendar());

        weekViewEvent.setColor(EventColorMap.getEventColor(TypeCode));

        return weekViewEvent;
    }

}
