package com.example.calendarview.tests;

import android.content.Context;
import android.content.res.Configuration;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.calendarview.CalendarActivity;
import com.example.calendarview.R;
import com.tripadvisor.CalendarView;

import org.fest.assertions.api.Assertions;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isDisplayed;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;
import static com.tripadvisor.CalendarView.SelectionMode.SINGLE;
import static java.util.Calendar.MARCH;
import static java.util.Calendar.NOVEMBER;
import static org.fest.assertions.api.ANDROID.assertThat;

/**
 * Created by Kurry Tran
 * Email: ktran@tripadvisor.com
 * Date: 2/25/14
 */
public class CalendarInstrumentationTest extends
        ActivityInstrumentationTestCase2<CalendarActivity> {

    private Locale locale;
    private Calendar today;
    private Date maxDate;
    private Date minDate;

    static {
        // Set the default locale to a different one than the locale used for the tests to ensure
        // that
        // the CalendarView does not rely on any other locale than the configured one --
        // especially not the default locale.
        Locale.setDefault(Locale.GERMANY);
    }

    public CalendarInstrumentationTest() {
        super(CalendarActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
        locale = Locale.US;
        today = Calendar.getInstance(locale);
        today.set(2012, NOVEMBER, 16, 0, 0);
        minDate = today.getTime();
        today.set(2013, NOVEMBER, 16, 0, 0);
        maxDate = today.getTime();
        today.set(2012, NOVEMBER, 16, 0, 0);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        Context context = getInstrumentation().getTargetContext();
        Locale locale = new Locale("en", "US");
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
    }

    public void testUSLocaleCapitalizesDaysOfWeekAndMonth() throws Exception {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CalendarView view = (CalendarView) getActivity().findViewById(R.id.calendar_view);
                view.init(minDate, maxDate, Locale.US);
                LinearLayout header = (LinearLayout) view.findViewById(R.id.cv_day_names);
                TextView firstDay = (TextView) header.getChildAt(1);
                assertThat(firstDay).hasTextString("SUN");
            }
        });
        getInstrumentation().waitForIdleSync();
        onView(withText("Select Day"))
                .check(matches(isDisplayed()));
        onView(withText("Select Range"))
                .check(matches(isDisplayed()));
        onView(withText("November 2012"))
                .check(matches(isDisplayed()));
        onView(withText("SUN"))
                .check(matches(isDisplayed()));
        onView(withText("MON"))
                .check(matches(isDisplayed()));
        onView(withText("TUE"))
                .check(matches(isDisplayed()));
        onView(withText("WED"))
                .check(matches(isDisplayed()));
        onView(withText("THU"))
                .check(matches(isDisplayed()));
        onView(withText("FRI"))
                .check(matches(isDisplayed()));
        onView(withText("SAT"))
                .check(matches(isDisplayed()));
    }

    public void testFrenchLocaleDoesNotCapitalizeDaysOfWeekAndMonth() throws Exception {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CalendarView view = (CalendarView) getActivity().findViewById(R.id.calendar_view);
                view.init(minDate, maxDate, Locale.FRENCH);
                LinearLayout header = (LinearLayout) view.findViewById(R.id.cv_day_names);
                TextView firstDay = (TextView) header.getChildAt(1);
                assertThat(firstDay).hasTextString("lun.");
            }
        });
        getInstrumentation().waitForIdleSync();
        onView(withText("novembre 2012"))
                .check(matches(isDisplayed()));
        onView(withText("lun."))
                .check(matches(isDisplayed()));
        onView(withText("mar."))
                .check(matches(isDisplayed()));
        onView(withText("mer."))
                .check(matches(isDisplayed()));
        onView(withText("jeu."))
                .check(matches(isDisplayed()));
        onView(withText("ven."))
                .check(matches(isDisplayed()));
        onView(withText("sam."))
                .check(matches(isDisplayed()));
        onView(withText("dim."))
                .check(matches(isDisplayed()));
    }

    public void testJapaneseLocaleHasCorrectMonthAndYearHeader() throws Exception {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Context context = getInstrumentation().getTargetContext();
                Locale locale = new Locale("ja");
                Configuration config = new Configuration();
                config.locale = locale;
                context.getResources().updateConfiguration(config, null);
                CalendarView view = (CalendarView) getActivity().findViewById(R.id.calendar_view);
                view.init(minDate, maxDate, Locale.JAPAN);
            }
        });
        getInstrumentation().waitForIdleSync();
        onView(withText("2012年11月"))
                .check(matches(isDisplayed()));
    }

    public void testSelectingDateScrollToMonth() throws Throwable {
        final CalendarView view = (CalendarView) getActivity().findViewById(R.id.calendar_view);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Calendar march2013 = buildCal(2013, MARCH, 1);
                view.init(minDate, maxDate, locale)
                        .inMode(SINGLE)
                        .withSelectedDate(march2013.getTime());
                Assertions.assertThat(view.getSelectedCals()).hasSize(1);
                assertThat((TextView) view.findViewById(R.id.cv_month_name)).hasTextString("March" +
                        " 2013");
            }
        });
        getInstrumentation().waitForIdleSync();
    }

    private Calendar buildCal(int year, int month, int day) {
        Calendar jumpToCal = Calendar.getInstance(locale);
        jumpToCal.set(year, month, day);
        return jumpToCal;
    }
}
