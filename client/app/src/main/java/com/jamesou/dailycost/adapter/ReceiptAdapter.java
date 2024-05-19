package com.jamesou.dailycost.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jamesou.dailycost.CategoryAddActivity;
import com.jamesou.dailycost.R;
import com.jamesou.dailycost.db.CategoryBean;
import com.jamesou.dailycost.db.DBManager;
import com.jamesou.dailycost.db.ReceiptBean;
import com.jamesou.dailycost.utils.PromptMsgUtil;

import java.util.ArrayList;
import java.util.List;

public class ReceiptAdapter extends BaseAdapter {
    private List<ReceiptBean> dataList;
    private  Context context;
    LayoutInflater inflater;

    public ReceiptAdapter(Context context, List<ReceiptBean> dataList) {
        this.dataList = dataList;
        this.context = context;
        inflater = LayoutInflater.from(context);

    }
    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_receipt, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ReceiptBean bean = dataList.get(position);
        holder.itemNameTv.setText(bean.getItem_name());
        holder.itemQtyTv.setText(bean.getItem_qty());
        holder.itemAmountUnitTv.setText(bean.getItem_amount_unit());
        holder.itemAmountTv.setText(bean.getItem_amount());

        return convertView;
    }

    class ViewHolder{

        TextView itemNameTv,itemQtyTv,itemAmountUnitTv, itemAmountTv;
        public ViewHolder(View view){
            itemNameTv = view.findViewById(R.id.item_name);
            itemQtyTv = view.findViewById(R.id.item_qty);
            itemAmountUnitTv = view.findViewById(R.id.item_amount_unit);
            itemAmountTv = view.findViewById(R.id.item_amount);
        }
    }
}
