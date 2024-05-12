package com.jamesou.dailycost;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jamesou.dailycost.adapter.AccountAdapter;
import com.jamesou.dailycost.db.AccountBean;
import com.jamesou.dailycost.db.DBManager;
import com.jamesou.dailycost.dialog.CalendarDialog;
import com.jamesou.dailycost.utils.PromptMsgUtil;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    EditText searchEt;
    ListView searchLv;
    TextView emptyTv;
    AccountAdapter adapter;
    List<AccountBean> mData;
    int dialogSelectPos = -1;
    int dialogSelectMonth = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        init();
        mData = new ArrayList<>();
        adapter = new AccountAdapter(this , mData);
        searchLv.setAdapter(adapter);
        //if no data, it would display empty
        searchLv.setEmptyView(emptyTv);
    }

    private void init() {
        searchEt = findViewById(R.id.search_et);
        searchLv =findViewById(R.id.search_lv);
        emptyTv = findViewById(R.id.search_tv_empty);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_iv_back:
                finish();
                break;
            case R.id.search_iv_sh:
                String mgs = searchEt.getText().toString().trim();
                if (TextUtils.isEmpty(mgs)) {
                    PromptMsgUtil.promptMsg(this , "Can't search empty context");
                    return;
                }
                List<AccountBean> beans = DBManager.getAccountListByComment(mgs);
                mData.clear();
                mData.addAll(beans);
                adapter.notifyDataSetChanged();
                break;
            case R.id.search_calendar:
                CalendarDialog dialog = new CalendarDialog(this , dialogSelectPos , dialogSelectMonth);
                dialog.show();
                dialog.setDialogSize();
                dialog.setOnRefreshListener(new CalendarDialog.OnRefreshListener() {
                    @Override
                    public void Ensure(int selPos, int year, int month) {
                        String mgs = searchEt.getText().toString().trim();
                        if (TextUtils.isEmpty(mgs)) {
                            PromptMsgUtil.promptMsg(SearchActivity.this , "Can't search empty context");
                            return;
                        }
                        List<AccountBean> beans = DBManager.getAccountListBySearchCondition(mgs,year,month);
                        mData.clear();
                        mData.addAll(beans);
                        adapter.notifyDataSetChanged();
                        dialogSelectPos = selPos;
                        dialogSelectMonth = month;
                    }
                });
        }
    }
}