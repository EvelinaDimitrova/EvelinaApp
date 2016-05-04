package com.fmi.evelina.unimobileapp.localDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.fmi.evelina.unimobileapp.model.calendar_events_model.CalendarEvent;
import com.fmi.evelina.unimobileapp.model.calendar_events_model.RecurringLecturerCalendarEvent;
import com.fmi.evelina.unimobileapp.model.calendar_events_model.RecurringStudentCalendarEvent;
import com.fmi.evelina.unimobileapp.network.CallBack;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 *
 */
public class DataBaseAPI {

    private static SQLiteDatabase readableDB, writableDB;
    private static final String DATE_FORMAT_SHORT = "yyyy-MM-dd";
    private static final DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_SHORT);
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final DateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);

    public DataBaseAPI(Context context) {
        DataBaseHelper dbHelper = new DataBaseHelper(context);

        readableDB = dbHelper.getReadableDatabase();
        writableDB = dbHelper.getWritableDatabase();
    }

    public static void putEvents(List<CalendarEvent> data) {
        //Clear the Events table
        writableDB.delete(DataBaseContract.Events.TABLE_NAME, null, null);

        //Insert the events
        for (CalendarEvent event : data) {
            Log.v("EVE_TRACE", "Inserting event.");
            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(DataBaseContract.Events.COLUMN_NAME_ABBR, event.Abbreviation);
            values.put(DataBaseContract.Events.COLUMN_NAME_NAME, event.Name);
            values.put(DataBaseContract.Events.COLUMN_NAME_DESC, event.Description);
            values.put(DataBaseContract.Events.COLUMN_NAME_LOCATION, event.Location);
            values.put(DataBaseContract.Events.COLUMN_NAME_TYPE_CODE, event.TypeCode);
            values.put(DataBaseContract.Events.COLUMN_NAME_DATE, dateFormat.format(event.EventDate));
            values.put(DataBaseContract.Events.COLUMN_NAME_START_TIME, timeFormat.format(event.StartTime));
            values.put(DataBaseContract.Events.COLUMN_NAME_END_TIME, timeFormat.format(event.EndTime));

            // Insert the new row, returning the primary key value of the new row
            long newRowId;
            newRowId = writableDB.insert(
                    DataBaseContract.Events.TABLE_NAME,
                    //TODO verify second parameter
                    null,
                    values);
        }
    }

    public static void getEvents(final CallBack<List<CalendarEvent>> onCallBack) {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                DataBaseContract.Events._ID,
                DataBaseContract.Events.COLUMN_NAME_ABBR,
                DataBaseContract.Events.COLUMN_NAME_NAME,
                DataBaseContract.Events.COLUMN_NAME_DESC,
                DataBaseContract.Events.COLUMN_NAME_LOCATION,
                DataBaseContract.Events.COLUMN_NAME_TYPE_CODE,
                DataBaseContract.Events.COLUMN_NAME_DATE,
                DataBaseContract.Events.COLUMN_NAME_START_TIME,
                DataBaseContract.Events.COLUMN_NAME_END_TIME
        };

        // How you want the results sorted in the resulting Cursor
        // String sortOrder = FeedEntry.COLUMN_NAME_UPDATED + " DESC";

        Cursor cursor = readableDB.query(
                DataBaseContract.Events.TABLE_NAME,         // The table to query
                projection,                                 // The columns to return
                null,                                       // The columns for the WHERE clause
                null,                                       // The values for the WHERE clause
                null,                                       // don't group the rows
                null,                                       // don't filter by row groups
                null                                         // The sort order
        );

        List<CalendarEvent> result = new ArrayList<>();

        if (cursor != null) {
            // move cursor to first row
            if (cursor.moveToFirst()) {
                do {
                    CalendarEvent event = new CalendarEvent();
                    event.Abbreviation = cursor.getString(cursor.getColumnIndex(DataBaseContract.Events.COLUMN_NAME_ABBR));
                    event.Name = cursor.getString(cursor.getColumnIndex(DataBaseContract.Events.COLUMN_NAME_NAME));
                    event.Description = cursor.getString(cursor.getColumnIndex(DataBaseContract.Events.COLUMN_NAME_DESC));
                    event.Location = cursor.getString(cursor.getColumnIndex(DataBaseContract.Events.COLUMN_NAME_LOCATION));
                    event.TypeCode = cursor.getString(cursor.getColumnIndex(DataBaseContract.Events.COLUMN_NAME_TYPE_CODE));
                    event.EventDate = getDate(cursor.getString(cursor.getColumnIndex(DataBaseContract.Events.COLUMN_NAME_DATE)));
                    event.StartTime = getTime(cursor.getString(cursor.getColumnIndex(DataBaseContract.Events.COLUMN_NAME_START_TIME)));
                    event.EndTime = getTime(cursor.getString(cursor.getColumnIndex(DataBaseContract.Events.COLUMN_NAME_END_TIME)));

                    result.add(event);
                    // move to next row
                } while (cursor.moveToNext());
            }
        }
        onCallBack.onSuccess(result);
    }

    public static void putLecturerSchedule(List<RecurringLecturerCalendarEvent> data) {
        //Clear the Events table
        writableDB.delete(DataBaseContract.RecurringEvents.TABLE_NAME, null, null);

        //Insert the events
        for (RecurringLecturerCalendarEvent event : data) {
            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(DataBaseContract.RecurringEvents.COLUMN_NAME_ABBR, event.Abbreviation);
            values.put(DataBaseContract.RecurringEvents.COLUMN_NAME_NAME, event.Name);
            values.put(DataBaseContract.RecurringEvents.COLUMN_NAME_DESC, event.Description);
            values.put(DataBaseContract.RecurringEvents.COLUMN_NAME_LOCATION, event.Location);
            values.put(DataBaseContract.RecurringEvents.COLUMN_NAME_TYPE_CODE, event.TypeCode);
            values.put(DataBaseContract.RecurringEvents.COLUMN_NAME_TYPE_NAME, event.TypeName);
            values.put(DataBaseContract.RecurringEvents.COLUMN_NAME_START_DATE, dateFormat.format(event.StartDate));
            values.put(DataBaseContract.RecurringEvents.COLUMN_NAME_END_DATE, dateFormat.format(event.EndDate));
            values.put(DataBaseContract.RecurringEvents.COLUMN_NAME_START_TIME, timeFormat.format(event.StartTime));
            values.put(DataBaseContract.RecurringEvents.COLUMN_NAME_END_TIME, timeFormat.format(event.EndTime));
            values.put(DataBaseContract.RecurringEvents.COLUMN_NAME_WEEKDAY, event.WeekDay);
            values.put(DataBaseContract.RecurringEvents.COLUMN_NAME_ELECTIVE, event.IsElective.toString());
            values.put(DataBaseContract.RecurringEvents.COLUMN_NAME_PROGRAM, event.Program);

            // Insert the new row, returning the primary key value of the new row
            long newRowId;
            newRowId = writableDB.insert(
                    DataBaseContract.RecurringEvents.TABLE_NAME,
                    //TODO verify second parameter
                    null,
                    values);
        }
    }

    public static void getLecturerSchedule(final CallBack<List<RecurringLecturerCalendarEvent>> onCallBack) {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                DataBaseContract.RecurringEvents._ID,
                DataBaseContract.RecurringEvents.COLUMN_NAME_ABBR,
                DataBaseContract.RecurringEvents.COLUMN_NAME_NAME,
                DataBaseContract.RecurringEvents.COLUMN_NAME_DESC,
                DataBaseContract.RecurringEvents.COLUMN_NAME_LOCATION,
                DataBaseContract.RecurringEvents.COLUMN_NAME_TYPE_CODE,
                DataBaseContract.RecurringEvents.COLUMN_NAME_TYPE_NAME,
                DataBaseContract.RecurringEvents.COLUMN_NAME_START_DATE,
                DataBaseContract.RecurringEvents.COLUMN_NAME_END_DATE,
                DataBaseContract.RecurringEvents.COLUMN_NAME_START_TIME,
                DataBaseContract.RecurringEvents.COLUMN_NAME_END_TIME,
                DataBaseContract.RecurringEvents.COLUMN_NAME_WEEKDAY,
                DataBaseContract.RecurringEvents.COLUMN_NAME_ELECTIVE,
                DataBaseContract.RecurringEvents.COLUMN_NAME_PROGRAM
        };


        // How you want the results sorted in the resulting Cursor
        // String sortOrder = FeedEntry.COLUMN_NAME_UPDATED + " DESC";

        Cursor cursor = readableDB.query(
                DataBaseContract.RecurringEvents.TABLE_NAME,         // The table to query
                projection,                                 // The columns to return
                null,                                       // The columns for the WHERE clause
                null,                                       // The values for the WHERE clause
                null,                                       // don't group the rows
                null,                                       // don't filter by row groups
                null                                         // The sort order
        );

        List<RecurringLecturerCalendarEvent> result = new ArrayList<>();

        if (cursor != null) {
            // move cursor to first row
            if (cursor.moveToFirst()) {
                do {
                    RecurringLecturerCalendarEvent event = new RecurringLecturerCalendarEvent();
                    event.Abbreviation = cursor.getString(cursor.getColumnIndex(DataBaseContract.RecurringEvents.COLUMN_NAME_ABBR));
                    event.Name = cursor.getString(cursor.getColumnIndex(DataBaseContract.RecurringEvents.COLUMN_NAME_NAME));
                    event.Description = cursor.getString(cursor.getColumnIndex(DataBaseContract.RecurringEvents.COLUMN_NAME_DESC));
                    event.Location = cursor.getString(cursor.getColumnIndex(DataBaseContract.RecurringEvents.COLUMN_NAME_LOCATION));
                    event.TypeCode = cursor.getString(cursor.getColumnIndex(DataBaseContract.RecurringEvents.COLUMN_NAME_TYPE_CODE));
                    event.TypeName = cursor.getString(cursor.getColumnIndex(DataBaseContract.RecurringEvents.COLUMN_NAME_TYPE_NAME));
                    event.StartDate = getDate(cursor.getString(cursor.getColumnIndex(DataBaseContract.RecurringEvents.COLUMN_NAME_START_DATE)));
                    event.EndDate = getDate(cursor.getString(cursor.getColumnIndex(DataBaseContract.RecurringEvents.COLUMN_NAME_END_DATE)));
                    event.StartTime = getTime(cursor.getString(cursor.getColumnIndex(DataBaseContract.RecurringEvents.COLUMN_NAME_START_TIME)));
                    event.EndTime = getTime(cursor.getString(cursor.getColumnIndex(DataBaseContract.RecurringEvents.COLUMN_NAME_END_TIME)));
                    event.WeekDay = cursor.getInt(cursor.getColumnIndex(DataBaseContract.RecurringEvents.COLUMN_NAME_WEEKDAY));
                    event.IsElective = cursor.getString(cursor.getColumnIndex(DataBaseContract.RecurringEvents.COLUMN_NAME_ELECTIVE)).charAt(0);
                    event.Program = cursor.getString(cursor.getColumnIndex(DataBaseContract.RecurringEvents.COLUMN_NAME_PROGRAM));

                    result.add(event);
                    // move to next row
                } while (cursor.moveToNext());
            }
        }
        onCallBack.onSuccess(result);
    }

    public static void putStudentSchedule(List<RecurringStudentCalendarEvent> data) {
        //Clear the Events table
        writableDB.delete(DataBaseContract.RecurringEvents.TABLE_NAME, null, null);

        //Insert the events
        for (RecurringStudentCalendarEvent event : data) {
            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(DataBaseContract.RecurringEvents.COLUMN_NAME_ABBR, event.Abbreviation);
            values.put(DataBaseContract.RecurringEvents.COLUMN_NAME_NAME, event.Name);
            values.put(DataBaseContract.RecurringEvents.COLUMN_NAME_DESC, event.Description);
            values.put(DataBaseContract.RecurringEvents.COLUMN_NAME_LOCATION, event.Location);
            values.put(DataBaseContract.RecurringEvents.COLUMN_NAME_TYPE_CODE, event.TypeCode);
            values.put(DataBaseContract.RecurringEvents.COLUMN_NAME_TYPE_NAME, event.TypeName);
            values.put(DataBaseContract.RecurringEvents.COLUMN_NAME_START_DATE, dateFormat.format(event.StartDate));
            values.put(DataBaseContract.RecurringEvents.COLUMN_NAME_END_DATE, dateFormat.format(event.EndDate));
            values.put(DataBaseContract.RecurringEvents.COLUMN_NAME_START_TIME, timeFormat.format(event.StartTime));
            values.put(DataBaseContract.RecurringEvents.COLUMN_NAME_END_TIME, timeFormat.format(event.EndTime));
            values.put(DataBaseContract.RecurringEvents.COLUMN_NAME_WEEKDAY, event.WeekDay);
            values.put(DataBaseContract.RecurringEvents.COLUMN_NAME_ELECTIVE, event.IsElective.toString());
            values.put(DataBaseContract.RecurringEvents.COLUMN_NAME_LECTURER, event.Lecturer);

            // Insert the new row, returning the primary key value of the new row
            long newRowId;
            newRowId = writableDB.insert(
                    DataBaseContract.RecurringEvents.TABLE_NAME,
                    //TODO verify second parameter
                    null,
                    values);
        }
    }

    public static void getStudentSchedule(final CallBack<List<RecurringStudentCalendarEvent>> onCallBack){
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                DataBaseContract.RecurringEvents._ID,
                DataBaseContract.RecurringEvents.COLUMN_NAME_ABBR,
                DataBaseContract.RecurringEvents.COLUMN_NAME_NAME,
                DataBaseContract.RecurringEvents.COLUMN_NAME_DESC,
                DataBaseContract.RecurringEvents.COLUMN_NAME_LOCATION,
                DataBaseContract.RecurringEvents.COLUMN_NAME_TYPE_CODE,
                DataBaseContract.RecurringEvents.COLUMN_NAME_TYPE_NAME,
                DataBaseContract.RecurringEvents.COLUMN_NAME_START_DATE,
                DataBaseContract.RecurringEvents.COLUMN_NAME_END_DATE,
                DataBaseContract.RecurringEvents.COLUMN_NAME_START_TIME,
                DataBaseContract.RecurringEvents.COLUMN_NAME_END_TIME,
                DataBaseContract.RecurringEvents.COLUMN_NAME_WEEKDAY,
                DataBaseContract.RecurringEvents.COLUMN_NAME_ELECTIVE,
                DataBaseContract.RecurringEvents.COLUMN_NAME_LECTURER
        };


        // How you want the results sorted in the resulting Cursor
        // String sortOrder = FeedEntry.COLUMN_NAME_UPDATED + " DESC";

        Cursor cursor = readableDB.query(
                DataBaseContract.RecurringEvents.TABLE_NAME,         // The table to query
                projection,                                 // The columns to return
                null,                                       // The columns for the WHERE clause
                null,                                       // The values for the WHERE clause
                null,                                       // don't group the rows
                null,                                       // don't filter by row groups
                null                                         // The sort order
        );

        List<RecurringStudentCalendarEvent> result = new ArrayList<>();

        if (cursor != null) {
            // move cursor to first row
            if (cursor.moveToFirst()) {
                do {
                    RecurringStudentCalendarEvent event = new RecurringStudentCalendarEvent();
                    event.Abbreviation = cursor.getString(cursor.getColumnIndex(DataBaseContract.RecurringEvents.COLUMN_NAME_ABBR));
                    event.Name = cursor.getString(cursor.getColumnIndex(DataBaseContract.RecurringEvents.COLUMN_NAME_NAME));
                    event.Description = cursor.getString(cursor.getColumnIndex(DataBaseContract.RecurringEvents.COLUMN_NAME_DESC));
                    event.Location = cursor.getString(cursor.getColumnIndex(DataBaseContract.RecurringEvents.COLUMN_NAME_LOCATION));
                    event.TypeCode = cursor.getString(cursor.getColumnIndex(DataBaseContract.RecurringEvents.COLUMN_NAME_TYPE_CODE));
                    event.TypeName = cursor.getString(cursor.getColumnIndex(DataBaseContract.RecurringEvents.COLUMN_NAME_TYPE_NAME));
                    event.StartDate = getDate(cursor.getString(cursor.getColumnIndex(DataBaseContract.RecurringEvents.COLUMN_NAME_START_DATE)));
                    event.EndDate = getDate(cursor.getString(cursor.getColumnIndex(DataBaseContract.RecurringEvents.COLUMN_NAME_END_DATE)));
                    event.StartTime = getTime(cursor.getString(cursor.getColumnIndex(DataBaseContract.RecurringEvents.COLUMN_NAME_START_TIME)));
                    event.EndTime = getTime(cursor.getString(cursor.getColumnIndex(DataBaseContract.RecurringEvents.COLUMN_NAME_END_TIME)));
                    event.WeekDay = cursor.getInt(cursor.getColumnIndex(DataBaseContract.RecurringEvents.COLUMN_NAME_WEEKDAY));
                    event.IsElective = cursor.getString(cursor.getColumnIndex(DataBaseContract.RecurringEvents.COLUMN_NAME_ELECTIVE)).charAt(0);
                    event.Lecturer = cursor.getString(cursor.getColumnIndex(DataBaseContract.RecurringEvents.COLUMN_NAME_LECTURER));

                    result.add(event);
                    // move to next row
                } while (cursor.moveToNext());
            }
        }
        onCallBack.onSuccess(result);
    }

    private static Date getDate(String dateSting) {
        try {
            return new SimpleDateFormat(DATE_FORMAT_SHORT, Locale.US).parse(dateSting);
        } catch (ParseException e) {
            return null;
        }
    }

    private static Time getTime(String timeString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT, Locale.US);
            long ms = sdf.parse(timeString).getTime();
            Time t = new Time(ms);
            return t;
        } catch (ParseException e) {
            return null;
        }
    }

}
