package com.jamesou.dailycost;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.jamesou.dailycost.adapter.RecordEntryAdapter;
import com.jamesou.dailycost.db.CategoryBean;
import com.jamesou.dailycost.fragment.ExpenseCategoryFragment;
import com.jamesou.dailycost.fragment.IncomeCategoryFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jamesou on 13/05/2024
 * Describe:
 */
public class CategoryAddActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        init();
        initPage();
    }

    private void initPage() {
        List<Fragment> fragmentList = new ArrayList<>();
        ExpenseCategoryFragment  expenseCategoryFragment = new ExpenseCategoryFragment();
        IncomeCategoryFragment incomeCategoryFragment = new IncomeCategoryFragment();
        fragmentList.add(expenseCategoryFragment);
        fragmentList.add(incomeCategoryFragment);
        RecordEntryAdapter adapter = new RecordEntryAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        ArrayList<String> stringList = getIntent().getStringArrayListExtra("ListString");
        if(stringList != null&&stringList.size()!=0) {
            CategoryBean categoryBean = new CategoryBean();
            categoryBean.setId(Integer.parseInt(stringList.get(0)));
            categoryBean.setCategoryName(stringList.get(1));
            categoryBean.setImageId(Integer.parseInt(stringList.get(2)));
            categoryBean.setsImageId(Integer.parseInt(stringList.get(3)));
            categoryBean.setKind(Integer.parseInt(stringList.get(4)));
            if(categoryBean.getKind()==0) {
                expenseCategoryFragment.setCategoryBean(categoryBean);
            }else {
                incomeCategoryFragment.setCategoryBean(categoryBean);
                tabLayout.getTabAt(1).select();
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
