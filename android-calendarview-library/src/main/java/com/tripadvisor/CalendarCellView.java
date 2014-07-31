// Copyright 2013 Square, Inc.

package com.tripadvisor;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import static com.tripadvisor.WeekCellDescriptor.RangeState;

public class CalendarCellView extends TextView {
    private static final int[] STATE_SELECTABLE = {
            R.attr.state_selectable
    };
    private static final int[] STATE_CURRENT_MONTH = {
            R.attr.state_current_month
    };
    private static final int[] STATE_TODAY = {
            R.attr.state_today
    };
    private static final int[] STATE_HIGHLIGHTED = {
            R.attr.state_highlighted
    };
    private static final int[] STATE_RANGE_FIRST = {
            R.attr.state_range_first
    };
    private static final int[] STATE_RANGE_MIDDLE = {
            R.attr.state_range_middle
    };
    private static final int[] STATE_RANGE_LAST = {
            R.attr.state_range_last
    };
    private static final int[] STATE_RANGE_FIRST_AND_LAST = {
            R.attr.state_range_first_and_last
    };
    private static final int[] STATE_RANGE_OPEN = {
            R.attr.state_range_open
    };

    private boolean isSelectable = false;
    private boolean isCurrentMonth = false;
    private boolean isToday = false;
    private boolean isHighlighted = false;
    private boolean isFirstAndLast = false;
    private RangeState rangeState = RangeState.NONE;

    @SuppressWarnings("UnusedDeclaration")
    public CalendarCellView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setSelectable(boolean isSelectable) {
        this.isSelectable = isSelectable;
        refreshDrawableState();
    }

    public void setCurrentMonth(boolean isCurrentMonth) {
        this.isCurrentMonth = isCurrentMonth;
        refreshDrawableState();
    }

    public void setToday(boolean isToday) {
        this.isToday = isToday;
        refreshDrawableState();
    }

    public void setRangeState(RangeState rangeState) {
        this.rangeState = rangeState;
        refreshDrawableState();
    }

    public void setHighlighted(boolean highlighted) {
        isHighlighted = highlighted;
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 5);

        if (isSelectable) {
            mergeDrawableStates(drawableState, STATE_SELECTABLE);
        }

        if (isCurrentMonth) {
            mergeDrawableStates(drawableState, STATE_CURRENT_MONTH);
        }

        if (isToday) {
            mergeDrawableStates(drawableState, STATE_TODAY);
        }

        if (isHighlighted) {
            mergeDrawableStates(drawableState, STATE_HIGHLIGHTED);
        }

        if (isFirstAndLast) {
            mergeDrawableStates(drawableState, STATE_RANGE_FIRST_AND_LAST);
        }

        if (rangeState == RangeState.FIRST) {
            mergeDrawableStates(drawableState, STATE_RANGE_FIRST);
        } else if (rangeState == RangeState.MIDDLE) {
            mergeDrawableStates(drawableState, STATE_RANGE_MIDDLE);
        } else if (rangeState == RangeState.LAST) {
            mergeDrawableStates(drawableState, STATE_RANGE_LAST);
        } else if (rangeState == RangeState.OPEN) {
            mergeDrawableStates(drawableState, STATE_RANGE_OPEN);
        }
        return drawableState;
    }
}
