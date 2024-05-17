package com.jamesou.dailycost.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jamesou.dailycost.R;

import java.util.ArrayList;
import java.util.List;


public class CalendarAdapter extends BaseAdapter {
    Context context;
    List<String> mData;
    public int year;
    public int selectPos = -1;

    public void setYear(int year) {
        this.year = year;
        mData.clear();
        loadData(year);
        notifyDataSetChanged();
    }

    public CalendarAdapter(Context context, int year) {
        this.context = context;
        this.year = year;
        mData = new ArrayList<>();
        loadData(year);
    }

    private void loadData(int year) {
        //add month
        for(int i = 1 ; i < 13 ; i++){
            String data = year + "/" + i;
            mData.add(data);
        }
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_dialog_gv , parent, false);
        TextView view = convertView.findViewById(R.id.item_dialog_gv_tv);
        view.setText(mData.get(position));
        view.setBackgroundResource(R.color.grey_f3f3f3);
        view.setTextColor(Color.BLACK);
        if(selectPos == position){
            view.setBackgroundResource(R.color.green_006400);
            view.setTextColor(Color.WHITE);
        }
        return convertView;
    }
}
