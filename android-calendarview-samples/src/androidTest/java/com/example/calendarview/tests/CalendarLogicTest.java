package com.example.calendarview.tests;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.calendarview.R;
import com.example.calendarview.TestActivity;
import com.oyorooms.DateStateDescriptor;
import com.tripadvisor.CalendarView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.tripadvisor.CalendarView.SelectionMode.RANGE;
import static com.tripadvisor.CalendarView.SelectionMode.SINGLE;
import static java.util.Calendar.DATE;
import static java.util.Calendar.DAY_OF_WEEK;
import static java.util.Calendar.DECEMBER;
import static java.util.Calendar.FEBRUARY;
import static java.util.Calendar.JANUARY;
import static java.util.Calendar.MAY;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.NOVEMBER;
import static java.util.Calendar.YEAR;
import static org.fest.assertions.api.ANDROID.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Created by Kurry Tran
 * Email: ktran@tripadvisor.com
 * Date: 2/7/14
 */
public class CalendarLogicTest extends ActivityInstrumentationTestCase2<TestActivity> {

    private Locale locale;
    private Calendar today;
    private Date maxDate;
    private Date minDate;
    private Activity activity;

    static {
        // Set the default locale to a different one than the locale used for the tests to ensure
        // that
        // the CalendarView does not rely on any other locale than the configured one --
        // especially not the default locale.
        Locale.setDefault(Locale.GERMANY);
    }

    public CalendarLogicTest() {
        super(TestActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        activity = getActivity();
        locale = Locale.US;
        today = Calendar.getInstance(locale);
        today.set(2012, NOVEMBER, 16, 0, 0);
        minDate = today.getTime();
        today.set(2013, NOVEMBER, 16, 0, 0);
        maxDate = today.getTime();
        today.set(2012, NOVEMBER, 16, 0, 0);
    }

    public void testInitDecember() throws Exception {
        final CalendarView view = new CalendarView(activity, null);
        Locale locale = Locale.US;
        Calendar dec2012 = buildCal(2012, DECEMBER, 1);
        Calendar dec2013 = buildCal(2013, DECEMBER, 1);
        view.init(dec2012.getTime(), dec2013.getTime(), locale)
                .inMode(SINGLE)
                .withSelectedDate(dec2012.getTime());
        assertThat(view.getWeeks()).hasSize(53);
    }

    public void testInitJanuary() throws Throwable {
        CalendarView view = new CalendarView(activity, null);
        final Calendar jan2012 = buildCal(2012, JANUARY, 1);
        final Calendar jan2013 = buildCal(2013, JANUARY, 1);
        view.init(jan2012.getTime(), jan2013.getTime(), Locale.US)
                .inMode(SINGLE)
                .withSelectedDate(jan2012.getTime());
        assertThat(view.getWeeks()).hasSize(53);
    }

    public void testInitMidYear() throws Exception {
        CalendarView view = new CalendarView(activity, null);
        Calendar may2012 = buildCal(2012, MAY, 1);
        Calendar may2013 = buildCal(2013, MAY, 1);
        view.init(may2012.getTime(), may2013.getTime(), Locale.US)
                .inMode(SINGLE)
                .withSelectedDate(may2012.getTime());
        assertThat(view.getWeeks()).hasSize(53);
    }

    public void testSelectabilityInFirstMonth() throws Exception {
        CalendarView view = new CalendarView(activity, null);
        view.init(minDate, maxDate, locale)
                .inMode(SINGLE)
                .withSelectedDate(minDate);
        assertThat((TextView) view.findViewById(R.id.cv_month_name)).hasTextString("November 2012");
        assertThat(view.getSelectedCells()).hasSize(1);
        assertThat(view.getSelectedCals()).hasSize(1);
    }

    public void testSelectabilityInLastMonth() throws Exception {
        CalendarView view = new CalendarView(activity, null);
        Calendar jan2012 = buildCal(2012, JANUARY, 1);
        Calendar jan2013 = buildCal(2013, JANUARY, 30);
        Calendar selected = buildCal(2013, JANUARY, 29);
        view.init(jan2012.getTime(), jan2013.getTime(), locale)
                .inMode(SINGLE)
                .withSelectedDate(selected.getTime());
        assertThat((TextView) view.findViewById(R.id.cv_month_name)).hasTextString("January 2013");
        assertThat(view.getSelectedCells()).hasSize(1);
        assertThat(view.getSelectedCals()).hasSize(1);
    }

    public void testInitSingleWithMultipleSelections() throws Exception {
        CalendarView view = new CalendarView(activity, null);
        List<Date> selectedDates = new ArrayList<Date>();
        selectedDates.add(minDate);
        // This one should work.
        view.init(minDate, maxDate, locale) //
                .inMode(SINGLE) //
                .withSelectedDates(selectedDates);

        // Now add another date and try init'ing again in SINGLE mode.
        Calendar secondSelection = buildCal(2012, NOVEMBER, 17);
        selectedDates.add(secondSelection.getTime());
        try {
            view.init(minDate, maxDate, locale) //
                    .inMode(SINGLE) //
                    .withSelectedDates(selectedDates);
            fail("Should not have been able to init() with SINGLE mode && multiple selected dates");
        } catch (IllegalArgumentException expected) {
        }
    }

    public void testNullInitArguments() throws Exception {
        final CalendarView view = new CalendarView(activity, null);
        final Date validDate = today.getTime();
        try {
            view.init(validDate, validDate, locale) //
                    .inMode(SINGLE) //
                    .withSelectedDate(null);
            fail("Should not have been able to pass in a null startDate");
        } catch (IllegalArgumentException expected) {
        }
        try {
            view.init(null, validDate, locale) //
                    .inMode(SINGLE) //
                    .withSelectedDate(validDate);
            fail("Should not have been able to pass in a null minDate");
        } catch (IllegalArgumentException expected) {
        }
        try {
            view.init(validDate, null, locale) //
                    .inMode(SINGLE) //
                    .withSelectedDate(validDate);
            fail("Should not have been able to pass in a null maxDate");
        } catch (IllegalArgumentException expected) {
        }
        try {
            view.init(validDate, validDate, null) //
                    .inMode(SINGLE) //
                    .withSelectedDate(validDate);
            fail("Should not have been able to pass in a null locale");
        } catch (IllegalArgumentException expected) {
        }
    }

    public void testZeroDates() throws Exception {
        final CalendarView view = new CalendarView(activity, null);
        final Date validDate = today.getTime();
        final Date zeroDate = new Date(0L);
        try {
            view.init(validDate, validDate, locale) //
                    .inMode(SINGLE) //
                    .withSelectedDate(zeroDate);
            fail("Should not have been able to pass in a zero startDate");
        } catch (IllegalArgumentException expected) {
        }
        try {
            view.init(zeroDate, validDate, locale) //
                    .inMode(SINGLE) //
                    .withSelectedDate(validDate);
            fail("Should not have been able to pass in a zero minDate");
        } catch (IllegalArgumentException expected) {
        }
        try {
            view.init(validDate, zeroDate, locale) //
                    .inMode(SINGLE) //
                    .withSelectedDate(validDate);
            fail("Should not have been able to pass in a zero maxDate");
        } catch (IllegalArgumentException expected) {
        }
    }

    public void testMinAndMaxMixup() throws Exception {
        final CalendarView view = new CalendarView(activity, null);
        final Date minDate = today.getTime();
        today.add(YEAR, -1);
        final Date maxDate = today.getTime();
        try {
            view.init(minDate, maxDate, locale) //
                    .inMode(SINGLE) //
                    .withSelectedDate(minDate);
            fail("Should not have been able to pass in a maxDate < minDate");
        } catch (IllegalArgumentException expected) {
        }
    }

    public void testSelectedNotInRange() throws Exception {
        final CalendarView view = new CalendarView(activity, null);
        final Date minDate = today.getTime();
        today.add(YEAR, 1);
        final Date maxDate = today.getTime();
        today.add(YEAR, 1);
        Date selectedDate = today.getTime();
        try {
            view.init(minDate, maxDate, locale) //
                    .inMode(SINGLE) //
                    .withSelectedDate(selectedDate);
            fail("Should not have been able to pass in a selectedDate > maxDate");
        } catch (IllegalArgumentException expected) {
        }
        today.add(YEAR, -5);
        selectedDate = today.getTime();
        try {
            view.init(minDate, maxDate, locale) //
                    .inMode(SINGLE) //
                    .withSelectedDate(selectedDate);
            fail("Should not have been able to pass in a selectedDate < minDate");
        } catch (IllegalArgumentException expected) {
        }
    }

    public void testSelectDateThrowsExceptionForDatesOutOfRange() throws Exception {
        final CalendarView view = new CalendarView(activity, null);
        view.init(minDate, maxDate, locale) //
                .inMode(SINGLE) //
                .withSelectedDate(today.getTime());
        Calendar outOfRange = buildCal(2015, FEBRUARY, 1);
        try {
            view.selectDate(outOfRange.getTime());
            fail("selectDate should've blown up with an out of range date");
        } catch (IllegalArgumentException expected) {
        }
    }

    public void testHideMonthName() throws Exception {
        final CalendarView view = new CalendarView(activity, null);
        view.setMonthNameHidden(true);
        assertThat(view.findViewById(R.id.cv_month_name)).isNotShown();
    }

    public void testSelectDateReturnsTrueForDateInRange() {
        final CalendarView view = new CalendarView(activity, null);
        view.init(minDate, maxDate, locale) //
                .inMode(SINGLE) //
                .withSelectedDate(today.getTime());
        Calendar inRange = buildCal(2013, FEBRUARY, 1);
        boolean wasAbleToSetDate = view.selectDate(inRange.getTime());
        assertThat(wasAbleToSetDate).isTrue();
    }

    public void testSelectDateDoesntSelectDisabledCell() {
        final CalendarView view = new CalendarView(activity, null);
        view.init(minDate, maxDate, locale) //
                .inMode(SINGLE) //
                .withSelectedDate(today.getTime());
        Calendar jumpToCal = buildCal(2013, FEBRUARY, 1);
        boolean wasAbleToSetDate = view.selectDate(jumpToCal.getTime());
        assertThat(wasAbleToSetDate).isTrue();
        assertThat(view.getSelectedCells().get(0).isSelectable()).isTrue();
    }

    public void testOnDateConfiguredListener() {
        final CalendarView view = new CalendarView(activity, null);
        final Calendar testCal = Calendar.getInstance(locale);
        view.setDateSelectableFilter(new CalendarView.DateSelectableFilter() {
            @Override
            public boolean isDateSelectable(Date date) {
                testCal.setTime(date);
                int dayOfWeek = testCal.get(DAY_OF_WEEK);
                return dayOfWeek > 1 && dayOfWeek < 7;
            }
        });
        view.init(minDate, maxDate, locale) //
                .inMode(SINGLE) //
                .withSelectedDate(today.getTime());
        Calendar jumpToCal = Calendar.getInstance(locale);
        jumpToCal.setTime(today.getTime());
        jumpToCal.add(MONTH, 2);
        jumpToCal.set(DAY_OF_WEEK, 1);
        boolean wasAbleToSetDate = view.selectDate(jumpToCal.getTime());
        assertThat(wasAbleToSetDate).isFalse();

        jumpToCal.set(DAY_OF_WEEK, 2);
        wasAbleToSetDate = view.selectDate(jumpToCal.getTime());
        assertThat(wasAbleToSetDate).isTrue();
    }

    public void testWithoutDateSelectedListener() throws Exception {
        final CalendarView view = new CalendarView(activity, null);
        view.init(minDate, maxDate, locale)
                .inMode(SINGLE)
                .withSelectedDate(today.getTime());

        Calendar jumpToCal = Calendar.getInstance(locale);
        jumpToCal.setTime(today.getTime());
        jumpToCal.add(DATE, 1);
        DateStateDescriptor cellToClick =
                new DateStateDescriptor(jumpToCal.getTime(), true, true, true, true, true, 0,
                        DateStateDescriptor.RangeState.NONE);
        view.getListener().handleClick(cellToClick);

        assertThat(view.getSelectedCals().get(0).get(DATE)).isEqualTo(jumpToCal.get(DATE));
    }

    public void testRangeSelectionWithNoInitialSelection() throws Exception {
        final CalendarView view = new CalendarView(activity, null);
        view.init(minDate, maxDate, locale)
                .inMode(RANGE);
        assertThat(view.getSelectedCals()).hasSize(0);
        assertThat(view.getSelectedCells()).hasSize(0);

        Calendar nov18 = buildCal(2012, NOVEMBER, 18);
        view.selectDate(nov18.getTime());
        assertOneDateSelected(view);

        Calendar nov24 = buildCal(2012, NOVEMBER, 24);
        view.selectDate(nov24.getTime());
        assertRangeSelected(view);

        assertRangeSelectionBehavior(view);
    }

    public void testRangeWithTwoInitialSelections() throws Exception {
        final CalendarView view = new CalendarView(activity, null);
        Calendar nov18 = buildCal(2012, NOVEMBER, 18);
        Calendar nov24 = buildCal(2012, NOVEMBER, 24);
        List<Date> selectedDates = Arrays.asList(nov18.getTime(), nov24.getTime());
        view.init(minDate, maxDate, locale)
                .inMode(RANGE)
                .withSelectedDates(selectedDates);
        assertRangeSelected(view);

        assertRangeSelectionBehavior(view);
    }

    public void testRangeWithOneInitialSelection() throws Exception {
        final CalendarView view = new CalendarView(activity, null);
        Calendar nov18 = buildCal(2012, NOVEMBER, 18);
        Calendar nov24 = buildCal(2012, NOVEMBER, 24);
        List<Date> selectedDates = Arrays.asList(nov18.getTime());
        view.init(minDate, maxDate, locale)
                .inMode(RANGE)
                .withSelectedDates(selectedDates);
        assertOneDateSelected(view);
        view.selectDate(nov24.getTime());
        assertRangeSelected(view);
        assertRangeSelectionBehavior(view);
    }

    private void assertRangeSelectionBehavior(CalendarView view) {
        // Start a new range in the middle of the current (Nov 18 - Nov 24) one.
        Calendar nov20 = buildCal(2012, NOVEMBER, 20);
        view.selectDate(nov20.getTime());
        assertOneDateSelected(view);

        // Finish that range.
        Calendar nov26 = buildCal(2012, NOVEMBER, 26);
        view.selectDate(nov26.getTime());
        assertRangeSelected(view);

        // Start a new range in the middle of the current (Nov 20 - Nov 26) one.
        Calendar nov24 = buildCal(2012, NOVEMBER, 24);
        view.selectDate(nov24.getTime());
        assertOneDateSelected(view);

        // Only Nov 24 is selected: going backward should start a new range.
        view.selectDate(nov20.getTime());
        assertOneDateSelected(view);
    }


    private void assertRangeSelected(CalendarView view) {
        assertThat(view.getSelectedCals()).hasSize(2);
        assertThat(view.getSelectedCells()).hasSize(7);
        assertThat(view.getSelectedDates()).hasSize(7);
    }

    private void assertOneDateSelected(CalendarView view) {
        assertThat(view.getSelectedCals()).hasSize(1);
        assertThat(view.getSelectedCells()).hasSize(1);
        assertThat(view.getSelectedDates()).hasSize(1);
    }

    public void testFirstDayOfWeekIsMonday() throws Exception {
        final CalendarView view = new CalendarView(activity, null);
        final Locale greatBritain = new Locale("en", "GB");
        // Verify that firstDayOfWeek is actually Monday.
        Calendar cal = Calendar.getInstance(greatBritain);
        assertThat(cal.getFirstDayOfWeek()).isEqualTo(Calendar.MONDAY);
        view.init(minDate, maxDate, greatBritain);
        LinearLayout header = (LinearLayout) view.findViewById(R.id.cv_day_names);
        TextView firstDay = (TextView) header.getChildAt(1);
        assertThat(firstDay).hasTextString("MON");
    }

    private Calendar buildCal(int year, int month, int day) {
        Calendar jumpToCal = Calendar.getInstance(locale);
        jumpToCal.set(year, month, day);
        return jumpToCal;
    }
}
