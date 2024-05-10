package com.jamesou.dailycost.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.jamesou.dailycost.R;
import com.jamesou.dailycost.adapter.CalendarAdapter;
import com.jamesou.dailycost.db.DBManager;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class CalendarDialog extends Dialog implements View.OnClickListener {
    ImageView errorIv;
    GridView gv;
    LinearLayout yearLayout;
    List<TextView> yearTvList;
    List<Integer> yearList;
    CalendarAdapter adapter;

    int selectPos = -1;
    int selectMonth = -1;

    OnRefreshListener onRefreshListener;

    public CalendarDialog(@NonNull Context context , int selectPos , int selectMonth) {
        super(context);
        this.selectPos = selectPos;
        this.selectMonth = selectMonth;
    }

    public interface OnRefreshListener{
        public void Ensure(int selPos , int year , int month);
    }

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_calendar);
        errorIv = findViewById(R.id.dialog_calendar_error);
        gv = findViewById(R.id.dialog_calendar_gv);
        yearLayout = findViewById(R.id.dialog_calendar_layout);
        errorIv.setOnClickListener(this);

        addViewToLayout();
        initGridView();
        // set GridView click listener
        setGVListener();
    }

    private void setGVListener() {
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.selectPos = position;
                adapter.notifyDataSetChanged();;
                int month = position + 1;
                int year = adapter.year;
                //get selected date
                onRefreshListener.Ensure(selectPos , year , month);
                cancel();
            }
        });
    }

    private void initGridView() {
        int selectYear = yearList.get(selectPos);
        adapter = new CalendarAdapter(getContext(), selectYear);
        if (selectMonth == -1) {
            int month = Calendar.getInstance().get(Calendar.MONTH);
            adapter.selectPos = month;
        }else{
            adapter.selectPos = selectMonth - 1;
        }
        gv.setAdapter(adapter);
    }

    private void addViewToLayout() {
        yearTvList = new ArrayList<>();
        yearList = DBManager.yearListFromAccounttb();
        //if can't found year, add this year
        if (yearList.size() == 0) {
            int year = Calendar.getInstance().get(Calendar.YEAR);
            yearList.add(year);
        }
        for (int i = 0; i < yearList.size(); i++) {
            int year = yearList.get(i);
            View view = getLayoutInflater().inflate(R.layout.dialog_calendar_year, null);
            yearLayout.addView(view);
            TextView yearTv = view.findViewById(R.id.dialog_calendar_year_tv);
            yearTv.setText(year + "");
            yearTvList.add(yearTv);
        }
        // init it as latest year
        if(selectPos == -1){
            selectPos = yearTvList.size() - 1;
        }
        changTvBg(selectPos); // set default selected
        setHsvClickListener();
    }

    //set scrollview click listener
    private void setHsvClickListener() {
        for (int i = 0; i < yearTvList.size(); i++) {
            TextView view = yearTvList.get(i);
            int pos = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changTvBg(pos);
                    selectPos = pos;
                    //synchronised change the date gridview
                    int year = yearList.get(selectPos);
                    adapter.setYear(year);
                }
            });
        }
    }

    //change background
    private void changTvBg(int selectPos) {
        for (int i = 0; i < yearTvList.size(); i++) {
            TextView tv = yearTvList.get(i);
            tv.setBackgroundResource(R.drawable.dialog_btn_bg);
            tv.setTextColor(Color.BLACK);
        }
        TextView view = yearTvList.get(selectPos);
        view.setBackgroundResource(R.drawable.main_recordbtn_bg);
        view.setTextColor(Color.WHITE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dialog_calendar_error:
                cancel();
                break;
        }
    }
    //set Dialog size as same as the window size
    public void setDialogSize(){
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        Display d = window.getWindowManager().getDefaultDisplay();
        wlp.width = (int)d.getWidth();
        wlp.gravity = Gravity.TOP;
        window.setAttributes(wlp);
    }
}
