package com.jamesou.dailycost;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.jamesou.dailycost.adapter.RecordEntryAdapter;
import com.jamesou.dailycost.db.AccountBean;
import com.jamesou.dailycost.fragment.IncomeFragment;
import com.jamesou.dailycost.fragment.ExpenseFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class NewEntryActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);
        //init components
        initLayout();
        initPage();
    }

    private void initPage() {
        List<Fragment> fragmentList = new ArrayList<>();
        ExpenseFragment expenseFragment = new ExpenseFragment();
        IncomeFragment incomeFragment = new IncomeFragment();
        fragmentList.add(expenseFragment);
        fragmentList.add(incomeFragment);

        RecordEntryAdapter adapter = new RecordEntryAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        ArrayList<String> stringList = (ArrayList<String>) getIntent().getStringArrayListExtra("RecordInfo");
        //modify record
        if(stringList != null&&stringList.size()!=0) {
            AccountBean accountBean = new AccountBean();
            accountBean.setId(Integer.parseInt(stringList.get(0)));
            accountBean.setCategoryName(stringList.get(1));
            accountBean.setsImageId(Integer.parseInt(stringList.get(2)));
            accountBean.setComment(stringList.get(3));
            accountBean.setMoney(Float.parseFloat(stringList.get(4)));
            accountBean.setTime(stringList.get(5));
            accountBean.setYear(Integer.parseInt(stringList.get(6)));
            accountBean.setMonth(Integer.parseInt(stringList.get(7)));
            accountBean.setDay(Integer.parseInt(stringList.get(8)));
            accountBean.setKind(Integer.parseInt(stringList.get(9)));
            if(accountBean.getKind()==0) {
                expenseFragment.setAccountBeanBean(accountBean);
                expenseFragment.setDirection(stringList.get(10));
            }else {
                incomeFragment.setAccountBeanBean(accountBean);
                incomeFragment.setDirection(stringList.get(10));
                tabLayout.getTabAt(1).select();//choose tab
                viewPager.setCurrentItem(1);
            }
        }
    }


    private void initLayout() {
        tabLayout = findViewById(R.id.record_tabs);
        viewPager = findViewById(R.id.record_vp);
    }


    public void onClick(View v){
        switch (v.getId()){
            case R.id.record_iv_back:
//                Log.d("NewEntriActivity", "Click back icon");
                finish();
                break;
        }
    }
}