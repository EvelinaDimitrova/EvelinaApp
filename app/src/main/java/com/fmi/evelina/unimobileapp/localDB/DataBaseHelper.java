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
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "UniMobileApp.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String NUMB_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        Log.v("EVE_TRACE", "DataBaseHelper.onCreate");
        for (String sql : getSqlCreateEntries()) {
            Log.v("EVE_TRACE", sql);
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

        result.add(createEventTableSQL);
        result.add(createRecurringEventsTableSQL);

        return result;
    }

    private List<String> getSqlDeleteEntries() {
        List<String> result = new ArrayList<>();

        String deleteEventsTableSQL = "DROP TABLE IF EXISTS " + DataBaseContract.Events.TABLE_NAME;
        String deleteRecurrinEventsTableSQL = "DROP TABLE IF EXISTS " + DataBaseContract.RecurringEvents.TABLE_NAME;

        result.add(deleteEventsTableSQL);
        result.add(deleteRecurrinEventsTableSQL);

        return result;
    }
}