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

import com.jamesou.dailycost.CategoryAddActivity;
import com.jamesou.dailycost.R;
import com.jamesou.dailycost.db.DBManager;
import com.jamesou.dailycost.db.CategoryBean;
import com.jamesou.dailycost.utils.PromptMsgUtil;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends BaseAdapter implements View.OnClickListener{
    Context context;
    List<CategoryBean> mDatas;
    LayoutInflater inflater;

    public CategoryAdapter(Context context, List<CategoryBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
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
            convertView = inflater.inflate(R.layout.item_categorylv , parent , false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        CategoryBean bean = mDatas.get(position);
        holder.typeIv.setImageResource(bean.getsImageId());
        holder.typeTv.setText(bean.getCategoryName());
        holder.typemvup.setOnClickListener(this);
        holder.typemvdown.setOnClickListener(this);
        holder.typeremove.setOnClickListener(this);
        holder.typeedit.setOnClickListener(this);
        holder.typemvup.setTag(position);
        holder.typemvdown.setTag(position);
        holder.typeremove.setTag(position);
        holder.typeedit.setTag(position);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        CategoryBean bean = mDatas.get(position);
        final int select_id = bean.getId();
        final int kind = bean.getKind();
        switch (v.getId()){
            case R.id.typebean_iv_mvup:
                String msg = DBManager.updateCategoryBeanById(select_id,kind,1,mDatas);
                if("first".equals(msg)) {
                    PromptMsgUtil.promptMsg(context,"This has been the first row already!");
                }else{
                    PromptMsgUtil.promptMsg(context,"Move Up successfully");
                }
                this.notifyDataSetChanged();
                break;
            case R.id.typebean_iv_mvdown:
                String msg2 = DBManager.updateCategoryBeanById(select_id,kind,0,mDatas);
                if("first".equals(msg2)) {
                    PromptMsgUtil.promptMsg(context,"This has been the last row already!");
                }else{
                    PromptMsgUtil.promptMsg(context,"Move Down successfully");
                }
                this.notifyDataSetChanged();
                break;
            case R.id.typebean_iv_eidt:
                ArrayList<String> stringList = new ArrayList<String>();
                stringList.add(bean.getId()+"");
                stringList.add(bean.getCategoryName()+"");
                stringList.add(bean.getImageId()+"");
                stringList.add(bean.getsImageId()+"");
                stringList.add(bean.getKind()+"");
                Intent intent = new Intent();
                intent.setClass(context, CategoryAddActivity.class);
                intent.putStringArrayListExtra("ListString", stringList);
                context.startActivity(intent);
                break;
            case R.id.typebean_iv_remove:
                delectItem(bean);
                break;
        }
    }


    private void delectItem(final CategoryBean bean) {
        final int deleci_id = bean.getId();
        new AlertDialog.Builder(context).setTitle("Prompt")
                .setMessage("Are you sure to del this row?")
                .setNegativeButton("Cancel" , null)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBManager.deleteCategoryById(deleci_id);
                        mDatas.remove(bean);
                        notifyDataSetChanged();
                    }
                }).show();
    }

    class ViewHolder{
        ImageView typeIv,typemvup,typemvdown,typeremove,typeedit;
        TextView typeTv;
        public ViewHolder(View view){
            typeIv = view.findViewById(R.id.item_typelv_iv);
            typemvup = view.findViewById(R.id.typebean_iv_mvup);
            typemvdown = view.findViewById(R.id.typebean_iv_mvdown);
            typeremove = view.findViewById(R.id.typebean_iv_remove);
            typeedit = view.findViewById(R.id.typebean_iv_eidt);
            typeTv = view.findViewById(R.id.item_typelv_tv_title);
        }
    }
}
