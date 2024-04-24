package com.jamesou.dailycost.fragrecord;


import android.content.Intent;
import android.widget.Toast;

//import com.jamesou.dailycost.HistoryActivity;
import com.jamesou.dailycost.R;
import com.jamesou.dailycost.adapter.CategoryBeanAdapter;
import com.jamesou.dailycost.db.DBManager;
import com.jamesou.dailycost.db.CategoryBean;

import java.util.List;


public class ExpenditureFragment extends BaseRecordFragment {

    @Override
    public void loadDataToGridView(){
        super.loadDataToGridView();
        List<CategoryBean> expenditureList = DBManager.getCategoryList(0);
        categoryBeanList.addAll(expenditureList);
        categoryBeanAdapter.notifyDataSetChanged();
        if(!paramBean) {
            categoryTv.setText("Others");
            categoryIv.setImageResource(R.mipmap.ic_others_fs);
        }
    }

    @Override
    public void saveAccountToDb() {
        accountBean.setKind(0);
        if(!paramBean) {
            DBManager.insertItemToAccounttb(accountBean);
        }else {
            DBManager.updateItemToAccounttb(accountBean);
        }
        Toast.makeText(getContext() , "保存成功" , Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
//        if("histroy".equals(this.getDirection())) {
//            intent.setClass(this.getActivity(), HistoryActivity.class);
//        }else {
//            intent.setClass(this.getActivity(), MainActivity.class);
//        }
        this.getActivity().finish();
    }

}