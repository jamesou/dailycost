package com.jamesou.dailycost.fragment;


import android.widget.Toast;

//import com.jamesou.dailycost.HistoryActivity;
import com.jamesou.dailycost.R;
import com.jamesou.dailycost.db.DBManager;
import com.jamesou.dailycost.db.CategoryBean;

import java.util.List;

public class IncomeFragment extends BaseRecordFragment {

    @Override
    public void loadDataToGridView(){
        super.loadDataToGridView();
        List<CategoryBean> incomeList = DBManager.getCategoryList(1);
        categoryBeanList.addAll(incomeList);
        categoryBeanAdapter.notifyDataSetChanged();
        if(!flag) {
            categoryTv.setText("Wage");
            categoryIv.setImageResource(R.mipmap.in_wage_fs);
        }else{
            categoryTv.setText(accountBean.getCategoryName());
            categoryIv.setImageResource(accountBean.getsImageId());
        }
    }

    @Override
    public void saveAccountToDb() {
        accountBean.setKind(1);
        if(!flag) {
            //if select nothing
            if(accountBean.getCategoryName()==null||accountBean.getCategoryName().trim().equals("")) {
                accountBean.setCategoryName("Wage");
                accountBean.setsImageId(R.mipmap.in_wage_fs);
            }
            DBManager.insertItemToAccounttb(accountBean);
        }else {
            DBManager.updateItemToAccounttb(accountBean);
        }
        Toast.makeText(getContext() , "Save successfully" , Toast.LENGTH_SHORT).show();
    }

}