package com.tripadvisor;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.oyorooms.DateStateDescriptor;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import static android.view.View.MeasureSpec.EXACTLY;
import static android.view.View.MeasureSpec.makeMeasureSpec;
import static android.view.View.OnClickListener;

/**
 * <p> This is a dynamic view for drawing a single week. It can be configured to display the week
 * number, start the week on a given day, or show a reduced number of days. It is intended for use
 * as a single view within a ListView. See {@link com.tripadvisor.CalendarView
 * .WeeksAdapter}
 * for usage. </p>
 */
public class WeekView extends LinearLayout implements OnClickListener {

    Listener mListener;
    private WeekDescriptor mWeekDescriptor;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy", Locale.US);
    private int mLastWeekDayMonth = -1;
    private int cellSize;

    public WeekView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(WeekDescriptor weekDescriptor, List<DateStateDescriptor> cells,
                     final boolean displayOnly, final int focusedMonth) {
        mWeekDescriptor = weekDescriptor;
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params) {
        child.setOnClickListener(this);
        return super.addViewInLayout(child, index, params);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        long start = System.currentTimeMillis();
        final int totalWidth = MeasureSpec.getSize(widthMeasureSpec);
        cellSize = totalWidth / 7;
        int cellWidthSpec = makeMeasureSpec(cellSize, EXACTLY);
        int cellHeightSpec = makeMeasureSpec((int) (cellSize * 1.1), EXACTLY);
        int rowHeight = 0;
        for (int c = 0, numChildren = getChildCount(); c < numChildren; c++) {
            final View child = getChildAt(c);
            child.measure(cellWidthSpec, cellHeightSpec);
            // The row height is the height of the tallest cell.
            if (child.getMeasuredHeight() > rowHeight) {
                rowHeight = child.getMeasuredHeight();
            }
        }
        final int widthWithPadding = totalWidth + getPaddingLeft() + getPaddingRight();
        final int heightWithPadding = (rowHeight + getPaddingTop() + getPaddingBottom());
        setMeasuredDimension(widthWithPadding, heightWithPadding);
        Logr.d("Row.onMeasure %d ms", System.currentTimeMillis() - start);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        long start = System.currentTimeMillis();
        int cellHeight = bottom - top;
        for (int c = 0, numChildren = getChildCount(); c < numChildren; c++) {
            final View child = getChildAt(c);
            int startX = c * cellSize;
            int endX = (c + 1) * cellSize;
            if (c == numChildren - 1) {
                endX = right;
            }
            child.layout(startX, 0, endX, cellHeight);
        }

        Logr.d("Row.onLayout %d ms", System.currentTimeMillis() - start);
    }

    @Override
    public void onClick(View v) {
        mListener.handleClick((DateStateDescriptor) v.getTag());
        Logr.d("onClick(View v):" + (DateStateDescriptor) v.getTag());
    }

    /**
     * Returns the month of the last day in this week
     *
     * @return The month the last day of this view is in
     */
    public int getMonthOfLastWeekDay() {
        return mLastWeekDayMonth;
    }

    public interface Listener {
        void handleClick(DateStateDescriptor cell);
    }
}
