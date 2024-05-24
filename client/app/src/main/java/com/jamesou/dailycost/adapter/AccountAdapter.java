package com.jamesou.dailycost.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jamesou.dailycost.R;
import com.jamesou.dailycost.SearchActivity;
import com.jamesou.dailycost.db.AccountBean;
import com.jamesou.dailycost.utils.FormatNumberUtil;

import java.util.Calendar;
import java.util.List;

public class AccountAdapter extends BaseAdapter {
    Context context;
    List<AccountBean> mDatas;

    LayoutInflater inflater;
    int year , month , day;

    public AccountAdapter(Context context, List<AccountBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        // load layout
        inflater = LayoutInflater.from(context);
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
//        System.out.println("mDatas:"+mDatas);
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_mainlv , parent , false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        AccountBean bean = mDatas.get(position);
        holder.typeIv.setImageResource(bean.getsImageId());
        holder.typeTv.setText(bean.getCategoryName());
        holder.commentTv.setText(bean.getComment());
        holder.moneyTv.setText("$ " + FormatNumberUtil.formatFloat(bean.getMoney()));
//        System.out.println("bean.getTime():"+bean.getTime());
//        System.out.println("bean.getYear():"+bean.getYear());
//        System.out.println("bean.getMonth():"+bean.getMonth());
//        System.out.println("bean.getDay():"+bean.getDay());
//        System.out.println("this:context:"+context);
        String[] timeArray = bean.getTime().split(" ");
        if (bean.getYear() == year && bean.getMonth() == month && bean.getDay() == day) {
            if(timeArray.length>1) {
                String time = timeArray[1];
                holder.timeTv.setText("Today " + time);
            }else {
                holder.timeTv.setText(bean.getTime());
            }
        }else{
            if(context instanceof SearchActivity) { //search page
                holder.timeTv.setText(bean.getTime());
            }else{
                if (timeArray.length > 1) {
                    String time = timeArray[1];
                    holder.timeTv.setText(time);
                }
            }
        }
        return convertView;
    }
    class ViewHolder{
        ImageView typeIv;
        TextView typeTv , commentTv , timeTv , moneyTv;
        public ViewHolder(View view){
            typeIv = view.findViewById(R.id.item_mainlv_iv);
            typeTv = view.findViewById(R.id.item_mainlv_tv_title);
            commentTv = view.findViewById(R.id.item_mainlv_tv_comment);
            timeTv = view.findViewById(R.id.item_mainlv_tv_time);
            moneyTv = view.findViewById(R.id.item_mainlv_tv_money);
        }
    }
}
