package com.jamesou.dailycost.fragrecord;


import android.content.Intent;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

//import com.jamesou.dailycost.HistoryActivity;
import com.jamesou.dailycost.MainActivity;
import com.jamesou.dailycost.R;
import com.jamesou.dailycost.adapter.CategoryBeanAdapter;
import com.jamesou.dailycost.db.DBManager;
import com.jamesou.dailycost.db.CategoryBean;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class IncomeFragment extends BaseRecordFragment {

    @Override
    public void loadDataToGridView(){
        super.loadDataToGridView();
        List<CategoryBean> incomeList = DBManager.getCategoryList(1);
        categoryBeanList.addAll(incomeList);
        categoryBeanAdapter.notifyDataSetChanged();
        if(!paramBean) {
            categoryTv.setText("其他");
            categoryIv.setImageResource(R.mipmap.in_others_fs);
        }
    }

    @Override
    public void saveAccountToDb() {
        accountBean.setKind(1);
        if(!paramBean) {
            DBManager.insertItemToAccounttb(accountBean);
        }else {
            DBManager.updateItemToAccounttb(accountBean);
        }
        Toast.makeText(getContext() , "Save successfully" , Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
//        if("histroy".equals(this.getDirection())) {
//            intent.setClass(this.getActivity(), HistoryActivity.class);
//        }else {
            intent.setClass(this.getActivity(), MainActivity.class);
//        }
        this.getActivity().finish();
    }

}