package com.jamesou.dailycost.fragment;

import android.graphics.Color;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.jamesou.dailycost.db.BarChartItemBean;
import com.jamesou.dailycost.db.DBManager;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;


public class ExpenseChartFragment extends BaseChartFragment {
    int kind = 0;
    @Override
    public void onResume() {
        super.onResume();
        loaData(year , month , kind);
    }

    @Override
    protected void setAxisData(int year, int month) {
        List<IBarDataSet> sets = new ArrayList<>();
        if(isYear) {

            List<BarChartItemBean> list = DBManager.getBarChartListGroupByMonth(year, kind);
            if(list.size()==0){
                barChart.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
            }else{
                barChart.setVisibility(View.VISIBLE);
                textView.setVisibility(View.GONE);

                List<BarEntry> barEntries = new ArrayList<>();
                for(int i = 0 ; i < 12;i++){

                    BarEntry entry = new BarEntry(i, 0.0f);
                    barEntries.add(entry);
                }

                for (int i = 0; i < list.size(); i++) {
                    BarChartItemBean bean = list.get(i);
                    int beanMonth = bean.getMonth();
                    int index = beanMonth-1;
                    BarEntry entry = barEntries.get(index);
                    entry.setY(bean.getSumMoney());
                }
                BarDataSet barDataSet = new BarDataSet(barEntries, "");
                barDataSet.setValueTextColor(Color.BLACK);
                barDataSet.setValueTextSize(8f);
                barDataSet.setColor(Color.RED);


                barDataSet.setValueFormatter(new IValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {

                        if(value == 0){
                            return "";
                        }
                        return value + "";
                    }
                });
                sets.add(barDataSet);

                BarData barData = new BarData(sets);
                barData.setBarWidth(0.2f);
                barChart.setData(barData);
                barChart.invalidate();
            }
        }else {

            List<BarChartItemBean> list = DBManager.getBarChartListGroupByDay(year, month, kind);
            if(list.size()==0){
                barChart.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
            }else{
                barChart.setVisibility(View.VISIBLE);
                textView.setVisibility(View.GONE);

                List<BarEntry> barEntries = new ArrayList<>();
                for(int i = 0 ; i < 31;i++){

                    BarEntry entry = new BarEntry(i, 0.0f);
                    barEntries.add(entry);
                }

                for (int i = 0; i < list.size(); i++) {
                    BarChartItemBean bean = list.get(i);
                    int day = bean.getDay();
                    int index = day - 1;
                    BarEntry entry = barEntries.get(index);
                    entry.setY(bean.getSumMoney());
                }
                BarDataSet barDataSet1 = new BarDataSet(barEntries, "");
                barDataSet1.setValueTextColor(Color.BLACK);
                barDataSet1.setValueTextSize(8f);
                barDataSet1.setColor(Color.RED);


                barDataSet1.setValueFormatter(new IValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {

                        if(value == 0){
                            return "";
                        }
                        return value + "";
                    }
                });
                sets.add(barDataSet1);

                BarData barData = new BarData(sets);
                barData.setBarWidth(0.2f);
                barChart.setData(barData);
            }
        }

    }

    @Override
    protected void setYAxis(int year, int month) {

        float maxMoney = 0;
        if(isYear) {
            maxMoney = DBManager.getSumMoneyGroupByMonth(year, kind);
        }else {
            maxMoney = DBManager.getSumMoneyGroupByDay(year, month, kind);
        }
        float max = (float) Math.ceil(maxMoney);

        YAxis yAxis_right = barChart.getAxisRight();
        yAxis_right.setAxisMaximum(max);
        yAxis_right.setAxisMinimum(0f);
        yAxis_right.setEnabled(false);

        YAxis yAxis_left = barChart.getAxisLeft();
        yAxis_left.setAxisMaximum(max);
        yAxis_left.setAxisMinimum(0f);
        yAxis_left.setEnabled(false);


        Legend legend = barChart.getLegend();
        legend.setEnabled(false);
    }


    @Override
    public void setDate(int year, int month) {
        super.setDate(year, month);
        loaData(year , month , kind);
    }
}