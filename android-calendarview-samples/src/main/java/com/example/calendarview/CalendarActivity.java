package com.example.calendarview;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.tripadvisor.CalendarView;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.app.ActionBar.NAVIGATION_MODE_TABS;
import static android.app.ActionBar.TabListener;
import static com.tripadvisor.CalendarView.DateSelectableFilter;
import static com.tripadvisor.CalendarView.OnInvalidDateSelectedListener;
import static com.tripadvisor.CalendarView.SelectionMode.RANGE;
import static com.tripadvisor.CalendarView.SelectionMode.SINGLE;
import static java.util.Calendar.DECEMBER;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MARCH;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.SECOND;


public class CalendarActivity extends Activity implements TabListener, DateSelectableFilter, OnInvalidDateSelectedListener {

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
        final Calendar dec2012 = buildCal(2013, DECEMBER, 1);
        final Calendar dec2013 = buildCal(2020, DECEMBER, 1);
        Calendar march2013 = buildCal(2014, MARCH, 1);
        mCalendarView.init(dec2012.getTime(), dec2013.getTime(), locale)
                .inMode(SINGLE).withSelectedDate(march2013.getTime());
        setMidnight(today);
        final ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setNavigationMode(NAVIGATION_MODE_TABS);
            ActionBar.Tab oneWayTab = actionBar.newTab();
            ActionBar.Tab roundTripTab = actionBar.newTab();
            oneWayTab.setText("Select Day");
            roundTripTab.setText("Select Range");
            oneWayTab.setTabListener(this);
            roundTripTab.setTabListener(this);
            actionBar.addTab(oneWayTab);
            actionBar.addTab(roundTripTab);
        }
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction
            fragmentTransaction) {
        if (tab.getPosition() == 0) {
            mCalendarView.setSelectionMode(SINGLE);
        } else if (tab.getPosition() == 1) {
            mCalendarView.setSelectionMode(RANGE);
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction
            fragmentTransaction) {
        Log.d("CalendarActivity", "Tab Unselected:"+tab.getText());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

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
