package com.jamesou.dailycost;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.jamesou.dailycost.adapter.AccountAdapter;
import com.jamesou.dailycost.db.AccountBean;
import com.jamesou.dailycost.db.DBManager;
import com.jamesou.dailycost.dialog.CalendarDialog;
import com.joybar.librarycalendar.data.CalendarDate;
import com.joybar.librarycalendar.fragment.CalendarViewFragment;
import com.joybar.librarycalendar.fragment.CalendarViewPagerFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RecordActivity extends AppCompatActivity implements View.OnClickListener,
        CalendarViewPagerFragment.OnPageChangeListener,
        CalendarViewFragment.OnDateClickListener,
        CalendarViewFragment.OnDateCancelListener {
    ListView historyLv;
    TextView timeTv,sumTv;
    List<AccountBean> mData;
    AccountAdapter adapter;
    int year , month,day;

    int dialogSelectPos = -1;
    int dialogSelectMonth = -1;

    int MENU_UPDATE = 0;
    int MENU_DELETE = 1;

    private boolean isChoiceModelSingle = true;
    private List<CalendarDate> mListDate = new ArrayList<>();

    private CalendarViewPagerFragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_history);
        historyLv = findViewById(R.id.history_lv);
        timeTv = findViewById(R.id.history_tv_time);
        sumTv = findViewById(R.id.history_tv_sum);
        initFragment();
        initTime();
        timeTv.setText( year+ "/" +month);
        mData = new ArrayList<>();
        adapter = new AccountAdapter(this , mData);
        historyLv.setAdapter(adapter);
        //long press the item of list to show the menu invoke onCreateContextMenu and onContextItemSelected
        registerForContextMenu(historyLv);
    }

    private void initFragment(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tx = fm.beginTransaction();
        fragment = CalendarViewPagerFragment.newInstance(isChoiceModelSingle);
        tx.replace(R.id.fl_content, fragment);
        tx.commit();
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, MENU_UPDATE, 0, "Modify Record");
        menu.add(0, MENU_DELETE, 0, "Delete Record");
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo cmi=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        //selected item
        int posMenu=cmi.position;
        //getItem from adapter list
//        System.out.println("posMenu:"+posMenu);
        AccountBean bean=(AccountBean) adapter.getItem(posMenu);
        switch(item.getItemId()){
            case 0:
                ArrayList<String> stringList = new ArrayList<>();
                stringList.add(bean.getId()+"");
                stringList.add(bean.getCategoryName()+"");
                stringList.add(bean.getsImageId()+"");
                stringList.add(bean.getComment()==null?"":bean.getComment());
                stringList.add(bean.getMoney()+"");
                stringList.add(bean.getTime()+"");
                stringList.add(bean.getYear()+"");
                stringList.add(bean.getMonth()+"");
                stringList.add(bean.getDay()+"");
                stringList.add(bean.getKind()+"");
                stringList.add("recode");   //the jump direction of page
                Intent intent = new Intent();
                intent.setClass(this, NewEntryActivity.class);
                intent.putStringArrayListExtra("RecordInfo", stringList);
                startActivity(intent);
                break;
            case 1:
                showDeleteItemDialog(bean);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onDateClick(CalendarDate calendarDate) {
        year = calendarDate.getSolar().solarYear;
        month = calendarDate.getSolar().solarMonth;
        day = calendarDate.getSolar().solarDay;
        timeTv.setText(year + "/" +month + "/"+day);
        if(TextUtils.isEmpty(calendarDate.getOutcome())&&TextUtils.isEmpty(calendarDate.getIncome())){
            sumTv.setText("");
        }else {
            String outcomStr = calendarDate.getOutcome().replaceFirst("[-+]","");
            String incomStr = calendarDate.getIncome().replaceFirst("[-+]","");
            if(outcomStr.equals("")) {
                outcomStr = "0";
            }
            if(incomStr.equals("")) {
                incomStr = "0";
            }
            sumTv.setText("expense:"+outcomStr+"   income:"+incomStr);
        }
        if (isChoiceModelSingle) {
            loadData(year,month,day);
        } else {
            //System.out.println(calendarDate.getSolar().solarDay);
            mListDate.add(calendarDate);
        }
    }

    @Override
    public void onDateCancel(CalendarDate calendarDate) {
        int count = mListDate.size();
        for (int i = 0; i < count; i++) {
            CalendarDate date = mListDate.get(i);
            if (date.getSolar().solarDay == calendarDate.getSolar().solarDay) {
                mListDate.remove(i);
                break;
            }
        }
    }

    @Override
    public void onPageChange(int year, int month) {
        timeTv.setText(year + "/" + month);
        mListDate.clear();
        sumTv.setText("");
    }

    private static String listToString(List<CalendarDate> list) {
        StringBuffer stringBuffer = new StringBuffer();
        for (CalendarDate date : list) {
            stringBuffer.append(date.getSolar().solarYear + "-" + date.getSolar().solarMonth + "-" + date.getSolar().solarDay).append(" ");
        }
        return stringBuffer.toString();
    }


    private void showDeleteItemDialog(final AccountBean clickBean) {
        new AlertDialog.Builder(this).setTitle("Message").setMessage("Confirm deletion?")
                .setNegativeButton("Cancel" , null)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int click_id = clickBean.getId();
                        //Delete record in local database
                        DBManager.deleteRecordById(click_id);
                        mData.remove(clickBean);
                        adapter.notifyDataSetChanged(); // refresh list view
                    }
                }).show();
    }


    private void loadData(int year , int month,int day) {
        List<AccountBean> list = DBManager.getAccountListByDate(year, month,day);
        mData.clear();
        mData.addAll(list);
        adapter.notifyDataSetChanged();
    }

    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData(year , month,day);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.history_iv_back:
                finish();
                break;
            case R.id.history_iv_calender:
                CalendarDialog dialog = new CalendarDialog(this , dialogSelectPos , dialogSelectMonth);
                dialog.show();
                dialog.setDialogSize();
                //click calendar image to change month, the activity need to be refresh
                dialog.setOnRefreshListener(new CalendarDialog.OnRefreshListener() {
                    @Override
                    public void Ensure(int selPos, int year, int month) {
                        timeTv.setText(year + "/" + month );
                        dialogSelectPos = selPos;
                        dialogSelectMonth = month;
                        fragment.setCurrentItem(year,month);
                        mData.clear();
                        adapter.notifyDataSetChanged();
                        historyLv.setAdapter(adapter);
                    }
                });
                break;
        }
    }
}