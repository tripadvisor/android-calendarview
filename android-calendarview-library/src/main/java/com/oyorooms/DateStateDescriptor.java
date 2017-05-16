package com.oyorooms;

public class DateStateDescriptor {

    public enum RangeState {
        NONE, START, MIDDLE, END
    }

    public static final int noCurrDateInMonth = 0;

    private final int day;
    private final int month;
    private final int year;
    private final boolean isToday;
    private final boolean isSelectable;
    private RangeState rangeState;
    private final boolean isCurrSelection;

    public DateStateDescriptor(int day, int month, int year, boolean today, boolean selectable,
                               RangeState rangeState, boolean isCurrSelection) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.isToday = today;
        this.isSelectable = selectable;
        this.rangeState = rangeState;
        this.isCurrSelection = isCurrSelection;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public boolean isToday() {
        return isToday;
    }

    public boolean isSelectable() {
        return isSelectable;
    }

    public RangeState getRangeState() {
        return rangeState;
    }

    public boolean isCurrSelection() {
        return isCurrSelection;
    }

    @Override
    public String toString() {
        return "DateStateDescriptor{"
                + "date="
                + day + "/" + month + "/" + year
                + ", isToday="
                + isToday
                + ", isSelectable="
                + isSelectable
                + ", rangeState="
                + rangeState
                + ", isCurrSelection="
                + isCurrSelection
                + '}';
    }
}
