package com.jamesou.dailycost;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.jamesou.dailycost.adapter.CategoryAdapter;
import com.jamesou.dailycost.db.CategoryBean;
import com.jamesou.dailycost.db.DBManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jamesou on 13/05/2024
 * Describe:
 */
public class CategoryActivity extends AppCompatActivity implements View.OnClickListener {
    ListView categoryLv;
    List<CategoryBean> mData;
    CategoryAdapter adapter;
    ImageView categoryAdd;



    int dialogSelectPos = -1;
    int dialogSelectMonth = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        categoryLv = findViewById(R.id.category_lv);
        categoryAdd = findViewById(R.id.category_iv_add);
        mData = new ArrayList<>();
        adapter = new CategoryAdapter(this, mData);
        categoryLv.setAdapter(adapter);
        loadData();
        categoryAdd.setOnClickListener(this);
        adapter.notifyDataSetChanged();
    }

    private void loadData() {
        List<CategoryBean> list = DBManager.getCategoryList(-1);
        mData.clear();
        mData.addAll(list);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.category_iv_back:
                finish();
                break;
            case R.id.category_iv_add:
                Intent intent = new Intent();
                intent.setClass(this, CategoryAddActivity.class);
                this.startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //automatically refresh data
        loadData();
        adapter.notifyDataSetChanged();
    }
}