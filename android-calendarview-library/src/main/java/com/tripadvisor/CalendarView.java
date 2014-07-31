/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tripadvisor;

import android.app.Service;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.tripadvisor.CalendarView.SelectionMode.RANGE;
import static com.tripadvisor.WeekCellDescriptor.RangeState.*;
import static com.tripadvisor.WeekCellDescriptor.RangeState.FIRST_AND_LAST;
import static com.tripadvisor.WeekCellDescriptor.RangeState.NONE;
import static java.util.Calendar.DATE;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.DAY_OF_WEEK;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.SECOND;
import static java.util.Calendar.WEEK_OF_YEAR;
import static java.util.Calendar.YEAR;

/**
 * Android component to allow picking a date from a calendar view (a list of months).  Must be
 * initialized after inflation with {@link #init(java.util.Date, java.util.Date)} and can be customized with any of the
 * {@link com.tripadvisor.CalendarView.FluentInitializer} methods returned.  The currently selected date can be retrieved with
 * {@link #getSelectedDate()}.
 */
public class CalendarView extends FrameLayout {

    /**
     * Affects when the month selection will change while scrolling upe
     */
    private static final int SCROLL_HYST_WEEKS = 2;

    private static final int UNSCALED_WEEK_MIN_VISIBLE_HEIGHT = 12;

    private static final int UNSCALED_LIST_SCROLL_TOP_OFFSET = 2;

    private final WeekView.Listener mListener = new CellClickedListener();

    private final List<WeekDescriptor> mWeeks = new ArrayList<WeekDescriptor>();

    private final List<WeekCellDescriptor> mSelectedCells = new ArrayList<WeekCellDescriptor>();

    private final List<Calendar> mSelectedCals = new ArrayList<Calendar>();

    private final List<Calendar> mHighlightedCals = new ArrayList<Calendar>();

    private final List<WeekCellDescriptor> mHighlightedCells = new ArrayList<WeekCellDescriptor>();

    private final List<List<WeekCellDescriptor>> mCells =
            new ArrayList<List<WeekCellDescriptor>>();

    private Calendar mToday = Calendar.getInstance();

    private Drawable mCurrentDayOfWeekBackground;

    private boolean mMonthNameHidden;
    /**
     * The visible height of a week view.
     */
    private int mWeekMinVisibleHeight = 12;
    /**
     * The adapter for the mWeeks list.
     */
    private WeeksAdapter mAdapter;
    /**
     * The mWeeks list.
     */
    private ListView mListView;
    /**
     * The name of the month to display.
     */
    private TextView mMonthName;
    /**
     * The header with week day names.
     */
    private ViewGroup mDayNamesHeader;
    /**
     * Which month should be displayed/highlighted [0-11].
     */
    private int mCurrentMonthDisplayed = -1;
    /**
     * Used for tracking during a scroll.
     */
    private long mPreviousScrollPosition;
    /**
     * Used for tracking which direction the view is scrolling.
     */
    private boolean mIsScrollingUp = false;
    /**
     * The previous scroll state of the mWeeks ListView.
     */
    private int mPreviousScrollState = OnScrollListener.SCROLL_STATE_IDLE;
    /**
     * The current scroll state of the mWeeks ListView.
     */
    private int mCurrentScrollState = OnScrollListener.SCROLL_STATE_IDLE;
    /**
     * Temporary instance to avoid multiple instantiations.
     */
    private Calendar mTempDate;
    /**
     * The first day of the focused month.
     */
    private Calendar mFirstDayOfMonth;
    /**
     * The start date of the range supported by this picker.
     */
    private Calendar mMinCal;
    /**
     * The end date of the range supported by this picker.
     */
    private Calendar mMaxCal;

    private SimpleDateFormat mWeekdayFormatter;

    private static Typeface mLightTypeface;

    private Locale mLocale;

    private SimpleDateFormat mMonthNameFormat;

    private DateFormat mFullMonthNameFormat;

    private DateFormat mFullDateFormat;

    private Calendar mMonthCounter;

    private boolean mDisplayOnly;

    private OnDateSelectedListener mDateSelectedListener;

    private DateSelectableFilter mDateConfiguredListener;

    private OnInvalidDateSelectedListener mInvalidDateSelectedListener =
            new DefaultOnInvalidDateSelectedListener();

    SelectionMode mSelectionMode = SelectionMode.SINGLE;

    public CalendarView(Context context) {
        this(context, null);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        setLocale(Locale.getDefault());
        mLightTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/RobotoCondensed-Light.ttf");
        mCurrentDayOfWeekBackground = getResources().getDrawable(R.drawable.calendar_day_of_week_bg);
        DisplayMetrics displayMetrics = getResources()
                .getDisplayMetrics();
        mWeekMinVisibleHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                UNSCALED_WEEK_MIN_VISIBLE_HEIGHT, displayMetrics);
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Service.LAYOUT_INFLATER_SERVICE);
        View content = layoutInflater.inflate(R.layout.calendar_view, this, false);
        addView(content);

        mListView = (ListView) findViewById(android.R.id.list);
        mDayNamesHeader = (ViewGroup) content.findViewById(R.id.cv_day_names);
        mMonthName = (TextView) content.findViewById(R.id.cv_month_name);
        mMonthName.setTypeface(mLightTypeface);
        Drawable horizontalDivider = getResources().getDrawable(R.drawable.calendar_week_bg_shadow);
        ((ImageView) findViewById(R.id.cv_divider)).setImageDrawable(horizontalDivider);
        mFullDateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, mLocale);
        if (isInEditMode()) {
            Calendar nextYear = Calendar.getInstance(mLocale);
            nextYear.add(Calendar.YEAR, 1);
            init(new Date(), nextYear.getTime())
                    .withSelectedDate(new Date());
        }
    }

    /**
     * Returns a string summarizing what the client sent us for init() params.
     */
    private static String dbg(Date minDate, Date maxDate) {
        return "minDate: " + minDate + "\nmaxDate: " + maxDate;
    }

    /**
     * Clears out the hours/minutes/seconds/millis of a Calendar.
     */
    static void setMidnight(Calendar cal) {
        cal.set(HOUR_OF_DAY, 0);
        cal.set(MINUTE, 0);
        cal.set(SECOND, 0);
        cal.set(MILLISECOND, 0);
    }

    private static boolean containsDate(List<Calendar> selectedCals, Calendar cal) {
        for (Calendar selectedCal : selectedCals) {
            if (sameDate(cal, selectedCal)) {
                return true;
            }
        }
        return false;
    }

    private static Calendar minDate(List<Calendar> selectedCals) {
        if (selectedCals == null || selectedCals.size() == 0) {
            return null;
        }
        Collections.sort(selectedCals);
        return selectedCals.get(0);
    }

    private static Calendar maxDate(List<Calendar> selectedCals) {
        if (selectedCals == null || selectedCals.size() == 0) {
            return null;
        }
        Collections.sort(selectedCals);
        return selectedCals.get(selectedCals.size() - 1);
    }

    private static boolean sameDate(Calendar cal, Calendar selectedDate) {
        return cal.get(MONTH) == selectedDate.get(MONTH)
                && cal.get(YEAR) == selectedDate.get(YEAR)
                && cal.get(DAY_OF_MONTH) == selectedDate.get(DAY_OF_MONTH);
    }

    private static boolean betweenDates(Calendar cal, Calendar minCal, Calendar maxCal) {
        final Date date = cal.getTime();
        return betweenDates(date, minCal, maxCal);
    }

    static boolean betweenDates(Date date, Calendar minCal, Calendar maxCal) {
        final Date min = minCal.getTime();
        return (date.equals(min) || date.after(min)) // >= mMinCal
                && date.before(maxCal.getTime()); // && < mMaxCal
    }

    private static boolean sameMonth(Calendar cal, WeekDescriptor week) {
        return (cal.get(WEEK_OF_YEAR) == week.getWeekInYear() && cal.get(YEAR) == week.getYear());
    }

    public SelectionMode getSelectionMode() {
        return mSelectionMode;
    }

    public void setSelectionMode(SelectionMode selectionMode) {
        this.mSelectionMode = selectionMode;
        if (selectionMode == SelectionMode.SINGLE && getSelectedCals().size() > 0) {
            WeekCellDescriptor cellDescriptor = getSelectedCells().get(0);
            Date selectedDate = getSelectedCals().get(0).getTime();
            clearOldSelections();
            doSelectDate(selectedDate, cellDescriptor);
        }else if (mSelectionMode == RANGE && getSelectedCells().size() == 1) {
            getSelectedCells().get(0).setRangeState(OPEN);
        }
        validateAndUpdate();
    }

    public synchronized List<Calendar> getSelectedCals() {
        return mSelectedCals;
    }

    public synchronized List<WeekCellDescriptor> getSelectedCells() {
        return mSelectedCells;
    }

    public synchronized List<WeekDescriptor> getWeeks() {
        return mWeeks;
    }

    private boolean doSelectDate(Date date, WeekCellDescriptor cell) {
        Calendar newlySelectedCal = Calendar.getInstance(mLocale);
        newlySelectedCal.setTime(date);
        // Sanitize input: clear out the hours/minutes/seconds/millis.
        setMidnight(newlySelectedCal);

        // Clear any remaining range state.
        for (WeekCellDescriptor selectedCell : getSelectedCells()) {
            selectedCell.setRangeState(NONE);
        }

        switch (mSelectionMode) {
            case RANGE:
                if (getSelectedCals().size() > 1) {
                    // We've already got a range selected: clear the old one.
                    clearOldSelections();
                } else if (getSelectedCals().size() == 1 && newlySelectedCal.before(getSelectedCals().get
                        (0))) {
                    // We're moving the start of the range back in time: clear the old start date.
                    clearOldSelections();
                }
                break;

            case MULTIPLE:
                date = applyMultiSelect(date, newlySelectedCal);
                break;

            case SINGLE:
                clearOldSelections();
                break;
            default:
                throw new IllegalStateException("Unknown mSelectionMode " + mSelectionMode);
        }

        if (date != null) {
            // Select a new cell.
            if (getSelectedCells().size() == 0 || !getSelectedCells().get(0).equals(cell)) {
                getSelectedCells().add(cell);
                cell.setSelected(true);
            }
            getSelectedCals().add(newlySelectedCal);

            if (mSelectionMode == RANGE && getSelectedCells().size() > 1) {
                // Select all days in between start and end.
                Date start = getSelectedCells().get(0).getDate();
                Date end = getSelectedCells().get(1).getDate();
                getSelectedCells().get(0).setRangeState(FIRST);
                getSelectedCells().get(1).setRangeState(LAST);

                for (List<WeekCellDescriptor> week : mCells) {
                    for (WeekCellDescriptor singleCell : week) {
                        if (singleCell.getDate().after(start)
                                && singleCell.getDate().before(end)
                                && singleCell.isSelectable()) {
                            singleCell.setSelected(true);
                            singleCell.setRangeState(MIDDLE);
                            getSelectedCells().add(singleCell);
                        }
                    }
                }
            } else if (mSelectionMode == RANGE && getSelectedCells().size() == 1 && getSelectedCals().get(0).compareTo(newlySelectedCal) != 0 || mSelectionMode == RANGE && getSelectedCells().size() == 1 && getSelectedCells().get(0).getRangeState() == FIRST_AND_LAST  || mSelectionMode == RANGE && getSelectedCells().size() == 1 && getSelectedCells().get(0).getRangeState() == NONE) {
                getSelectedCells().get(0).setRangeState(OPEN);
            } else if (mSelectionMode == RANGE && getSelectedCells().size() == 1 && getSelectedCells().get(0).getRangeState() == OPEN || mSelectionMode == RANGE && getSelectedCells().size() == 1 && getSelectedCals().get(0).compareTo(newlySelectedCal) == 0 || mSelectionMode == RANGE && getSelectedCells().size() == 1 && getSelectedCells().get(0).getRangeState() == OPEN) {
                getSelectedCells().get(0).setRangeState(FIRST_AND_LAST);
            }
        }
        // Update the adapter.
        validateAndUpdate();
        return date != null;
    }

    private void clearOldSelections() {
        for (WeekCellDescriptor selectedCell : getSelectedCells()) {
            // De-select the currently-selected cell.
            selectedCell.setSelected(false);
            selectedCell.setRangeState(NONE);
        }

        getSelectedCells().clear();
        getSelectedCals().clear();
    }

    private Date applyMultiSelect(Date date, Calendar selectedCal) {
        for (WeekCellDescriptor selectedCell : getSelectedCells()) {
            if (selectedCell.getDate().equals(date)) {
                // De-select the currently-selected cell.
                selectedCell.setSelected(false);
                getSelectedCells().remove(selectedCell);
                date = null;
                break;
            }
        }
        for (Calendar cal : getSelectedCals()) {
            if (sameDate(cal, selectedCal)) {
                getSelectedCals().remove(cal);
                break;
            }
        }
        return date;
    }

    private void validateAndUpdate() {
        if (mAdapter == null) {
            setUpAdapter();
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }
    private DateFormatSymbols mDateFormatSymbols;

    public FluentInitializer init(Date minDate, Date maxDate, Locale locale) {
        if (minDate == null || maxDate == null) {
            throw new IllegalArgumentException(
                    "minDate and maxDate must be non-null.  " + dbg(minDate, maxDate));
        }
        if (minDate.after(maxDate)) {
            throw new IllegalArgumentException(
                    "minDate must be before maxDate.  " + dbg(minDate, maxDate));
        }
        if (minDate.getTime() == 0 || maxDate.getTime() == 0) {
            throw new IllegalArgumentException(
                    "minDate and maxDate must be non-zero.  " + dbg(minDate, maxDate));
        }
        if (locale == null) {
            throw new IllegalArgumentException("Locale is null.");
        }

        // Make sure that all calendar instances use the same mLocale.
        this.mLocale = locale;
        setToday(Calendar.getInstance(locale));
        mMinCal = Calendar.getInstance(locale);
        mMaxCal = Calendar.getInstance(locale);
        mMonthCounter = Calendar.getInstance(locale);
        mDateFormatSymbols = new DateFormatSymbols(locale);
        mWeekdayFormatter = new SimpleDateFormat("EEE", locale);
        mMonthNameFormat =
                new SimpleDateFormat(getContext().getString(R.string.month_name_format), locale);
        mFullMonthNameFormat = new SimpleDateFormat(getContext().getString(R.string
                .header_month_name_format), locale);

        this.mSelectionMode = SelectionMode.SINGLE;
        // Clear out any previously-selected dates/cells.
        getSelectedCals().clear();
        getSelectedCells().clear();
        getHighlightedCells().clear();

        // Clear previous state.
        mCells.clear();
        getWeeks().clear();
        mMinCal.setTime(minDate);
        mMaxCal.setTime(maxDate);
        setMidnight(mMinCal);
        setMidnight(mMaxCal);
        mDisplayOnly = false;

        // maxDate is exclusive: bump back to the previous day so if maxDate is the first of a
        // month,
        // we don't accidentally include that month in the view.
        mMaxCal.add(MINUTE, -1);

        // Now iterate between mMinCal and mMaxCal and build up our list of mWeeks to show.
        mMonthCounter.setTime(mMinCal.getTime());
        final int maxMonth = mMaxCal.get(MONTH);
        final int maxYear = mMaxCal.get(YEAR);
        while ((mMonthCounter.get(MONTH) <= maxMonth // Up to, including the month.
                || mMonthCounter.get(YEAR) < maxYear) // Up to the year.
                && mMonthCounter.get(YEAR) < maxYear + 1) { // But not > next yr.
            Date date = mMonthCounter.getTime();
            WeekDescriptor week =
                    new WeekDescriptor(mMonthCounter.get(WEEK_OF_YEAR), mMonthCounter.get(YEAR), date,
                            mMonthNameFormat.format(date));
            List<WeekCellDescriptor> weekCells = getWeekCells(week, mMonthCounter);
            mCells.add(weekCells);
            Logr.d("Adding week %s", week);
            getWeeks().add(week);
            mMonthCounter.add(WEEK_OF_YEAR, 1);
        }

        setUpHeader();
        setUpListView();
        setUpAdapter();
        setMonthDisplayed(mMinCal);
        setMonthTitle(mMinCal.getTime());
        validateAndUpdate();
        return new FluentInitializer();
    }

    private void setMonthTitle(Date date) {
        mMonthName.setText(mFullMonthNameFormat.format(date));
    }

    /**
     * Both date parameters must be non-null and their {@link java.util.Date#getTime()} must not return 0.
     * Time of day will be ignored.  For instance, if you pass in {@code minDate} as 11/16/2012
     * 5:15pm and {@code maxDate} as 11/16/2013 4:30am, 11/16/2012 will be the first selectable date
     * and 11/15/2013 will be the last selectable date ({@code maxDate} is exclusive).
     * <p/>
     * This will implicitly set the {@link com.tripadvisor.CalendarView.SelectionMode} to {@link com.tripadvisor.CalendarView.SelectionMode#SINGLE}.  If you
     * want a different selection mode, use {@link com.tripadvisor.CalendarView.FluentInitializer#inMode(com.tripadvisor.CalendarView.SelectionMode)} on the
     * {@link com.tripadvisor.CalendarView.FluentInitializer} this method returns.
     * <p/>
     * The calendar will be constructed using the default mLocale as returned by {@link
     * java.util.Locale#getDefault()}. If you wish the calendar to be constructed using a different
     * mLocale, use {@link #init(java.util.Date, java.util.Date, java.util.Locale)}.
     *
     * @param minDate Earliest selectable date, inclusive.  Must be earlier than {@code maxDate}.
     * @param maxDate Latest selectable date, exclusive.  Must be later than {@code minDate}.
     */
    public FluentInitializer init(Date minDate, Date maxDate) {
        return init(minDate, maxDate, Locale.getDefault());
    }

    private void scrollToSelectedMonth(final int selectedIndex) {
        post(new Runnable() {
            @Override
            public void run() {
                Logr.d("Scrolling to position %d", selectedIndex);
                setSelection(selectedIndex);
            }
        });
    }

    public void setSelection(int position) {
        mListView.setSelectionFromTop(position, 0);
    }

    private void scrollToSelectedDates() {
        Integer selectedIndex = null;
        Integer todayIndex = null;
        Calendar today = Calendar.getInstance(mLocale);
        for (int c = 0; c < getWeeks().size(); c++) {
            WeekDescriptor week = getWeeks().get(c);
            if (selectedIndex == null) {
                for (Calendar selectedCal : getSelectedCals()) {
                    if (sameMonth(selectedCal, week)) {
                        selectedIndex = c;
                        break;
                    }
                }
                if (selectedIndex == null && todayIndex == null && sameMonth(today, week)) {
                    todayIndex = c;
                }
            }
        }
        if (selectedIndex != null) {
            WeekDescriptor week = getWeeks().get(selectedIndex);
            Calendar selectedCal = Calendar.getInstance();
            selectedCal.setTime(week.getDate());
            setMonthDisplayed(selectedCal);
            scrollToSelectedMonth(selectedIndex);
            // KLT: This was added due to weeks that contain two months.
            int daysPerWeek = 7;
            selectedCal.add(Calendar.DAY_OF_MONTH, daysPerWeek);
            mAdapter.setFocusMonth(selectedCal.get(MONTH));
        } else if (todayIndex != null) {
            setMonthDisplayed(today);
            // KLT: This was changed since the background drawable was not correct on launch.
            setSelection(todayIndex);
            mAdapter.setFocusMonth(today.get(MONTH));
        }
    }

    public void scrollToDate(Date date) {
        Integer selectedIndex = null;
        Calendar scrollToCal = Calendar.getInstance(mLocale);
        scrollToCal.setTime(date);

        for (int c = 0; c < getWeeks().size(); c++) {
            WeekDescriptor week = getWeeks().get(c);
            if (selectedIndex == null) {
                if (sameMonth(scrollToCal, week)) {
                    selectedIndex = c;
                    break;
                }
            }
        }
        if (selectedIndex != null) {
            WeekDescriptor week = getWeeks().get(selectedIndex);
            scrollToCal.setTime(week.getDate());
            setMonthDisplayed(scrollToCal);
            scrollToSelectedMonth(selectedIndex);
        }
    }

    /**
     * Select a new date.  Respects the {@link com.tripadvisor.CalendarView.SelectionMode} this CalendarPickerView is configured
     * with: if you are in {@link com.tripadvisor.CalendarView.SelectionMode#SINGLE}, the previously selected date will be
     * un-selected.  In {@link com.tripadvisor.CalendarView.SelectionMode#MULTIPLE}, the new date will be added to the list of
     * selected dates.
     * <p/>
     * If the selection was made (selectable date, in range), the view will scroll to the newly
     * selected date if it's not already visible.
     *
     * @return - whether we were able to set the date
     */
    public boolean selectDate(Date date) {
        validateDate(date);
        MonthCellWithMonthIndex monthCellWithMonthIndex = getMonthCellWithIndexByDate(date);
        if (monthCellWithMonthIndex == null || !isDateSelectable(date)) {
            return false;
        }
        boolean wasSelected = doSelectDate(date, monthCellWithMonthIndex.cell);
        if (wasSelected) {
            scrollToSelectedMonth(monthCellWithMonthIndex.monthIndex);
        }
        return wasSelected;
    }

    private void validateDate(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("Selected date must be non-null.");
        }
        if (date.getTime() == 0) {
            throw new IllegalArgumentException("Selected date must be non-zero.  " + date);
        }
        if (date.before(mMinCal.getTime()) || date.after(mMaxCal.getTime())) {
            throw new IllegalArgumentException(
                    "selectedDate must be between minDate and maxDate.  " + date);
        }
    }

    /**
     * Return cell and month-index (for scrolling) for a given Date.
     */
    private MonthCellWithMonthIndex getMonthCellWithIndexByDate(Date date) {
        int index = 0;
        Calendar searchCal = Calendar.getInstance(mLocale);
        searchCal.setTime(date);
        Calendar actCal = Calendar.getInstance(mLocale);
        for (List<WeekCellDescriptor> weekCells : mCells) {
            for (WeekCellDescriptor actCell : weekCells) {
                actCal.setTime(actCell.getDate());
                if (sameDate(actCal, searchCal) && actCell.isSelectable()) {
                    return new MonthCellWithMonthIndex(actCell, index);
                }
            }
            index++;
        }
        return null;
    }

    public boolean isMonthNameHidden() {
        return mMonthNameHidden;
    }

    public void setMonthNameHidden(boolean monthNameHidden) {
        if (mMonthNameHidden != monthNameHidden) {
            mMonthName = (TextView) findViewById(R.id.cv_month_name);
            if (monthNameHidden) {
                mMonthName.setVisibility(View.GONE);
            } else {
                mMonthName.setVisibility(View.VISIBLE);
            }
            mMonthNameHidden = monthNameHidden;
        }
    }

    @Override
    public boolean isEnabled() {
        return mListView.isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        mListView.setEnabled(enabled);
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setLocale(newConfig.locale);
    }

    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(CalendarView.class.getName());
    }

    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(CalendarView.class.getName());
    }

    public Date getSelectedDate() {
        return (getSelectedCals().size() > 0 ? getSelectedCals().get(0).getTime() : null);
    }

    public List<Date> getSelectedDates() {
        List<Date> selectedDates = new ArrayList<Date>();
        for (WeekCellDescriptor cal : getSelectedCells()) {
            selectedDates.add(cal.getDate());
        }
        Collections.sort(selectedDates);
        return selectedDates;
    }

    /**
     * Sets the current mLocale.
     *
     * @param locale The current mLocale.
     */
    public void setLocale(Locale locale) {
        if (locale.equals(this.mLocale)) {
            return;
        }

        this.mLocale = locale;
        mTempDate = getCalendarForLocale(mTempDate, locale);
        mFirstDayOfMonth = getCalendarForLocale(mFirstDayOfMonth, locale);
        mMinCal = getCalendarForLocale(mMinCal, locale);
        mMaxCal = getCalendarForLocale(mMaxCal, locale);
        mDateFormatSymbols = new DateFormatSymbols(locale);
        mWeekdayFormatter = new SimpleDateFormat("EEE", locale);
        if (mDayNamesHeader != null) {
            setUpHeader();
        }
        if (mMonthName != null) {
            WeekDescriptor week = getWeeks().get(mListView.getSelectedItemPosition());
            mFullMonthNameFormat = new SimpleDateFormat("MMMM yyyy", locale);
            String newMonthName = mFullMonthNameFormat.format(week.getDate());
            mMonthName.setText(newMonthName);
        }
    }

    /**
     * Gets a calendar for locale bootstrapped with the value of a given calendar.
     *
     * @param oldCalendar The old calendar.
     * @param locale      The mLocale.
     */
    private Calendar getCalendarForLocale(Calendar oldCalendar, Locale locale) {
        if (oldCalendar == null) {
            return Calendar.getInstance(locale);
        } else {
            final long currentTimeMillis = oldCalendar.getTimeInMillis();
            Calendar newCalendar = Calendar.getInstance(locale);
            newCalendar.setTimeInMillis(currentTimeMillis);
            return newCalendar;
        }
    }

    /**
     * Creates a new adapter if necessary and sets up its parameters.
     */
    private void setUpAdapter() {
        if (mAdapter == null) {
            mAdapter = new WeeksAdapter(getContext());
            mListView.setAdapter(mAdapter);
        }
        // refresh the view with the new parameters
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Sets up the strings to be used by the header.
     */
    private void setUpHeader() {
        int daysPerWeek = 7;
        String[] dayLabels = new String[daysPerWeek];
        int firstDayOfWeek = getToday().getFirstDayOfWeek();
        for (int offset = 0; offset < 7; offset++) {
            getToday().set(Calendar.DAY_OF_WEEK, firstDayOfWeek + offset);
            if (mLocale.getLanguage().equals("en")) {
                dayLabels[offset] = mWeekdayFormatter.format(getToday().getTime()).toUpperCase(mLocale);
            } else {
                dayLabels[offset] = mWeekdayFormatter.format(getToday().getTime());
            }
        }

        TextView label = (TextView) mDayNamesHeader.getChildAt(0);
        assert label != null;
        label.setVisibility(View.GONE);

        for (int i = 1, count = mDayNamesHeader.getChildCount(); i < count; i++) {
            label = (TextView) mDayNamesHeader.getChildAt(i);
            if (i < daysPerWeek + 1) {
                int day = Calendar.getInstance(mLocale).get(Calendar.DAY_OF_WEEK);
                if (i == day && mCurrentDayOfWeekBackground != null) {
                    assert label != null;
                    //noinspection deprecation
                    label.setBackgroundDrawable(mCurrentDayOfWeekBackground);
                    label.setTextColor(Color.BLACK);
                }
                assert label != null;
                label.setText(dayLabels[i - 1]);
                label.setVisibility(View.VISIBLE);
            } else {
                assert label != null;
                label.setVisibility(View.GONE);
            }
        }
    }

    /**
     * Sets all the required fields for the list view.
     */
    private void setUpListView() {
        mListView.setDivider(null);
        mListView.setItemsCanFocus(true);
        mListView.setVerticalScrollBarEnabled(false);
        mListView.setOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                mPreviousScrollState = scrollState;
              if (mPreviousScrollState ==  OnScrollListener.SCROLL_STATE_IDLE) {
                mAdapter.setFocusMonth(mCurrentMonthDisplayed);
              }
            }

            public void onScroll(
                    AbsListView view, int firstVisibleItem, int visibleItemCount,
                    int totalItemCount) {
                CalendarView.this.onScroll(view
                );
            }
        });
    }

    /**
     * Updates the title and selected month if the <code>view</code> has moved to a new month.
     */
    private void onScroll(AbsListView view) {
        WeekRowView child = (WeekRowView) view.getChildAt(0);
        if (child == null) {
            return;
        }

        // Figure out where we are
        long currScroll = view.getFirstVisiblePosition() * child.getHeight() - child.getBottom();

        // If we have moved since our last call update the direction
        if (currScroll < mPreviousScrollPosition) {
            mIsScrollingUp = true;
        } else if (currScroll > mPreviousScrollPosition) {
            mIsScrollingUp = false;
        } else {
            return;
        }

        // Use some hysteresis for checking which month to highlight. This
        // causes the month to transition when two full mWeeks of a month are
        // visible when scrolling up, and when the first day in a month reaches
        // the top of the screen when scrolling down.
        int offset = child.getBottom() < mWeekMinVisibleHeight ? 1 : 0;
        if (mIsScrollingUp) {
            child = (WeekRowView) view.getChildAt(SCROLL_HYST_WEEKS + offset);
        } else if (offset != 0) {
            child = (WeekRowView) view.getChildAt(offset);
        }

        // Find out which month we're moving into
        int month;
        if (child == null) {
            return;
        }

        if (mIsScrollingUp) {
            month = child.getMonthOfFirstWeekDay();
        } else {
            month = child.getMonthOfLastWeekDay();
        }

        // And how it relates to our current highlighted month
        int monthDiff;
        if (mCurrentMonthDisplayed == 11 && month == 0) {
            monthDiff = 1;
        } else if (mCurrentMonthDisplayed == 0 && month == 11) {
            monthDiff = -1;
        } else {
            monthDiff = month - mCurrentMonthDisplayed;
        }
        int daysPerWeek = 7;
        // Only switch mWeeks if we're scrolling away from the currently
        // selected month
        if ((!mIsScrollingUp && monthDiff > 0) || (mIsScrollingUp && monthDiff < 0)) {
            Calendar firstDay = child.getFirstDay();
            if (mIsScrollingUp) {
                firstDay.add(Calendar.DAY_OF_MONTH, -daysPerWeek);
            } else {
                firstDay.add(Calendar.DAY_OF_MONTH, daysPerWeek);
            }
            setMonthDisplayed(firstDay);
        }
        mPreviousScrollPosition = currScroll;
        mPreviousScrollState = mCurrentScrollState;
    }

    /**
     * Sets the month displayed at the top of this view based on time. Override to add custom events
     * when the title is changed.
     *
     * @param calendar A day in the new focus month.
     */
    private void setMonthDisplayed(Calendar calendar) {
        final int newMonthDisplayed = calendar.get(Calendar.MONTH);
        mCurrentMonthDisplayed = newMonthDisplayed;
        String newMonthName = mFullMonthNameFormat.format(calendar.getTime());
        mMonthName.setText(newMonthName);
    }

    public List<WeekCellDescriptor> getWeekCells(WeekDescriptor week, Calendar startCal) {
        Calendar cal = Calendar.getInstance(mLocale);
        cal.setTime(startCal.getTime());
        List<WeekCellDescriptor> weekCells = new ArrayList<WeekCellDescriptor>();
        int firstDayOfWeek = cal.get(DAY_OF_WEEK);
        int offset = cal.getFirstDayOfWeek() - firstDayOfWeek;
        if (offset > 0) {
            offset -= 7;
        }
        cal.add(Calendar.DATE, offset);

        Calendar minSelectedCal = minDate(getSelectedCals());
        Calendar maxSelectedCal = maxDate(getSelectedCals());
        Logr.d("Building week row starting at %s", cal.getTime());
        for (int c = 0; c < 7; c++) {
            Date date = cal.getTime();
            boolean isCurrentMonth = cal.get(MONTH) == week.getMonth();
            boolean isSelected = containsDate(getSelectedCals(), cal);
            boolean isSelectable = betweenDates(cal, mMinCal, mMaxCal) && isDateSelectable(date);
            boolean isToday = sameDate(cal, getToday());
            boolean isHighlighted = containsDate(getHighlightedCals(), cal);
            int value = cal.get(DAY_OF_MONTH);
            WeekCellDescriptor.RangeState rangeState = NONE;
            if (getSelectedCals().size() > 1) {
                if (sameDate(minSelectedCal, cal)) {
                    rangeState = FIRST;
                } else if (sameDate(maxDate(getSelectedCals()), cal)) {
                    rangeState = LAST;
                } else if (betweenDates(cal, minSelectedCal, maxSelectedCal)) {
                    rangeState = MIDDLE;
                }
            }
            WeekCellDescriptor cellDescriptor = new WeekCellDescriptor(date, isCurrentMonth, isSelectable, isSelected, isToday,
                    isHighlighted, value, rangeState);
            if (cal.get(DAY_OF_MONTH) == 1) {
                cellDescriptor.setFirstDayOfTheMonth(true);
                if (mLocale.getLanguage().equals("en")) {
                    cellDescriptor.setMonth(getMonth(cal.get(MONTH)).toUpperCase(mLocale));
                } else {
                    cellDescriptor.setMonth(getMonth(cal.get(MONTH)));
                }
            }
            weekCells.add(cellDescriptor);

            cal.add(DATE, 1);
        }
        return weekCells;
    }

    private String getMonth(int month) {
        return mDateFormatSymbols.getShortMonths()[month];
    }

    private boolean isDateSelectable(Date date) {
        return mDateConfiguredListener == null || mDateConfiguredListener.isDateSelectable(date);
    }

    public void setOnDateSelectedListener(OnDateSelectedListener listener) {
        mDateSelectedListener = listener;
    }

    /**
     * Set a mListener to react to user selection of a disabled date.
     *
     * @param listener the mListener to set, or null for no reaction
     */
    public void setOnInvalidDateSelectedListener(OnInvalidDateSelectedListener listener) {
        mInvalidDateSelectedListener = listener;
    }

    /**
     * Set a listener used to discriminate between selectable and unselectable dates. Set this to
     * disable arbitrary dates as they are rendered.
     * <p/>
     * Important: set this before you call {@link #init(java.util.Date, java.util.Date)} methods.  If called afterwards,
     * it will not be consistently applied.
     */
    public void setDateSelectableFilter(DateSelectableFilter listener) {
        mDateConfiguredListener = listener;
    }

    public WeekView.Listener getListener() {
        return mListener;
    }

    private List<Calendar> getHighlightedCals() {
        return mHighlightedCals;
    }

    private List<WeekCellDescriptor> getHighlightedCells() {
        return mHighlightedCells;
    }

    private Calendar getToday() {
        return mToday;
    }

    private void setToday(Calendar today) {
        mToday = today;
    }

    public enum SelectionMode {
        /**
         * Only one date will be selectable.  If there is already a selected date and you select a
         * new one, the old date will be unselected.
         */
        SINGLE,
        /**
         * Multiple dates will be selectable.  Selecting an already-selected date will un-select
         * it.
         */
        MULTIPLE,
        /**
         * Allows you to select a date range.  Previous selections are cleared when you either: <ul>
         * <li>Have a range selected and select another date (even if it's in the current
         * range).</li> <li>Have one date selected and then select an earlier date.</li> </ul>
         */
        RANGE
    }

    /**
     * Interface to be notified when a new date is selected or unselected. This will only be called
     * when the user initiates the date selection.  If you call {@link #selectDate(java.util.Date)} this
     * mListener will not be notified.
     *
     * @see #setOnDateSelectedListener(com.tripadvisor.CalendarView.OnDateSelectedListener)
     */
    public interface OnDateSelectedListener {
        void onDateSelected(Date date);

        void onDateUnselected(Date date);
    }

    /**
     * Interface to be notified when an invalid date is selected by the user. This will only be
     * called when the user initiates the date selection. If you call {@link #selectDate(java.util.Date)} this
     * mListener will not be notified.
     *
     * @see #setOnInvalidDateSelectedListener(com.tripadvisor.CalendarView.OnInvalidDateSelectedListener)
     */
    public interface OnInvalidDateSelectedListener {
        void onInvalidDateSelected(Date date);
    }


    /**
     * Interface used for determining the selectability of a date cell when it is configured for
     * display on the calendar.
     *
     * @see #setDateSelectableFilter(com.tripadvisor.CalendarView.DateSelectableFilter)
     */
    public interface DateSelectableFilter {
        boolean isDateSelectable(Date date);
    }

    /**
     * Hold a cell with a month-index.
     */
    private static class MonthCellWithMonthIndex {
        public WeekCellDescriptor cell;
        public int monthIndex;

        public MonthCellWithMonthIndex(WeekCellDescriptor cell, int monthIndex) {
            this.cell = cell;
            this.monthIndex = monthIndex;
        }
    }

    private class CellClickedListener implements WeekView.Listener {
        public CellClickedListener() {
        }

        @Override
        public void handleClick(WeekCellDescriptor cell) {
            Date clickedDate = cell.getDate();
            if (!betweenDates(clickedDate, mMinCal, mMaxCal) || !isDateSelectable(clickedDate)) {
                if (mInvalidDateSelectedListener != null) {
                    mInvalidDateSelectedListener.onInvalidDateSelected(clickedDate);
                }
            } else {
                boolean wasSelected = doSelectDate(clickedDate, cell);

                if (mDateSelectedListener != null) {
                    if (wasSelected) {
                        mDateSelectedListener.onDateSelected(clickedDate);
                    } else {
                        mDateSelectedListener.onDateUnselected(clickedDate);
                    }
                }
            }
        }
    }

    public class FluentInitializer {
        /**
         * Override the {@link com.tripadvisor.CalendarView.SelectionMode} from the default ({@link com.tripadvisor.CalendarView.SelectionMode#SINGLE}).
         */
        public FluentInitializer inMode(SelectionMode mode) {
            mSelectionMode = mode;
            validateAndUpdate();
            return this;
        }

        /**
         * Set an initially-selected date.  The calendar will scroll to that date if it's not
         * already visible.
         */
        public FluentInitializer withSelectedDate(Date selectedDates) {
            return withSelectedDates(Arrays.asList(selectedDates));
        }

        /**
         * Set multiple selected dates.  This will throw an {@link IllegalArgumentException} if you
         * pass in multiple dates and haven't already called {@link #inMode(com.tripadvisor.CalendarView.SelectionMode)}.
         */
        public FluentInitializer withSelectedDates(Collection<Date> selectedDates) {
            if (mSelectionMode == SelectionMode.SINGLE && selectedDates.size() > 1) {
                throw new IllegalArgumentException("SINGLE mode can't be used with multiple " +
                        "selectedDates");
            }
            if (selectedDates != null) {
                for (Date date : selectedDates) {
                    selectDate(date);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    setMonthDisplayed(calendar);
                }
            }
            scrollToSelectedDates();

            validateAndUpdate();
            return this;
        }

        @SuppressWarnings("unused")
        public FluentInitializer displayOnly() {
            mDisplayOnly = true;
            return this;
        }
    }

    /**
     * <p> This is a specialized adapter for creating a list of mWeeks with selectable days. It can
     * be configured to display the week number, start the week on a given day, show a reduced
     * number of days, or display an arbitrary number of mWeeks at a time. </p>
     */
    private class WeeksAdapter extends BaseAdapter {

        private final LayoutInflater mInflater;
        private int mFocusedMonth;

        public WeeksAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return getWeeks().size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            WeekRowView weekView = (WeekRowView) convertView;
            if (weekView == null) {
                weekView = WeekRowView.create(parent, mInflater, getListener());
            }
            weekView.init(getWeeks().get(position), mCells.get(position), mDisplayOnly, mFocusedMonth);
            return weekView;
        }

        /**
         * Changes which month is in focus and updates the view.
         *
         * @param month The month to show as in focus [0-11]
         */
        public void setFocusMonth(int month) {
            if (mFocusedMonth == month) {
                return;
            }
            mFocusedMonth = month;
            notifyDataSetChanged();
        }

        public int getFocusedMonth() {
            return mFocusedMonth;
        }
    }

    private class DefaultOnInvalidDateSelectedListener implements OnInvalidDateSelectedListener {
        @Override
        public void onInvalidDateSelected(Date date) {
            String errMessage =
                    getResources().getString(R.string.invalid_date, mFullDateFormat.format(mMinCal
                            .getTime()),
                            mFullDateFormat.format(mMaxCal.getTime()));
            assert getContext() != null;
            Toast.makeText(getContext(), errMessage, Toast.LENGTH_SHORT).show();
        }
    }
}
