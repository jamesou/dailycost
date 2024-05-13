package com.jamesou.dailycost.fragment;

import com.jamesou.dailycost.db.CategoryBean;
import com.jamesou.dailycost.db.DBManager;
import com.jamesou.dailycost.R;
import com.jamesou.dailycost.utils.PromptMsgUtil;

import java.util.List;

public class ExpenseCategoryFragment extends BaseCategoryFragment {

    @Override
    public void loadDataToGridView(){
        super.loadDataToGridView();
        List<CategoryBean> outList = DBManager.getNoneValueCategoryList(0);
        categoryBeanList.addAll(outList);
        categorySelectorAdater.notifyDataSetChanged();
        if(!flag) {
            imageView.setImageResource(R.mipmap.ic_others_fs);
        }
    }

    @Override
    public void saveCategoryToDb(String categoryName) {
        categoryBean.setKind(0);
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