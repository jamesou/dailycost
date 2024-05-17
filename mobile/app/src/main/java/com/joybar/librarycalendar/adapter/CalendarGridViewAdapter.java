package com.joybar.librarycalendar.adapter;


import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.joybar.librarycalendar.data.CalendarDate;

import java.util.ArrayList;
import java.util.List;
import com.jamesou.dailycost.R;

/**
 * Created by joybar on 2/24/16.
 * Modified by jamesou on 11/05/2024--add income and expense to display on the calendar
 */
public class CalendarGridViewAdapter extends BaseAdapter {

    private List<CalendarDate> mListData = new ArrayList<>();


    public CalendarGridViewAdapter(List<CalendarDate> mListData) {
        this.mListData = mListData;
    }

    public List<CalendarDate> getListData() {
        return mListData;
    }


    public int getCount() {
        return mListData.size();
    }


    public Object getItem(int position) {
        return position;
    }



    public long getItemId(int position) {
        return position;
    }


    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder viewHolder = null;
        CalendarDate calendarDate = mListData.get(position);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_calendar, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_day.setText(calendarDate.getSolar().solarDay+"");

        String str;

        if(!TextUtils.isEmpty(calendarDate.getSolar().solar24Term)){
            str =  calendarDate.getSolar().solar24Term;
        }else if(!TextUtils.isEmpty(calendarDate.getSolar().solarFestivalName)){
            str = calendarDate.getSolar().solarFestivalName;
        }else if(!TextUtils.isEmpty(calendarDate.getLunar().lunarFestivalName)){
            str = calendarDate.getLunar().lunarFestivalName;
        }else{
            str = calendarDate.getLunar().getChinaDayString(mListData.get(position).getLunar().lunarDay,mListData.get(position).getLunar().lunarMonth);
        }
        if(mListData.get(position).isInThisMonth()){
            viewHolder.tv_day.setTextColor(Color.parseColor("#000000"));
        }else{
            viewHolder.tv_day.setTextColor(Color.parseColor("#D7D7D7"));

        }
        viewHolder.tv_expense.setText(calendarDate.getOutcome());
        viewHolder.tv_income.setText(calendarDate.getIncome());
        return convertView;
    }


    public static class ViewHolder {
        private TextView tv_day;
        private TextView tv_expense,tv_income;
        public ViewHolder(View itemView) {
            tv_day = (TextView) itemView.findViewById(R.id.tv_day);
            tv_expense = (TextView) itemView.findViewById(R.id.tv_expense);
            tv_income = (TextView) itemView.findViewById(R.id.tv_income);
        }

    }



}

