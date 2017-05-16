package com.oyorooms;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tripadvisor.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aneesha.bahukhandi on 15/05/17
 */

public class MonthAdapter extends RecyclerView.Adapter<MonthAdapter.MonthViewHolder>{

    static class MonthViewHolder extends RecyclerView.ViewHolder{

        private TextView mMonthName;
        private List<LinearLayout> mWeeks;
        private List<CalendarCellView> mDays;

        MonthViewHolder(View convertView){
            super(convertView);
            this.mMonthName = (TextView) convertView.findViewById(R.id.cv_month_name);
            this.mWeeks = new ArrayList<>();
            this.mDays = new ArrayList<>();
            initWeeks(convertView);
            initDaysWeek();
        }

        private void initWeeks(View convertView){
            this.mWeeks.add((LinearLayout) convertView.findViewById(R.id.week1));
            this.mWeeks.add((LinearLayout) convertView.findViewById(R.id.week2));
            this.mWeeks.add((LinearLayout) convertView.findViewById(R.id.week3));
            this.mWeeks.add((LinearLayout) convertView.findViewById(R.id.week4));
            this.mWeeks.add((LinearLayout) convertView.findViewById(R.id.week5));
            this.mWeeks.add((LinearLayout) convertView.findViewById(R.id.week6));
        }

        private void initDaysWeek(){
            for (LinearLayout view : mWeeks) {
                this.mDays.add((CalendarCellView)view.findViewById(R.id.day_1));
                this.mDays.add((CalendarCellView)view.findViewById(R.id.day_2));
                this.mDays.add((CalendarCellView)view.findViewById(R.id.day_3));
                this.mDays.add((CalendarCellView)view.findViewById(R.id.day_4));
                this.mDays.add((CalendarCellView)view.findViewById(R.id.day_5));
                this.mDays.add((CalendarCellView)view.findViewById(R.id.day_6));
                this.mDays.add((CalendarCellView)view.findViewById(R.id.day_7));
            }
        }

    }

    private Context mContext;
    private List<MonthDescriptor> mMonthsList;

    public MonthAdapter(Context context, List<MonthDescriptor> monthsList){
        this.mContext =context;
        this.mMonthsList = new ArrayList<>(monthsList);
    }

    @Override
    public MonthViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = ((LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.month_view, parent, false);
        return new MonthViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(MonthViewHolder holder, int position) {
        MonthDescriptor descriptor = mMonthsList.get(position);
        holder.mMonthName.setText(descriptor.getMonthName());

        //set days
        initDaysOfMonth(holder, descriptor);
    }

    @Override
    public int getItemCount() {
        return mMonthsList == null ? 0 : mMonthsList.size();
    }

    private void initDaysOfMonth(MonthViewHolder holder, MonthDescriptor descriptor){
        for(LinearLayout weekView : holder.mWeeks){
            weekView.setVisibility(View.VISIBLE);
        }

        int i = 0;
        for (; i < descriptor.getFirstDayOfMonthInWeek(); i++){
            holder.mDays.get(i).setVisibility(View.INVISIBLE);
        }

        int startDate = 1;

        for (i = descriptor.getFirstDayOfMonthInWeek(); i < holder.mDays.size(); i++){
            if (startDate <= descriptor.getDaysOfMonth()){
                holder.mDays.get(i).setVisibility(View.VISIBLE);
                setCalendarCellState(descriptor.getDateStateInfo()[startDate - 1], holder.mDays.get(i));
                holder.mDays.get(i).setText(String.valueOf(startDate++));
                if (startDate > descriptor.getDaysOfMonth()){ // true when startDate at prev step == last date of month
                    break;
                }
            }
        }

        if (++i < holder.mDays.size()){ //invisible if extra days is less than 7
            int diff = holder.mDays.size() - i;
            if (diff >= 7){
                diff = diff % 7;
                holder.mWeeks.get(holder.mWeeks.size() - 1).setVisibility(View.GONE);
            }
            while (diff-- > 0) {
                holder.mDays.get(i++).setVisibility(View.INVISIBLE);
            }
        }
    }

    private void setCalendarCellState(DateStateDescriptor state, CalendarCellView cell){
        Log.e("Date descriptor", state + "");
        cell.setRangeState(state.getRangeState());
        cell.setSelectable(state.isSelectable());
        cell.setToday(state.isToday());
    }

}
