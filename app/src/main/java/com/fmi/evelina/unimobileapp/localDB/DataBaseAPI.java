package com.fmi.evelina.unimobileapp.localDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.fmi.evelina.unimobileapp.controller.ApplicationController;
import com.fmi.evelina.unimobileapp.model.calendar_events_model.CalendarEvent;
import com.fmi.evelina.unimobileapp.model.calendar_events_model.RecurringLecturerCalendarEvent;
import com.fmi.evelina.unimobileapp.model.calendar_events_model.RecurringStudentCalendarEvent;
import com.fmi.evelina.unimobileapp.model.contacts_model.AdministrationCategoryContacts;
import com.fmi.evelina.unimobileapp.model.contacts_model.AdministrationContactData;
import com.fmi.evelina.unimobileapp.model.contacts_model.ContactData;
import com.fmi.evelina.unimobileapp.model.contacts_model.LecturerContact;
import com.fmi.evelina.unimobileapp.model.student_plan_model.StudentPlan;
import com.fmi.evelina.unimobileapp.network.ICallBack;
import com.google.gson.Gson;

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

    public static void getEvents(final ICallBack<List<CalendarEvent>> onCallBack) {
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

    public static void getLecturerSchedule(final ICallBack<List<RecurringLecturerCalendarEvent>> onCallBack) {
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

    public static void getStudentSchedule(final ICallBack<List<RecurringStudentCalendarEvent>> onCallBack) {
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

    public static void putAdministrationContacts(List<AdministrationCategoryContacts> data) {
        //Clear the Administration Contacts table
        writableDB.delete(DataBaseContract.AdministrationContacts.TABLE_NAME, null, null);

        //Insert the contacts
        for (AdministrationCategoryContacts category : data) {
            for (AdministrationContactData contact : category.Contacts) {
                // Create a new map of values, where column names are the keys
                ContentValues values = new ContentValues();
                values.put(DataBaseContract.AdministrationContacts.COLUMN_NAME_CATEGORY, category.Category);
                values.put(DataBaseContract.AdministrationContacts.COLUMN_NAME_NAME, contact.Name);
                values.put(DataBaseContract.AdministrationContacts.COLUMN_NAME_JOB, contact.Job);
                values.put(DataBaseContract.AdministrationContacts.COLUMN_NAME_PHONE, contact.ContactInfo.Phone);
                values.put(DataBaseContract.AdministrationContacts.COLUMN_NAME_EMAIL, contact.ContactInfo.Email);
                values.put(DataBaseContract.AdministrationContacts.COLUMN_NAME_ROOM, contact.ContactInfo.Room);
                values.put(DataBaseContract.AdministrationContacts.COLUMN_NAME_TIME, contact.ContactInfo.Time);

                // Insert the new row, returning the primary key value of the new row
                long newRowId;
                newRowId = writableDB.insert(
                        DataBaseContract.AdministrationContacts.TABLE_NAME,
                        null,
                        values);
            }
        }
    }

    public static void getAdministrationContacts(final ICallBack<List<AdministrationCategoryContacts>> onCallBack) {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.

        String[] projection = {
                DataBaseContract.AdministrationContacts._ID,
                DataBaseContract.AdministrationContacts.COLUMN_NAME_CATEGORY,
                DataBaseContract.AdministrationContacts.COLUMN_NAME_NAME,
                DataBaseContract.AdministrationContacts.COLUMN_NAME_JOB,
                DataBaseContract.AdministrationContacts.COLUMN_NAME_PHONE,
                DataBaseContract.AdministrationContacts.COLUMN_NAME_EMAIL,
                DataBaseContract.AdministrationContacts.COLUMN_NAME_ROOM,
                DataBaseContract.AdministrationContacts.COLUMN_NAME_TIME
        };

        Cursor cursor = readableDB.query(
                DataBaseContract.AdministrationContacts.TABLE_NAME,         // The table to query
                projection,                                 // The columns to return
                null,                                       // The columns for the WHERE clause
                null,                                       // The values for the WHERE clause
                null,                                       // don't group the rows
                null,                                       // don't filter by row groups
                null                                         // The sort order
        );

        List<AdministrationCategoryContacts> result = new ArrayList<>();

        if (cursor != null) {
            // move cursor to first row
            if (cursor.moveToFirst()) {
                do {
                    AdministrationContactData contactData = new AdministrationContactData();
                    String category;

                    category = cursor.getString(cursor.getColumnIndex(DataBaseContract.AdministrationContacts.COLUMN_NAME_CATEGORY));
                    contactData.Name = cursor.getString(cursor.getColumnIndex(DataBaseContract.AdministrationContacts.COLUMN_NAME_NAME));
                    contactData.Job = cursor.getString(cursor.getColumnIndex(DataBaseContract.AdministrationContacts.COLUMN_NAME_JOB));

                    contactData.ContactInfo = new ContactData();
                    contactData.ContactInfo.Phone = cursor.getString(cursor.getColumnIndex(DataBaseContract.AdministrationContacts.COLUMN_NAME_PHONE));
                    contactData.ContactInfo.Email = cursor.getString(cursor.getColumnIndex(DataBaseContract.AdministrationContacts.COLUMN_NAME_EMAIL));
                    contactData.ContactInfo.Room = cursor.getString(cursor.getColumnIndex(DataBaseContract.AdministrationContacts.COLUMN_NAME_ROOM));
                    contactData.ContactInfo.Time = cursor.getString(cursor.getColumnIndex(DataBaseContract.AdministrationContacts.COLUMN_NAME_TIME));

                    AdministrationCategoryContacts categoryContacts = null;

                    for (AdministrationCategoryContacts cat : result) {
                        if (cat.Category.equals(category)) {
                            categoryContacts = cat;
                            break;
                        }
                    }

                    if (categoryContacts == null) {
                        categoryContacts = new AdministrationCategoryContacts();
                        categoryContacts.Category = category;
                        categoryContacts.Contacts = new ArrayList<>();

                        result.add(categoryContacts);
                    }

                    categoryContacts.Contacts.add(contactData);

                    // move to next row
                } while (cursor.moveToNext());
            }
        }
        onCallBack.onSuccess(result);
    }

    public static void putLecturersContacts(List<LecturerContact> data) {
        //Clear the Lecturer Contacts table
        writableDB.delete(DataBaseContract.LecturerContacts.TABLE_NAME, null, null);

        //Insert the contacts
        for (LecturerContact contact : data) {
            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(DataBaseContract.LecturerContacts.COLUMN_NAME_NAME, contact.Name);
            values.put(DataBaseContract.LecturerContacts.COLUMN_NAME_DEPARTMENT, contact.Department);
            values.put(DataBaseContract.LecturerContacts.COLUMN_NAME_PHONE, contact.ContactData.Phone);
            values.put(DataBaseContract.LecturerContacts.COLUMN_NAME_EMAIL, contact.ContactData.Email);
            values.put(DataBaseContract.LecturerContacts.COLUMN_NAME_ROOM, contact.ContactData.Room);
            values.put(DataBaseContract.LecturerContacts.COLUMN_NAME_TIME, contact.ContactData.Time);

            // Insert the new row, returning the primary key value of the new row
            long newRowId;
            newRowId = writableDB.insert(
                    DataBaseContract.LecturerContacts.TABLE_NAME,
                    null,
                    values);

        }
    }

    public static void getLecturersContacts(final ICallBack<List<LecturerContact>> onCallBack) {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.

        String[] projection = {
                DataBaseContract.LecturerContacts._ID,
                DataBaseContract.LecturerContacts.COLUMN_NAME_NAME,
                DataBaseContract.LecturerContacts.COLUMN_NAME_DEPARTMENT,
                DataBaseContract.LecturerContacts.COLUMN_NAME_PHONE,
                DataBaseContract.LecturerContacts.COLUMN_NAME_EMAIL,
                DataBaseContract.LecturerContacts.COLUMN_NAME_ROOM,
                DataBaseContract.LecturerContacts.COLUMN_NAME_TIME
        };

        Cursor cursor = readableDB.query(
                DataBaseContract.LecturerContacts.TABLE_NAME,         // The table to query
                projection,                                 // The columns to return
                null,                                       // The columns for the WHERE clause
                null,                                       // The values for the WHERE clause
                null,                                       // don't group the rows
                null,                                       // don't filter by row groups
                null                                         // The sort order
        );

        List<LecturerContact> result = new ArrayList<>();

        if (cursor != null) {
            // move cursor to first row
            if (cursor.moveToFirst()) {
                do {
                    LecturerContact contact = new LecturerContact();

                    contact.Name = cursor.getString(cursor.getColumnIndex(DataBaseContract.LecturerContacts.COLUMN_NAME_NAME));
                    contact.Department = cursor.getString(cursor.getColumnIndex(DataBaseContract.LecturerContacts.COLUMN_NAME_DEPARTMENT));

                    contact.ContactData = new ContactData();
                    contact.ContactData.Phone = cursor.getString(cursor.getColumnIndex(DataBaseContract.LecturerContacts.COLUMN_NAME_PHONE));
                    contact.ContactData.Email = cursor.getString(cursor.getColumnIndex(DataBaseContract.LecturerContacts.COLUMN_NAME_EMAIL));
                    contact.ContactData.Room = cursor.getString(cursor.getColumnIndex(DataBaseContract.LecturerContacts.COLUMN_NAME_ROOM));
                    contact.ContactData.Time = cursor.getString(cursor.getColumnIndex(DataBaseContract.LecturerContacts.COLUMN_NAME_TIME));

                    result.add(contact);

                    // move to next row
                } while (cursor.moveToNext());
            }
        }
        onCallBack.onSuccess(result);
    }

    public static void putStudentPlan(StudentPlan data) {
        //Clear the Student Plan table
        writableDB.delete(DataBaseContract.StudentPlan.TABLE_NAME, null, null);

        Gson gson = ApplicationController.getGson();
        String plan = gson.toJson(data, StudentPlan.class);

        //Insert the student plan
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DataBaseContract.StudentPlan.COLUMN_NAME_PLAN, plan);

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = writableDB.insert(
                DataBaseContract.StudentPlan.TABLE_NAME,
                null,
                values);

    }

    public static void getStudentPlan(final ICallBack<StudentPlan> onCallBack) {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                DataBaseContract.StudentPlan._ID,
                DataBaseContract.StudentPlan.COLUMN_NAME_PLAN
        };

        // How you want the results sorted in the resulting Cursor
        // String sortOrder = FeedEntry.COLUMN_NAME_UPDATED + " DESC";

        Cursor cursor = readableDB.query(
                DataBaseContract.StudentPlan.TABLE_NAME,         // The table to query
                projection,                                 // The columns to return
                null,                                       // The columns for the WHERE clause
                null,                                       // The values for the WHERE clause
                null,                                       // don't group the rows
                null,                                       // don't filter by row groups
                null                                         // The sort order
        );

        if (cursor != null) {
            // move cursor to first row
            if (cursor.moveToFirst()) {
                String data  = cursor.getString(cursor.getColumnIndex(DataBaseContract.StudentPlan.COLUMN_NAME_PLAN));

                Gson gson = ApplicationController.getGson();
                StudentPlan studentPlan = gson.fromJson(data, StudentPlan.class);

                onCallBack.onSuccess(studentPlan);
                return;
            }
        }
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
