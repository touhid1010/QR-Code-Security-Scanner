package com.netizenbd.netichecker.sqlitedatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Md. Touhidul Islam on 11/20/2016.
 */

public class MyDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "data.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_DATA = "my_data";

    public static final String COLUMN_ID = "id",
            COLUMN_DATA_EVENT_ID = "event_id",
            COLUMN_DATA_EVENT_NAME = "event_name",
            COLUMN_DATA_PARTICIPANT_ID = "participant_id",
            COLUMN_DATA_PARTICIPANT_NAME = "name",
            COLUMN_DATA_PARTICIPANT_PHONE = "phone",
            COLUMN_DATA_PARTICIPANT_AREA = "area",
            COLUMN_DATA_DATE_TIME = "date_time";

    private static final String SQL_CREATE_DATA_TABLE = "CREATE TABLE " + TABLE_DATA + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_DATA_EVENT_ID + " TEXT, " +
            COLUMN_DATA_EVENT_NAME + " TEXT, " +
            COLUMN_DATA_PARTICIPANT_ID + " TEXT, " +
            COLUMN_DATA_PARTICIPANT_NAME + " TEXT, " +
            COLUMN_DATA_PARTICIPANT_PHONE + " TEXT, " +
            COLUMN_DATA_PARTICIPANT_AREA + " TEXT, " +
            COLUMN_DATA_DATE_TIME + " TEXT, " +
            "CONSTRAINT uniqueconstraint " +
            "UNIQUE (" + COLUMN_DATA_EVENT_ID + ", " + COLUMN_DATA_PARTICIPANT_ID + ") ON CONFLICT ROLLBACK)";

    public MyDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create tables
        sqLiteDatabase.execSQL(SQL_CREATE_DATA_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_DATA);
    }


}
