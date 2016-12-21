package com.main.seongmin.nfcalarm;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.main.seongmin.nfcalarm.AlarmContract.AlarmEntry;

public class MainActivity extends AppCompatActivity {
    private AlarmDbHelper alarmDbHelper;
    private ListView alarmListView;
    private Cursor alarmCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alarmDbHelper = new AlarmDbHelper(getApplicationContext());
        alarmListView = (ListView) findViewById(R.id.listView);

        saveAlarm("11:30 am", "nfc");
        saveAlarm("12:30 am", "nfc");
        saveAlarm("14:30 am", "nfc");

        alarmCursor = loadAlarms();

        final AlarmCursorAdapter alarmAdapter = new AlarmCursorAdapter(this, alarmCursor);
        alarmListView.setAdapter(alarmAdapter);
        alarmListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                alarmCursor.moveToPosition(pos);
                String alarmId = alarmCursor.getString(alarmCursor.getColumnIndexOrThrow(AlarmEntry._ID));
                deleteAlarm(alarmId);
                alarmCursor.close();
                alarmCursor = loadAlarms();
                alarmAdapter.swapCursor(alarmCursor);
            }
        });
    }


    private long saveAlarm(String time, String nfcId) {
        SQLiteDatabase db = alarmDbHelper.getWritableDatabase();

        ContentValues values =  new ContentValues();
        values.put(AlarmEntry.COLUMN_NAME_TIME, time);
        values.put(AlarmEntry.COLUMN_NAME_NFC, nfcId);

        long newAlarmId = db.insert(AlarmEntry.TABLE_NAME, null, values);

        return newAlarmId;
    }

    private Cursor loadAlarms() {
        SQLiteDatabase db = alarmDbHelper.getReadableDatabase();

        String[] projection = {
                AlarmEntry._ID,
                AlarmEntry.COLUMN_NAME_TIME,
                AlarmEntry.COLUMN_NAME_NFC
        };

        String sortOrder =  AlarmEntry.COLUMN_NAME_TIME + " DESC";

        Cursor cursor = db.query(
            AlarmEntry.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            sortOrder
            );


        return cursor;
    }

    private void deleteAlarm(String alarmId) {
        SQLiteDatabase db = alarmDbHelper.getReadableDatabase();

        String selection = AlarmEntry._ID + " LIKE ?";
        String[] selectionArgs = { alarmId };
        db.delete(AlarmEntry.TABLE_NAME, selection, selectionArgs);
    }

    private int updateAlarm(String alarmId, String time, String nfcId) {
        SQLiteDatabase db = alarmDbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(AlarmEntry.COLUMN_NAME_TIME, time);
        values.put(AlarmEntry.COLUMN_NAME_NFC, nfcId);

        String selection = AlarmEntry._ID + " LIKE ?";
        String[] selectionArgs = { alarmId };

        int count = db.update(
                AlarmEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );

        return count;
    }
}
