package com.jamesou.dailycost.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.jamesou.dailycost.R;


public class SelectTimeDialog extends Dialog implements OnClickListener {
    EditText et_hour , et_minute;
    DatePicker datePicker;
    Button btn_cancel , btn_ensure;
    OnEnsureListener onEnsureListener;
    String timeStr;
    public interface OnEnsureListener{
        public void onEnsure(String time , int year , int month , int day);
    }

    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    public SelectTimeDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_time);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        init();
        setSelectTime();
        btn_ensure.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        hideDatePickerHeader();
    }

    private void init() {
        et_hour = (EditText) findViewById(R.id.dialog_time_et_hour);
        et_minute = (EditText) findViewById(R.id.dialog_time_et_minute);
        datePicker = (DatePicker) findViewById(R.id.dialog_time_dp);
        btn_cancel = (Button) findViewById(R.id.dialog_time_btn_cancel);
        btn_ensure = (Button) findViewById(R.id.dialog_time_btn_ensure);
    }

    public void setTime(String timeStr){
        this.timeStr = timeStr;
    }

    private void setSelectTime(){
        if(timeStr!=null) {
            int year = Integer.parseInt(timeStr.substring(0,timeStr.indexOf("Year")));
            int month = Integer.parseInt(timeStr.substring(timeStr.indexOf("Year")+1,timeStr.indexOf("Month")));
            int day = Integer.parseInt(timeStr.substring(timeStr.indexOf("Month")+1,timeStr.indexOf("Day")));

            int hour = Integer.parseInt(timeStr.substring(timeStr.indexOf("Day")+1,timeStr.indexOf(":")).trim());
            int minute = Integer.parseInt(timeStr.substring(timeStr.indexOf(":")+1));

            datePicker.updateDate(year,month-1,day);
            et_hour.setText(String.format("%02d", hour));
            et_minute.setText(String.format("%02d", minute));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dialog_time_btn_cancel:
                cancel();
                break;
            case R.id.dialog_time_btn_ensure:
                int year = datePicker.getYear();
                int month = datePicker.getMonth() + 1;
                int dayOfMonth = datePicker.getDayOfMonth();
                String monthStr = String.valueOf(month);
                if(month < 10){
                    monthStr = "0" + month;
                }
                String dayStr = String.valueOf(dayOfMonth);
                if(dayOfMonth < 10){
                    dayStr = "0" + dayOfMonth;
                }

                String hourStr = et_hour.getText().toString();
                String minuteStr = et_minute.getText().toString();
                int hour = 0;
                if (!TextUtils.isEmpty(hourStr)) {
                    hour = Integer.parseInt(hourStr);
                    hour = hour % 24;
                }
                int minute = 0;
                if(!TextUtils.isEmpty(minuteStr)){
                    minute = Integer.parseInt(minuteStr);
                    minute = minute % 60;
                }
                hourStr = String.valueOf(hour);
                minuteStr = String.valueOf(minute);
                if(hour < 10){
                    hourStr = "0" + hourStr;
                }
                if(minute < 10){
                    minuteStr = "0" + minuteStr;
                }
                String timeFormat = year + "/" + monthStr + "/" + dayStr + " " + hourStr + ":" + minuteStr;

                if(onEnsureListener != null){
                    onEnsureListener.onEnsure(timeFormat , year , month , dayOfMonth);
                }
                cancel();
                break;
        }
    }

    public void hideDatePickerHeader(){
        ViewGroup rootView = (ViewGroup) datePicker.getChildAt(0);
        if (rootView == null) {
            return;
        }
        View headerView = rootView.getChildAt(0);
        if(headerView == null){
            return;
        }
        // 5.0+
        int headerId = getContext().getResources().getIdentifier("day_picker_selector_layout" , "id" , "android");
        if(headerId == headerView.getId()){
            headerView.setVisibility(View.GONE);    // hide it but conquer the space

            ViewGroup.LayoutParams params = rootView.getLayoutParams();
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            rootView.setLayoutParams(params);

            ViewGroup animator = (ViewGroup) rootView.getChildAt(1);
            ViewGroup.LayoutParams layoutParams = animator.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            animator.setLayoutParams(layoutParams);

            View child = animator.getChildAt(0);
            ViewGroup.LayoutParams layoutParams1 = child.getLayoutParams();
            layoutParams1.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            child.setLayoutParams(layoutParams1);
            return;
        }
         //6.0+
        headerId = getContext().getResources().getIdentifier("date_picker_header" , "id" , "android");
        if(headerId == headerView.getId()){
            headerView.setVisibility(View.GONE);
        }
    }
}
