package com.jamesou.dailycost.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jamesou.dailycost.R;
import com.jamesou.dailycost.db.ChartItemBean;
import com.jamesou.dailycost.utils.FormatNumberUtil;

import java.util.List;



public class ChartItemAdapter extends BaseAdapter {
    Context context;
    List<ChartItemBean> mData;
    LayoutInflater inflater;

    public ChartItemAdapter(Context context, List<ChartItemBean> mData) {
        this.context = context;
        this.mData = mData;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView ==null){
            convertView = inflater.inflate(R.layout.item_chartfrag, parent , false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        ChartItemBean bean = mData.get(position);
        holder.iv.setImageResource(bean.getsImageId());
        holder.typeTv.setText(bean.getType());
        holder.ratioLv.setText(bean.getRatio());
        holder.totalTv.setText("$ " + FormatNumberUtil.formatFloat(bean.getTotalMoney()));
        return convertView;
    }
    class ViewHolder{
        TextView typeTv,ratioLv,totalTv;
        ImageView iv;
        public ViewHolder(View view) {
            typeTv = view.findViewById(R.id.item_chartfrag_tv_type);
            ratioLv = view.findViewById(R.id.item_chartfrag_tv_pert);
            totalTv = view.findViewById(R.id.item_chartfrag_tv_sum);
            iv = view.findViewById(R.id.item_chartfrag_iv);
        }
    }
}
