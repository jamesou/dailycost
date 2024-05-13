package com.jamesou.dailycost.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.jamesou.dailycost.R;
import com.jamesou.dailycost.db.CategoryBean;

import java.util.List;

/**
 * Created by jamesou on 14/05/2024
 * Describe:
 */
public class CategorySelectorAdater extends BaseAdapter {
    Context context;
    List<CategoryBean> categoryBeanList;

    public int getSelectPos() {
        return selectPos;
    }

    public void setSelectPos(int selectPos) {
        this.selectPos = selectPos;
    }

    int selectPos = 0;

    public CategorySelectorAdater(Context context, List<CategoryBean> typeBeanList) {
        this.context = context;
        this.categoryBeanList = typeBeanList;
    }

    @Override
    public int getCount() {
        return categoryBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_categoryfrag_gv, parent, false);
        ImageView iv = convertView.findViewById(R.id.item_categoryfrag_iv);
        CategoryBean categoryBean = categoryBeanList.get(position);
        //set selected category image
        if(selectPos == position){
            iv.setImageResource(categoryBean.getsImageId());
        }else{
            iv.setImageResource(categoryBean.getImageId());
        }
        return convertView;
    }
}