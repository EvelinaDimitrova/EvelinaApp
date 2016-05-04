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
import java.util.Date;
import java.util.List;

/**
 * A Calendar Event model object.
 */
public class RecurringCalendarEvent extends CalendarEvent {


    @Expose
    @SerializedName("week_day")
    public int WeekDay;

    @Expose
    @SerializedName("start_date")
    public Date StartDate;

    @Expose
    @SerializedName("end_date")
    public Date EndDate;

    @Expose
    @SerializedName("type_name")
    public String TypeName;

    @Expose
    @SerializedName("flg_elective")
    public Character IsElective;


    public List<WeekViewEvent> getWeekViewEvents() {

        List<WeekViewEvent> result = new ArrayList<>();
        LocalTime startTime = new LocalTime(StartTime);
        LocalTime endTime = new LocalTime(EndTime);
        LocalDate endDate = new LocalDate(EndDate);

        //Get first weekday after period start
        LocalDate firstOccurrence = new LocalDate(StartDate);
        while( firstOccurrence.getDayOfWeek() != WeekDay )
            firstOccurrence = firstOccurrence.plusDays(1);

        LocalDate currentDate = firstOccurrence;
        while (currentDate.isBefore(endDate)) {

            //Set the start date and time
            DateTime startDateTime = new DateTime(currentDate.getYear(),
                    currentDate.getMonthOfYear(),
                    currentDate.getDayOfMonth(),
                    startTime.getHourOfDay(),
                    startTime.getMinuteOfHour(),
                    0);
            //Set the end date and time
            DateTime endDateTime = new DateTime(currentDate.getYear(),
                    currentDate.getMonthOfYear(),
                    currentDate.getDayOfMonth(),
                    endTime.getHourOfDay(),
                    endTime.getMinuteOfHour(),
                    0);

           //Create a WeekViewEvent for the current date
            WeekViewEvent weekViewEvent = new WeekViewEvent(Id,
                    Abbreviation,
                    Location,
                    startDateTime.toGregorianCalendar(),
                    endDateTime.toGregorianCalendar());

            Log.v("result_color=", "" + EventColorMap.getEventColor(TypeCode));
            
            weekViewEvent.setColor(EventColorMap.getEventColor(TypeCode));

            result.add(weekViewEvent);

            currentDate = currentDate.plusWeeks(1);
        }
        return result;
    }
}
