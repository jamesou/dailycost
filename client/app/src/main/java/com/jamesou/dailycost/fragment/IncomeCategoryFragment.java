package com.jamesou.dailycost.fragment;


import com.jamesou.dailycost.db.CategoryBean;
import com.jamesou.dailycost.db.DBManager;
import com.jamesou.dailycost.R;
import com.jamesou.dailycost.utils.PromptMsgUtil;

import java.util.List;


public class IncomeCategoryFragment extends BaseCategoryFragment {

    @Override
    public void loadDataToGridView(){
        super.loadDataToGridView();
        List<CategoryBean> inList = DBManager.getNoneValueCategoryList(1);
        categoryBeanList.addAll(inList);
        categorySelectorAdater.notifyDataSetChanged();
        if(!flag) {
            imageView.setImageResource(R.mipmap.in_others_fs);
        }
    }

    @Override
    public void saveCategoryToDb(String categoryName) {
        categoryBean.setKind(1);
        categoryBean.setCategoryName(categoryName);
        if(!flag) {
            DBManager.insertCategorytb(categoryBean);
        }else {
            DBManager.updateCategorytb(categoryBean);
        }
        PromptMsgUtil.promptMsg(getContext() ,"Save Successfully" );
        this.getActivity().finish();
    }

}