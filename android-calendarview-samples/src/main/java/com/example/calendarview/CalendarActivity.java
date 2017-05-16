package com.example.calendarview;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.oyorooms.CalendarView;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.tripadvisor.CalendarView.DateSelectableFilter;
import static com.tripadvisor.CalendarView.OnInvalidDateSelectedListener;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.SECOND;


public class CalendarActivity extends Activity implements DateSelectableFilter, OnInvalidDateSelectedListener {

    private CalendarView mCalendarView;
    private Calendar today = Calendar.getInstance();
    private Locale locale;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mCalendarView = (CalendarView) findViewById(R.id.calendar_view);
        locale = Locale.US;
        today = Calendar.getInstance(locale);
//        final Calendar dec2016 = buildCal(2016, DECEMBER, 1);
//        final Calendar dec2017 = buildCal(2017, DECEMBER, 1);
//        Calendar may2017 = buildCal(2017, MAY, 1);
//        mCalendarView.init(dec2016.getTime(), dec2017.getTime(), locale)
//                .inMode(RANGE).withSelectedDate(may2017.getTime());
//        setMidnight(today);
    }

    /**
     * Clears out the hours/minutes/seconds/millis of a Calendar.
     */
    private static void setMidnight(Calendar cal) {
        cal.set(HOUR_OF_DAY, 0);
        cal.set(MINUTE, 0);
        cal.set(SECOND, 0);
        cal.set(MILLISECOND, 0);
    }

    @Override
    public boolean isDateSelectable(Date date) {
        boolean selectable = false;
        if (today.getTime().compareTo(date) <= 0) {
            selectable = true;
        }
        return selectable;
    }

    @Override
    public void onInvalidDateSelected(Date date) {
        Log.d("CalendarActivity", "onInvalidDateSelected:" + date.toString());
    }

    private Calendar buildCal(int year, int month, int day) {
        Calendar jumpToCal = Calendar.getInstance(locale);
        jumpToCal.set(year, month, day);
        return jumpToCal;
    }
}
