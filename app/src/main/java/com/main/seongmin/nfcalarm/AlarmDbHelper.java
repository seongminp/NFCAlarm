package com.main.seongmin.nfcalarm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.main.seongmin.nfcalarm.AlarmContract.AlarmEntry;

/**
 * Created by seongmin on 12/12/16.
 */

public class AlarmDbHelper extends SQLiteOpenHelper {

    private static final String TYPE_TEXT = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " +
                    AlarmEntry.TABLE_NAME + " (" + AlarmEntry._ID + " INTEGER PRIMARY KEY," +
                    AlarmEntry.COLUMN_NAME_TIME + TYPE_TEXT + COMMA_SEP +
                    AlarmEntry.COLUMN_NAME_NFC + TYPE_TEXT + " )";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + AlarmEntry.TABLE_NAME;


    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "NFCAlarm.db";

    public AlarmDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        System.out.println(SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
