package com.netizenbd.netichecker.sqlitedatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import com.netizenbd.netichecker.ParticipantList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Md. Touhidul Islam on 11/20/2016.
 */

public class DataService {

    private SQLiteDatabase sqLiteDatabase;
    private MyDbHelper myDbHelper;

    private long insert;

    private Context context;

    public DataService(Context context) {
        this.context = context;
    }

    public boolean insertData(Context context, DataEntity dataEntity) {
        try {
            myDbHelper = new MyDbHelper(context);
            // System date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();

            // Open db as writable mode
            sqLiteDatabase = myDbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(MyDbHelper.COLUMN_DATA_EVENT_ID, dataEntity.getEventID());
            values.put(MyDbHelper.COLUMN_DATA_EVENT_NAME, dataEntity.getEventName());
            values.put(MyDbHelper.COLUMN_DATA_PARTICIPANT_ID, dataEntity.getParticipateID());
            values.put(MyDbHelper.COLUMN_DATA_PARTICIPANT_NAME, dataEntity.getName());
            values.put(MyDbHelper.COLUMN_DATA_PARTICIPANT_PHONE, dataEntity.getPhone());
            values.put(MyDbHelper.COLUMN_DATA_PARTICIPANT_AREA, dataEntity.getArea());
            values.put(MyDbHelper.COLUMN_DATA_DATE_TIME, dateFormat.format(date)); // System date

            // Inserting Row
            Log.d("touhidd", "insertData 1: " + dateFormat.format(date));

            insert = sqLiteDatabase.insertOrThrow(MyDbHelper.TABLE_DATA, null, values);

            sqLiteDatabase.close();
            Log.d("touhidd", "insertData 2: " + dateFormat.format(date));
        } catch (SQLException sqlException) {
            Toast.makeText(context, "Already Done! -S", Toast.LENGTH_SHORT).show();
        }

        if (insert == -1 || insert == 0) { // -1 means something wrong and 0 means no row effected
            return false;
        } else {
            return true;
        }
    }

    public List<DataEntity> getDataEntityList(Context context) {

        List<DataEntity> dataEntityList = new ArrayList<DataEntity>();

        myDbHelper = new MyDbHelper(context);
        Log.d("touhidd", "dataEntityList: 2");
        // Open db as readable mode
        sqLiteDatabase = myDbHelper.getWritableDatabase();
        Log.d("touhidd", "dataEntityList: 11");


        // Select All Query
        String selectQuery = "SELECT * FROM " + MyDbHelper.TABLE_DATA + " ORDER BY " + MyDbHelper.COLUMN_ID + " DESC";
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        Log.d("touhidd", "getDataEntityList: 222");

        // Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            Date convertedDate = null;
            try {
                convertedDate = new SimpleDateFormat().parse(cursor.getString(7));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            do {
                dataEntityList.add(new DataEntity(
                        cursor.getString(0), // Column id
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        convertedDate
                ));

            } while (cursor.moveToNext());
        }

        // Closing connection
        cursor.close();
        sqLiteDatabase.close();

        // Returning labels
        return dataEntityList;
    }




}
