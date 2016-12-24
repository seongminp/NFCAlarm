package com.main.seongmin.nfcalarm;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TimePicker;

/**
 * Created by seongmin on 12/22/16.
 */
public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabPagerAdapter pagerAdapter;

    public static AlarmDbHelper alarmDbHelper;
    public static AlarmCursorAdapter alarmAdapter;
    public static AlarmReceiver alarmReceiver;

    private FloatingActionButton addButton;

    private Animation toX, toPlus;
    private boolean fabOpen;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        // Tabs setup.
        pagerAdapter = new TabPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(pagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        // Alarms setup.
        alarmDbHelper = new AlarmDbHelper(getApplicationContext());
        alarmAdapter = new AlarmCursorAdapter(this, alarmDbHelper.loadAlarms());
        alarmReceiver = new AlarmReceiver();

        // Configure button.
        addButton = (FloatingActionButton) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFab();
                //DialogFragment timePickerFragment = new TimePickerFragment();
                //timePickerFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });

        // Animation setup.
        fabOpen = false;
        toX = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.tox);
        toPlus = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.toplus);
    }

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hour, int minute) {
            int period = hour < 12 ? 0 : 1;
            int alarmId = alarmDbHelper.saveAlarm(hour, minute, period, "dkjncksj");
            if (alarmId != -1) { alarmReceiver.setAlarm(getContext(), alarmId, hour, minute); }
            alarmAdapter.refreshAlarmList(alarmDbHelper.loadAlarms());
        }
    }

    private void animateFab() {
        if (fabOpen) {
            addButton.startAnimation(toPlus);
        } else {
            addButton.startAnimation(toX);
        }
        fabOpen = !fabOpen;
    }
}
