package com.oyorooms;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.tripadvisor.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by aneesha.bahukhandi on 15/05/17
 */

public class CalendarView extends LinearLayout {

    private Calendar mToday;
    private LinearLayout mParentLayout;
    private RecyclerView mMonthsList;
    private List<MonthDescriptor> mMonthDescriptorsList;
    private SimpleDateFormat mFullMonthNameFormat;
    private MonthAdapter mMonthsAdapter;

    private int numberOfPreviousMonths;
    private int numberOfFutureMonths;

    private static final int NA = 0;
    private static final int rotation = 1;

    public CalendarView(Context context) {
        super(context);
        initData();
        initView();
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.numberOfPreviousMonths = attrs != null ? attrs.getAttributeIntValue(R.attr.prev_months, NA) : NA;
        this.numberOfFutureMonths = attrs != null ? attrs.getAttributeIntValue(R.attr.next_months, NA) : NA;
        initData();
        initView();
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.numberOfPreviousMonths = attrs != null ? attrs.getAttributeIntValue(R.attr.prev_months, NA) : NA;
        this.numberOfFutureMonths = attrs != null ? attrs.getAttributeIntValue(R.attr.next_months, NA) : NA;
        initData();
        initView();
    }

    private void initData(){
        this.mToday = Calendar.getInstance();
        this.mMonthDescriptorsList = new ArrayList<>();
        this.mFullMonthNameFormat = new SimpleDateFormat(getContext().getString(R.string
                .header_month_name_format), Locale.getDefault());
        initMonths();
    }

    private void initMonths(){
        Calendar calculationCalendar = Calendar.getInstance();
        int startMonth = mToday.get(Calendar.MONTH);
        if (this.numberOfPreviousMonths > NA){
            int currMonth = startMonth;
            startMonth = currMonth - this.numberOfPreviousMonths + 1;
            while (startMonth < 0){
                calculationCalendar.set(Calendar.YEAR, mToday.get(Calendar.YEAR) - 1);
                startMonth += 12;
            }
        }
        calculationCalendar.set(Calendar.MONTH, startMonth);
        calculationCalendar.set(Calendar.DAY_OF_MONTH, 1);
        while (calculationCalendar.get(Calendar.MONTH) != mToday.get(Calendar.MONTH)
                || calculationCalendar.get(Calendar.YEAR) != mToday.get(Calendar.YEAR)){
            this.mMonthDescriptorsList.add(getNewMonthDescriptorForMonth(calculationCalendar));
            if (calculationCalendar.get(Calendar.MONTH) == Calendar.DECEMBER){
                calculationCalendar.set(Calendar.YEAR, calculationCalendar.get(Calendar.YEAR) + 1);
                calculationCalendar.set(Calendar.MONTH, Calendar.JANUARY);
            } else {
                calculationCalendar.set(Calendar.MONTH, calculationCalendar.get(Calendar.MONTH) + 1);
            }
        }
        this.mMonthDescriptorsList.add(getNewMonthDescriptorForMonth(calculationCalendar));
        if (this.numberOfFutureMonths > NA){
            int i = 0;
            int currMonth = mToday.get(Calendar.MONTH);
            while (++i <= this.numberOfFutureMonths){
                currMonth++;
                if (currMonth > Calendar.DECEMBER){
                    currMonth = Calendar.JANUARY;
                    calculationCalendar.set(Calendar.YEAR, calculationCalendar.get(Calendar.YEAR) + 1);
                }
                calculationCalendar.set(Calendar.MONTH, currMonth);
                this.mMonthDescriptorsList.add(getNewMonthDescriptorForMonth(calculationCalendar));
            }
        }
    }

    private MonthDescriptor getNewMonthDescriptorForMonth(Calendar calendar){
        String monthName = mFullMonthNameFormat.format(calendar.getTime());
        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1 - rotation; //SUN - SAT :: 1 - 7 in Java
        if (firstDayOfWeek < 0) {
            firstDayOfWeek += 7;
        }
        int daysCount = getNumberOfDays(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
        int currDate = mToday.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) && mToday.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) ?
                        mToday.get(Calendar.DAY_OF_MONTH) : DateStateDescriptor.noCurrDateInMonth;
        if (currDate == DateStateDescriptor.noCurrDateInMonth){
            if (calendar.compareTo(mToday) < 0){  //for prev months everything is unselectable. So currDate == daysCount
                currDate = daysCount;
            }
            //for future months everything is selectable. So currDate == 0
        }
        return new MonthDescriptor(daysCount, firstDayOfWeek, monthName, currDate,
                calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
    }

    private int getNumberOfDays(int month, int year){
        switch(month){
            case Calendar.JANUARY:
            case Calendar.MARCH:
            case Calendar.MAY:
            case Calendar.JULY:
            case Calendar.AUGUST:
            case Calendar.OCTOBER:
            case Calendar.DECEMBER:
                return 31;
            case Calendar.FEBRUARY:
                if (year % 100 == 0){
                    return  year % 4 == 0? 29 : 28;
                }
                return  year % 4 == 0? 29 : 28;
            case Calendar.APRIL:
            case Calendar.JUNE:
            case Calendar.SEPTEMBER:
            case Calendar.NOVEMBER:
            default:
                return 30;
        }
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mParentLayout = (LinearLayout) inflater.inflate(R.layout.calendar_view, this, true);
        this.mMonthsList = (RecyclerView) mParentLayout.findViewById(R.id.rv_months_list);
        this.mMonthsList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        //attach adapter
        this.mMonthsAdapter = new MonthAdapter(getContext(), this.mMonthDescriptorsList);
        this.mMonthsList.setAdapter(this.mMonthsAdapter);
    }
}
