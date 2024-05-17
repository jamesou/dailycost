package com.jamesou.dailycost.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jamesou.dailycost.adapter.CategorySelectorAdater;
import com.jamesou.dailycost.db.CategoryBean;
import com.jamesou.dailycost.R;
import java.util.ArrayList;
import java.util.List;


public abstract class BaseCategoryFragment extends Fragment implements View.OnClickListener {
    EditText editText;
    ImageView imageView, categorySave;
    GridView gridView;
    List<CategoryBean> categoryBeanList;
    CategorySelectorAdater categorySelectorAdater;
    CategoryBean categoryBean;

    Boolean flag = false; //modify or add
    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!flag) {
            categoryBean = new CategoryBean();
            categoryBean.setCategoryName("Others");
            categoryBean.setsImageId(R.mipmap.ic_others_fs);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_category, container, false);
        initView(view);
        loadDataToGridView();
        getGridViewListener();
        return view;

    }

    private void getGridViewListener() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                categorySelectorAdater.setSelectPos(position);
                categorySelectorAdater.notifyDataSetChanged();
                CategoryBean category = categoryBeanList.get(position);
                int sImageId = category.getsImageId();
                imageView.setImageResource(sImageId);
                categoryBean.setImageId(category.getImageId());
                categoryBean.setsImageId(sImageId);
                categoryBean.setKind(category.getKind());
            }
        });
    }

    public void setCategoryBean(CategoryBean objbean){
        categoryBean = objbean;
        flag = true;
    }

    void loadDataToGridView() {
        categoryBeanList = new ArrayList<>();
        categorySelectorAdater = new CategorySelectorAdater(getContext(), categoryBeanList);
        gridView.setAdapter(categorySelectorAdater);
    }

    private void initView(View view) {
        editText = view.findViewById(R.id.frag_category_name);
        imageView = view.findViewById(R.id.frag_category_iv);
        gridView = view.findViewById(R.id.frag_category_gv);
        categorySave = view.findViewById(R.id.category_save);
        categorySave.setOnClickListener(this);

        if(flag) {
            imageView.setImageResource(categoryBean.getsImageId());
            editText.setText(categoryBean.getCategoryName());
        }

    }

    public abstract void saveCategoryToDb(String categoryName);

    @Override
    public void onClick(View v) {
        String typename = editText.getText().toString();
        switch (v.getId()){
            case R.id.category_save:
                saveCategoryToDb(typename);
                break;
        }
    }
}