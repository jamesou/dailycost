package com.jamesou.dailycost.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.jamesou.dailycost.R;
import com.jamesou.dailycost.adapter.ChartItemAdapter;
import com.jamesou.dailycost.db.ChartItemBean;
import com.jamesou.dailycost.db.DBManager;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseChartFragment extends Fragment {
    ListView charLv;
    int year;
    int month;
    List<ChartItemBean> mData;
    private ChartItemAdapter adapter;
    BarChart barChart;
    TextView textView;

    Boolean isYear = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_income_chart, container, false);
        charLv = view.findViewById(R.id.frag_income_lv);
        Bundle bundle = getArguments();
        year = bundle.getInt("year");
        month = bundle.getInt("month");
        mData = new ArrayList<>();
        adapter = new ChartItemAdapter(getContext(), mData);
        charLv.setAdapter(adapter);
        addLvHeaderView();
        return view;
    }

    private void addLvHeaderView() {
        View view = getLayoutInflater().inflate(R.layout.item_chartfrag_top, null);
        charLv.addHeaderView(view);
        barChart = view.findViewById(R.id.item_chartfrag_char);
        textView = view.findViewById(R.id.item_charfrag_top_tv);
        barChart.getDescription().setEnabled(false);
        barChart.setExtraOffsets(20,20,20,20);
        setAxis(year , month);
        setAxisData(year , month);
    }

    protected abstract void setAxisData(int year, int month);
    protected void setAxis(int year, int month) {
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        xAxis.setTextSize(12f);
        if(isYear) {
            xAxis.setLabelCount(12);
            xAxis.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    int val = (int) value;
                    return String.valueOf(val+1);
                }
            });
        }else {
            xAxis.setLabelCount(31);
            xAxis.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    int val = (int) value;
                    if(val == 0) {
                        return month + "-1";
                    }
                    if(val == 14){
                        return month + "-15";
                    }
                    if(month == 2){
                        if(val == 27){
                            return month + "-28";
                        }
                    }else if(month == 1 || month == 3 || month == 5 || month ==7
                            || month == 8 || month == 10 || month == 12){
                        if(val == 30){
                            return month + "-31";
                        }
                    }else if(month == 4 || month == 6 || month == 9 || month ==11){
                        if(val == 29){
                            return month + "-30";
                        }
                    }

                    return "";
                }
            });
        }

        xAxis.setYOffset(10);
        setYAxis(year , month);
    }
    protected abstract void setYAxis(int year , int month);

    public void setDate(int year , int month){
        this.year = year;
        this.month = month;
        barChart.clear();
        barChart.invalidate();
        setAxis(year,month);
        setAxisData(year,month);
    }

    public void loaData(int year , int month , int kind) {
        List<ChartItemBean> beanList = null;
        if(isYear) {
            beanList = DBManager.getChartListByYear(year, kind);
        }else {
            beanList = DBManager.getChartListByMonth(year, month, kind);
        }
        mData.clear();
        mData.addAll(beanList);
        adapter.notifyDataSetChanged();
    }

    public Boolean getYear() {
        return isYear;
    }

    public void setYear(Boolean year) {
        isYear = year;
    }
}