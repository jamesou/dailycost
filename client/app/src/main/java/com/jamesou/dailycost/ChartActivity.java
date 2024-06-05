package com.jamesou.dailycost;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.jamesou.dailycost.adapter.ChartVpAdapter;
import com.jamesou.dailycost.db.DBManager;
import com.jamesou.dailycost.dialog.CalendarDialog;
import com.jamesou.dailycost.fragment.ExpenseChartFragment;
import com.jamesou.dailycost.fragment.IncomeChartFragment;
import com.jamesou.dailycost.utils.FormatNumberUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChartActivity extends AppCompatActivity {
    TextView dateTv,inTv,outTv;
    Switch selectSwitch;
    ViewPager chartVp;
    Button btn_out , btn_in;
    private int year;
    private int month;
    int selectPos = -1, selectMonth = -1;
    List<Fragment> chartFragList;
    private IncomeChartFragment incomeChartFragment;
    private ExpenseChartFragment expenseChartFragment;
    private ChartVpAdapter chartVpAdapter;

    private Boolean isYear = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        initView();
        initTime();
        initDatas(year , month);
        initFrag();
        setVPSelectListener();
    }
    private void initFrag() {
        chartFragList = new ArrayList<>();
        incomeChartFragment = new IncomeChartFragment();
        expenseChartFragment = new ExpenseChartFragment();
        // use bundle to pass params for fragment
        Bundle bundle = new Bundle();
        bundle.putInt("year" , year);
        bundle.putInt("month" , month);
        incomeChartFragment.setArguments(bundle);
        expenseChartFragment.setArguments(bundle);
        chartFragList.add(expenseChartFragment);
        chartFragList.add(incomeChartFragment);
        chartVpAdapter = new ChartVpAdapter(getSupportFragmentManager(), chartFragList);
        chartVp.setAdapter(chartVpAdapter);
    }


    private void initDatas(int year, int month) {
        float incomeSumMonthly = DBManager.getSumMoneyByMonth(year, month, 1);
        float expenseSumMonthly = DBManager.getSumMoneyByMonth(year, month, 0);
        int incomeCountMonthly = DBManager.getCountItemByMonth(year, month, 1);
        int expenseCountMonthly = DBManager.getCountItemByMonth(year, month, 0);
        dateTv.setText(year + "/" + month + " Cost");
        inTv.setText("Total [" + incomeCountMonthly + "]， $" + FormatNumberUtil.formatFloat(incomeSumMonthly));
        outTv.setText("Total [" + expenseCountMonthly + "]， $" + FormatNumberUtil.formatFloat(expenseSumMonthly));

    }

    private void initDatas(int year) {
        float incomeSumYearly = DBManager.getSumMoneyByYear(year, 1);
        float expenseSumYearly = DBManager.getSumMoneyByYear(year , 0);
        int incomeCountYearly = DBManager.getCountItemByYear(year, 1);
        int expenseCountYearly = DBManager.getCountItemByYear(year, 0);
        dateTv.setText(year + " Bill");
        inTv.setText("Total [" + incomeCountYearly + "]，$" + FormatNumberUtil.formatFloat(incomeSumYearly));
        outTv.setText("Total[" + expenseCountYearly + "]，$" + FormatNumberUtil.formatFloat(expenseSumYearly));

    }


    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
    }

    private void initView() {
        dateTv = findViewById(R.id.chart_tv_date);
        inTv = findViewById(R.id.chart_tv_income);
        outTv = findViewById(R.id.chart_tv_expense);
        chartVp = findViewById(R.id.chart_vp);
        btn_in = findViewById(R.id.chart_btn_income);
        btn_out = findViewById(R.id.chart_btn_expense);
        selectSwitch = findViewById(R.id.selectSwitch);
        selectSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    isYear = true;
                    initDatas(year);
                }else {
                    isYear = false;
                    initDatas(year, month);
                }
                incomeChartFragment.setYear(isYear);
                expenseChartFragment.setYear(isYear);
                incomeChartFragment.setDate(year , month);
                expenseChartFragment.setDate(year , month);
            }
        });
    }
    //horizontally scroll the chart
    private void setVPSelectListener() {
        chartVp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                setBtnStyle(position);
            }
        });
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.chart_iv_back:
                finish();
                break;
            case R.id.chart_iv_calendar:
                showCalendarDialog();
                break;
            case R.id.chart_btn_income:
                setBtnStyle(1);
                chartVp.setCurrentItem(1);
                break;
            case R.id.chart_btn_expense:
                setBtnStyle(0);
                chartVp.setCurrentItem(0);
                break;
        }
    }


    private void showCalendarDialog() {
        CalendarDialog dialog = new CalendarDialog(this, selectPos, selectMonth);
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnRefreshListener(new CalendarDialog.OnRefreshListener() {
            @Override
            public void Ensure(int selPos, int year, int month) {
                // set default selected data
                ChartActivity.this.selectPos = selPos;
                ChartActivity.this.selectMonth = month;
                if(isYear) {
                    initDatas(year);
                }else {
                    initDatas(year , month);
                }
                incomeChartFragment.setDate(year , month);
                expenseChartFragment.setDate(year , month);
            }
        });
    }


    private void setBtnStyle(int kind){
        if(kind == 0){
            btn_out.setBackgroundResource(R.drawable.main_recordbtn_bg);
            btn_out.setTextColor(Color.WHITE);
            btn_in.setBackgroundResource(R.drawable.dialog_btn_bg);
            btn_in.setTextColor(Color.BLACK);
        }else{
            btn_in.setBackgroundResource(R.drawable.main_recordbtn_bg);
            btn_in.setTextColor(Color.WHITE);
            btn_out.setBackgroundResource(R.drawable.dialog_btn_bg);
            btn_out.setTextColor(Color.BLACK);
        }
    }

}