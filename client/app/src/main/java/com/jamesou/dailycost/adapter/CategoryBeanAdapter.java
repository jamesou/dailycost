package com.jamesou.dailycost.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jamesou.dailycost.R;
import com.jamesou.dailycost.db.CategoryBean;

import java.util.List;


public class CategoryBeanAdapter extends BaseAdapter {
    Context context;
    List<CategoryBean> categoryBeanList;

    public int getSelectPos() {
        return selectPos;
    }

    public void setSelectPos(int selectPos) {
        this.selectPos = selectPos;
    }

    int selectPos = 0; // selected index

    public CategoryBeanAdapter(Context context, List<CategoryBean> categoryBeanList) {
        this.context = context;
        this.categoryBeanList = categoryBeanList;
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
        //@todo need to adjust it while developing categories management
        convertView = LayoutInflater.from(context).inflate(R.layout.item_recordfrag_gv, parent, false);
        ImageView iv = convertView.findViewById(R.id.item_recordfrag_iv);
        TextView tv = convertView.findViewById(R.id.item_recordfrag_tv);

        CategoryBean categoryBean = categoryBeanList.get(position);
        tv.setText(categoryBean.getCategoryName());
        // selected show the image, else show grey
        if(selectPos == position){
            iv.setImageResource(categoryBean.getsImageId());
        }else{
            iv.setImageResource(categoryBean.getImageId());
        }
        return convertView;
    }
}
