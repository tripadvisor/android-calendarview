package com.oyorooms;

/**
 * Created by aneesha.bahukhandi on 15/05/17
 */

public class MonthDescriptor {

    private int daysOfMonth;
    private String monthName;
    private int firstDayOfMonthInWeek; //which day of week does the month start from
    private DateStateDescriptor[] dateStateInfo;


    public MonthDescriptor(int daysOfMonth, int firstDayOfMonthInWeek, String monthName, int currDate,
                           int month, int year){
        this.daysOfMonth = daysOfMonth;
        this.firstDayOfMonthInWeek = firstDayOfMonthInWeek;
        this.monthName = monthName;
        initStates(currDate, month, year);
    }

    private void initStates(int currDate, int month, int year){
        this.dateStateInfo = new DateStateDescriptor[this.daysOfMonth];
        int i = 0;
        if (currDate > DateStateDescriptor.noCurrDateInMonth){ //only certain dates are selectable
            for (; i < currDate - 1 && i < this.daysOfMonth; i++){
                this.dateStateInfo[i] = new DateStateDescriptor(i + 1, month, year, false, false, DateStateDescriptor.RangeState.NONE, true);
            }
            if (i < this.daysOfMonth) {
                this.dateStateInfo[i] = new DateStateDescriptor(++i, month, year, true, true, DateStateDescriptor.RangeState.NONE, true);
            }
        }
        //all dates are selectable
        for (; i < this.daysOfMonth; i++){
            this.dateStateInfo[i] = new DateStateDescriptor(i + 1, month, year, false, true, DateStateDescriptor.RangeState.NONE, true);
        }
    }

    public int getDaysOfMonth() {
        return daysOfMonth;
    }

    public String getMonthName() {
        return monthName;
    }

    public int getFirstDayOfMonthInWeek() {
        return firstDayOfMonthInWeek;
    }

    public DateStateDescriptor[] getDateStateInfo() {
        return dateStateInfo;
    }
}
