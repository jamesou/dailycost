package com.jamesou.dailycost;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.jamesou.dailycost.adapter.RecordPageAdapter;
import com.jamesou.dailycost.db.AccountBean;
import com.jamesou.dailycost.fragrecord.IncomeFragment;
import com.jamesou.dailycost.fragrecord.ExpenditureFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

//@todo refine
public class RecordActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        //init components
        init();
        initPage();
    }

    private void initPage() {
        List<Fragment> fragmentList = new ArrayList<>();

        ExpenditureFragment outcomeFragment = new ExpenditureFragment();
        IncomeFragment incomeFragment = new IncomeFragment();
        fragmentList.add(outcomeFragment);
        fragmentList.add(incomeFragment);


        RecordPageAdapter adapter = new RecordPageAdapter(getSupportFragmentManager(), fragmentList);

        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);

        ArrayList<String> stringList = (ArrayList<String>) getIntent().getStringArrayListExtra("ListString");
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
                outcomeFragment.setAccountBeanBean(accountBean);
                outcomeFragment.setDirection(stringList.get(10));
            }else {
                incomeFragment.setAccountBeanBean(accountBean);
                incomeFragment.setDirection(stringList.get(10));
                tabLayout.getTabAt(1).select();//默认选中第二个
                viewPager.setCurrentItem(1);
            }
        }
    }


    private void init() {
        tabLayout = findViewById(R.id.record_tabs);
        viewPager = findViewById(R.id.record_vp);
    }


    public void onClick(View v){
        switch (v.getId()){
            case R.id.record_iv_back:
                finish();
                break;
        }
    }
}