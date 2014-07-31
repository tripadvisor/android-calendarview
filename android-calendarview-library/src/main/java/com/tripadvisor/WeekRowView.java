package com.tripadvisor;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import static android.content.res.Resources.NotFoundException;
import static com.tripadvisor.WeekView.Listener;

/**
 * Created by Kurry Tran Email: ktran@tripadvisor.com Date: 3/4/14
 */
public class WeekRowView extends RelativeLayout {

    private Listener mListener;
    private WeekDescriptor mWeekDescriptor;
    private int mMonthOfFirstWeekDay = -1;
    private int mLastWeekDayMonth = -1;
    WeekView mWeekView;
    CalendarRowView mHeaderView;
    CalendarRowView mFooterView;

    public WeekRowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static WeekRowView create(ViewGroup parent, LayoutInflater inflater,
                                     Listener listener) {
        final WeekRowView view = (WeekRowView) inflater.inflate(R.layout.week_row_view, parent,
                false);
        view.mListener = listener;
        return view;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mWeekView = (WeekView) findViewById(R.id.week_view);
        mHeaderView = (CalendarRowView) findViewById(R.id.header);
        mFooterView = (CalendarRowView) findViewById(R.id.footer);
    }

    @SuppressWarnings("ConstantConditions")
    public void init(final WeekDescriptor weekDescriptor, final List<WeekCellDescriptor> cells,
                     final boolean displayOnly, final int focusedMonth) {
        mWeekDescriptor = weekDescriptor;
        mMonthOfFirstWeekDay = weekDescriptor.getMonth();
        mWeekView.mListener = mListener;
        initHeader(cells, focusedMonth);
        mWeekView.init(weekDescriptor, cells, displayOnly, focusedMonth);
        mLastWeekDayMonth = mWeekView.getMonthOfLastWeekDay();
    }

    private void initHeader(List<WeekCellDescriptor> cells, int focusedMonth) {
        for (int i = 0; i < cells.size(); i++) {
            WeekCellDescriptor cell = cells.get(i);
            final TextView headerTextView = (TextView) mHeaderView.getChildAt(i);
            final TextView footerTextView = (TextView) mFooterView.getChildAt(i);

            if (cell.isFirstDayOfTheMonth()) {
                headerTextView.setText(cell.getMonth());
            } else {
                headerTextView.setText("");
            }
            int backgroundColor = Color.WHITE;
            try {
                if (cell.getDate().getMonth() == focusedMonth) {
                    backgroundColor = getResources().getColor(R.color
                            .calendar_active_month_bg);
                } else {
                    backgroundColor = getResources().getColor(R.color
                            .calendar_inactive_month_bg);
                }
            } catch (NotFoundException e) {
                Logr.d("Color Not Found:" + e.getLocalizedMessage());
            } finally {
                headerTextView.setBackgroundColor(backgroundColor);
                footerTextView.setBackgroundColor(backgroundColor);
            }
        }
    }

    public int getMonthOfFirstWeekDay() {
        return mMonthOfFirstWeekDay;
    }

    /**
     * Returns the first day in this view.
     *
     * @return The first day in the view.
     */
    public Calendar getFirstDay() {
        Calendar firstDay = Calendar.getInstance();
        firstDay.setTime(mWeekDescriptor.getDate());
        return firstDay;
    }

    /**
     * Returns the month of the last day in this week
     *
     * @return The month the last day of this view is in
     */
    public int getMonthOfLastWeekDay() {
        return mLastWeekDayMonth;
    }
}
