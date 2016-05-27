package com.fmi.evelina.unimobileapp.localDB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "UniMobileApp.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String NUMB_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        for (String sql : getSqlCreateEntries()) {
            db.execSQL(sql);
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        for (String sql : getSqlDeleteEntries()) {
            db.execSQL(sql);
        }
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    private List<String> getSqlCreateEntries() {
        List<String> result = new ArrayList<>();

        String createEventTableSQL =
                "CREATE TABLE " + DataBaseContract.Events.TABLE_NAME + " (" +
                        DataBaseContract.Events._ID + " INTEGER PRIMARY KEY," +
                        DataBaseContract.Events.COLUMN_NAME_ABBR + TEXT_TYPE + COMMA_SEP +
                        DataBaseContract.Events.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                        DataBaseContract.Events.COLUMN_NAME_DESC + TEXT_TYPE + COMMA_SEP +
                        DataBaseContract.Events.COLUMN_NAME_LOCATION + TEXT_TYPE + COMMA_SEP +
                        DataBaseContract.Events.COLUMN_NAME_TYPE_CODE + TEXT_TYPE + COMMA_SEP +
                        DataBaseContract.Events.COLUMN_NAME_DATE + TEXT_TYPE + COMMA_SEP +
                        DataBaseContract.Events.COLUMN_NAME_START_TIME + TEXT_TYPE + COMMA_SEP +
                        DataBaseContract.Events.COLUMN_NAME_END_TIME + TEXT_TYPE + " )";

        String createRecurringEventsTableSQL = "CREATE TABLE " + DataBaseContract.RecurringEvents.TABLE_NAME + " (" +
                DataBaseContract.Events._ID + " INTEGER PRIMARY KEY," +
                DataBaseContract.RecurringEvents.COLUMN_NAME_ABBR + TEXT_TYPE + COMMA_SEP +
                DataBaseContract.RecurringEvents.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                DataBaseContract.RecurringEvents.COLUMN_NAME_DESC + TEXT_TYPE + COMMA_SEP +
                DataBaseContract.RecurringEvents.COLUMN_NAME_LOCATION + TEXT_TYPE + COMMA_SEP +
                DataBaseContract.RecurringEvents.COLUMN_NAME_TYPE_NAME + TEXT_TYPE + COMMA_SEP +
                DataBaseContract.RecurringEvents.COLUMN_NAME_TYPE_CODE + TEXT_TYPE + COMMA_SEP +
                DataBaseContract.RecurringEvents.COLUMN_NAME_START_DATE + TEXT_TYPE + COMMA_SEP +
                DataBaseContract.RecurringEvents.COLUMN_NAME_START_TIME + TEXT_TYPE + COMMA_SEP +
                DataBaseContract.RecurringEvents.COLUMN_NAME_END_DATE + TEXT_TYPE + COMMA_SEP +
                DataBaseContract.RecurringEvents.COLUMN_NAME_END_TIME + TEXT_TYPE + COMMA_SEP +
                DataBaseContract.RecurringEvents.COLUMN_NAME_WEEKDAY + NUMB_TYPE + COMMA_SEP +
                DataBaseContract.RecurringEvents.COLUMN_NAME_ELECTIVE + TEXT_TYPE + COMMA_SEP +
                DataBaseContract.RecurringEvents.COLUMN_NAME_PROGRAM + TEXT_TYPE + COMMA_SEP +
                DataBaseContract.RecurringEvents.COLUMN_NAME_LECTURER + TEXT_TYPE + " )";


        String createAdminContactsTableSQL =
                "CREATE TABLE " + DataBaseContract.AdministrationContacts.TABLE_NAME + " (" +
                        DataBaseContract.AdministrationContacts._ID + " INTEGER PRIMARY KEY," +
                        DataBaseContract.AdministrationContacts.COLUMN_NAME_CATEGORY + TEXT_TYPE + COMMA_SEP +
                        DataBaseContract.AdministrationContacts.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                        DataBaseContract.AdministrationContacts.COLUMN_NAME_JOB + TEXT_TYPE + COMMA_SEP +
                        DataBaseContract.AdministrationContacts.COLUMN_NAME_PHONE + TEXT_TYPE + COMMA_SEP +
                        DataBaseContract.AdministrationContacts.COLUMN_NAME_EMAIL + TEXT_TYPE + COMMA_SEP +
                        DataBaseContract.AdministrationContacts.COLUMN_NAME_ROOM + TEXT_TYPE + COMMA_SEP +
                        DataBaseContract.AdministrationContacts.COLUMN_NAME_TIME + TEXT_TYPE + " )";

        String createLectContactsTableSQL =
                "CREATE TABLE " + DataBaseContract.LecturerContacts.TABLE_NAME + " (" +
                        DataBaseContract.LecturerContacts._ID + " INTEGER PRIMARY KEY," +
                        DataBaseContract.LecturerContacts.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                        DataBaseContract.LecturerContacts.COLUMN_NAME_DEPARTMENT + TEXT_TYPE + COMMA_SEP +
                        DataBaseContract.LecturerContacts.COLUMN_NAME_PHONE + TEXT_TYPE + COMMA_SEP +
                        DataBaseContract.LecturerContacts.COLUMN_NAME_EMAIL + TEXT_TYPE + COMMA_SEP +
                        DataBaseContract.LecturerContacts.COLUMN_NAME_ROOM + TEXT_TYPE + COMMA_SEP +
                        DataBaseContract.LecturerContacts.COLUMN_NAME_TIME + TEXT_TYPE + " )";

        String createStudentPlanTableSQL =
                "CREATE TABLE " + DataBaseContract.StudentPlan.TABLE_NAME + " (" +
                        DataBaseContract.StudentPlan._ID + " INTEGER PRIMARY KEY," +
                        DataBaseContract.StudentPlan.COLUMN_NAME_PLAN + TEXT_TYPE + " )";

        result.add(createEventTableSQL);
        result.add(createRecurringEventsTableSQL);
        result.add(createAdminContactsTableSQL);
        result.add(createLectContactsTableSQL);
        result.add(createStudentPlanTableSQL);

        return result;
    }

    private List<String> getSqlDeleteEntries() {
        List<String> result = new ArrayList<>();

        String deleteEventsTableSQL = "DROP TABLE IF EXISTS " + DataBaseContract.Events.TABLE_NAME;
        String deleteRecurringEventsTableSQL = "DROP TABLE IF EXISTS " + DataBaseContract.RecurringEvents.TABLE_NAME;
        String deleteAdminContactsTableSQL = "DROP TABLE IF EXISTS " + DataBaseContract.AdministrationContacts.TABLE_NAME;
        String deleteLectContactsTableSQL = "DROP TABLE IF EXISTS " + DataBaseContract.LecturerContacts.TABLE_NAME;
        String deleteStudentPlanTableSQL = "DROP TABLE IF EXISTS " + DataBaseContract.StudentPlan.TABLE_NAME;

        result.add(deleteEventsTableSQL);
        result.add(deleteRecurringEventsTableSQL);
        result.add(deleteAdminContactsTableSQL);
        result.add(deleteLectContactsTableSQL);
        result.add(deleteStudentPlanTableSQL);

        return result;
    }
}