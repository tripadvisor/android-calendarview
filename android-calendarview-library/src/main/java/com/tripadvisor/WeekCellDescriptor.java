// Copyright 2012 Square, Inc.

package com.tripadvisor;

import java.util.Date;

/**
 * Describes the state of a particular date cell in a {@link com.tripadvisor.WeekView}.
 */
public class WeekCellDescriptor {
    public enum RangeState {
        NONE, FIRST, MIDDLE, LAST, FIRST_AND_LAST, OPEN
    }

    private final Date date;
    private final int value;
    private boolean isCurrentMonth;
    private boolean isSelected;
    private final boolean isToday;
    private final boolean isSelectable;
    private boolean isHighlighted;
    private RangeState rangeState;
    private boolean isFirstDayOfTheMonth;
    private String month;

    public WeekCellDescriptor(Date date, boolean currentMonth, boolean selectable, boolean selected,
                       boolean today, boolean highlighted, int value, RangeState rangeState) {
        this.date = date;
        isCurrentMonth = currentMonth;
        isSelectable = selectable;
        isHighlighted = highlighted;
        isSelected = selected;
        isToday = today;
        this.value = value;
        this.rangeState = rangeState;
    }

    public Date getDate() {
        return date;
    }

    public boolean isCurrentMonth() {
        return isCurrentMonth;
    }

    public boolean isSelectable() {
        return isSelectable;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    boolean isHighlighted() {
        return isHighlighted;
    }

    void setHighlighted(boolean highlighted) {
        isHighlighted = highlighted;
    }

    public void setCurrentMonth(boolean isCurrentMonth) {
        this.isCurrentMonth = isCurrentMonth;
    }

    public boolean isToday() {
        return isToday;
    }

    public RangeState getRangeState() {
        return rangeState;
    }

    public void setRangeState(RangeState rangeState) {
        this.rangeState = rangeState;
    }

    public int getValue() {
        return value;
    }

    public boolean isFirstDayOfTheMonth() {
        return isFirstDayOfTheMonth;
    }

    public void setFirstDayOfTheMonth(boolean isFirstDayOfTheMonth) {
        this.isFirstDayOfTheMonth = isFirstDayOfTheMonth;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    @Override
    public String toString() {
        return "WeekCellDescriptor{"
                + "date="
                + date
                + ", value="
                + value
                + ", isSelected="
                + isSelected
                + ", isToday="
                + isToday
                + ", isSelectable="
                + isSelectable
                + ", isHighlighted="
                + isHighlighted
                + ", rangeState="
                + rangeState
                + '}';
    }
}
