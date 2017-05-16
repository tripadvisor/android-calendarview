// Copyright 2013 Square, Inc.

package com.oyorooms;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.tripadvisor.R;

import static com.oyorooms.DateStateDescriptor.RangeState;

public class CalendarCellView extends AppCompatTextView {
    private static final int[] STATE_SELECTABLE = {
            R.attr.state_selectable
    };
    private static final int[] STATE_TODAY = {
            R.attr.state_today
    };
    private static final int[] STATE_RANGE_START = {
            R.attr.state_range_start
    };
    private static final int[] STATE_RANGE_END = {
            R.attr.state_range_end
    };
    private static final int[] STATE_RANGE_MIDDLE = {
            R.attr.state_range_middle
    };
    private static final int[] STATE_PREDEFINED_RANGE_START = {
            R.attr.state_pre_defined_range_start
    };
    private static final int[] STATE_PREDEFINED_RANGE_END = {
            R.attr.state_pre_defined_range_end
    };
    private static final int[] STATE_PRE_DEFINED_RANGE_MIDDLE = {
            R.attr.state_pre_defined_range_middle
    };

    private boolean isSelectable = false;
    private boolean isToday = false;

    private RangeState rangeState = RangeState.NONE;
    private RangeState predefinedRangeState = RangeState.NONE;

    public CalendarCellView(Context context) {
        super(context);
    }

    public CalendarCellView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setSelectable(boolean isSelectable) {
        this.isSelectable = isSelectable;
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

    public void setPredefinedRangeState(RangeState rangeState) {
        this.predefinedRangeState = rangeState;
        refreshDrawableState();
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 5);

        setClickable(isSelectable);

        if (isSelectable) {
            mergeDrawableStates(drawableState, STATE_SELECTABLE);
        }

        if (isToday) {
            mergeDrawableStates(drawableState, STATE_TODAY);
        }

        if (rangeState == RangeState.START) {
            mergeDrawableStates(drawableState, STATE_RANGE_START); //this.isPredefinedRange ? STATE_PREDEFINED_RANGE_START :
        } else if (rangeState == RangeState.MIDDLE) {
            mergeDrawableStates(drawableState, STATE_RANGE_MIDDLE);
        } else if (rangeState == RangeState.END) {
            mergeDrawableStates(drawableState, STATE_RANGE_END);
        }

        if (predefinedRangeState == RangeState.START){
            mergeDrawableStates(drawableState, STATE_PREDEFINED_RANGE_START); //this.isPredefinedRange ? STATE_PREDEFINED_RANGE_START :
        } else if (predefinedRangeState == RangeState.MIDDLE) {
            mergeDrawableStates(drawableState, STATE_PRE_DEFINED_RANGE_MIDDLE);
        } else if (predefinedRangeState == RangeState.END) {
            mergeDrawableStates(drawableState, STATE_PREDEFINED_RANGE_END);
        }

        return drawableState;
    }
}
