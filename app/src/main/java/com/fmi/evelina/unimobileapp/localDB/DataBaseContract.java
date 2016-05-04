package com.fmi.evelina.unimobileapp.localDB;

import android.provider.BaseColumns;

/**
 *
 */
public final class DataBaseContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public DataBaseContract() {}

    /* Inner class that defines the table contents */
    public static abstract class Events implements BaseColumns {
        public static final String TABLE_NAME = "event";
        public static final String COLUMN_NAME_ABBR = "abbreviation";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_DESC = "description";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_START_TIME = "startTime";
        public static final String COLUMN_NAME_END_TIME = "endTime";
        public static final String COLUMN_NAME_LOCATION = "location";
        public static final String COLUMN_NAME_TYPE_CODE = "typeCode";
    }

    /* Inner class that defines the table contents */
    public static abstract class RecurringEvents implements BaseColumns {
        public static final String TABLE_NAME = "recurringEvent";
        public static final String COLUMN_NAME_ABBR = "abbreviation";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_DESC = "description";
        public static final String COLUMN_NAME_START_DATE = "startDate";
        public static final String COLUMN_NAME_END_DATE = "endDate";
        public static final String COLUMN_NAME_START_TIME = "startTime";
        public static final String COLUMN_NAME_END_TIME = "endTime";
        public static final String COLUMN_NAME_LOCATION = "location";
        public static final String COLUMN_NAME_TYPE_CODE = "typeCode";
        public static final String COLUMN_NAME_TYPE_NAME = "typeName";
        public static final String COLUMN_NAME_WEEKDAY = "weekDay";
        public static final String COLUMN_NAME_ELECTIVE = "elective";
        public static final String COLUMN_NAME_PROGRAM = "program";
        public static final String COLUMN_NAME_LECTURER = "lecturer";
    }
}
