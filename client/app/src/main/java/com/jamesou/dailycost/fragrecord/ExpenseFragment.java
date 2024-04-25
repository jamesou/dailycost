package com.jamesou.dailycost.fragrecord;


//import com.jamesou.dailycost.HistoryActivity;
import android.util.Log;

import com.jamesou.dailycost.R;
        import com.jamesou.dailycost.db.DBManager;
import com.jamesou.dailycost.db.CategoryBean;
import com.jamesou.dailycost.utils.PromptMsgUtil;

        import java.util.List;


public class ExpenseFragment extends BaseRecordFragment {

    @Override
    public void loadDataToGridView(){
        super.loadDataToGridView();
        List<CategoryBean> expenseList = DBManager.getCategoryList(0);
        categoryBeanList.addAll(expenseList);
        categoryBeanAdapter.notifyDataSetChanged();
        if(!paramBean) {
            //new entry
            categoryTv.setText("Catering");
            categoryIv.setImageResource(R.mipmap.ic_catering_fs);
        }else{
            //modify record
            categoryTv.setText(accountBean.getCategoryName());
            categoryIv.setImageResource(accountBean.getsImageId());
        }
    }

    @Override
    public void saveAccountToDb() {
        accountBean.setKind(0);
        if(!paramBean) {
            Log.d("ExpenseFragment","accountBean.getCategoryName():"+accountBean.getCategoryName());
            //if select nothing
            if(accountBean.getCategoryName()==null||accountBean.getCategoryName().trim().equals("")) {
                accountBean.setCategoryName("Catering");
                accountBean.setsImageId(R.mipmap.ic_catering_fs);
            }
            DBManager.insertItemToAccounttb(accountBean);
        }else {
            DBManager.updateItemToAccounttb(accountBean);
        }
        PromptMsgUtil.promptMsg(getContext() , "Save successfully");
    }

}