package com.jamesou.dailycost.fragment;

import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jamesou.dailycost.R;
import com.jamesou.dailycost.adapter.RecordCategoryAdapter;

import com.jamesou.dailycost.db.AccountBean;
import com.jamesou.dailycost.db.CategoryBean;
import com.jamesou.dailycost.dialog.CommentDialog;
import com.jamesou.dailycost.utils.FormatNumberUtil;
import com.jamesou.dailycost.utils.KeyBoardUtils;
import com.jamesou.dailycost.dialog.SelectTimeDialog;
import com.jamesou.dailycost.utils.PromptMsgUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * record module
 */
public abstract class BaseRecordFragment extends Fragment implements View.OnClickListener {
    KeyboardView keyboardView;
    EditText moneyEt;
    ImageView categoryIv;
    TextView categoryTv, comment , timeTv;
    GridView categoryGv;
    List<CategoryBean> categoryBeanList;// GridView data from Categories
    RecordCategoryAdapter categoryBeanAdapter;
    AccountBean accountBean;
    Boolean flag = false;  //determine modify or add
    private String direction = "";
    private String commentStr = null;
    private String timeStr = null;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_record, container, false);
        initView(view);
        loadDataToGridView();
        getGridViewListener();
        return view;
    }

    private void initTime() {
        accountBean = new AccountBean();
        String time = getCurrentDateTime();
        timeTv.setText(time);
        refreshTime(accountBean,time);
    }
    private void refreshTime(AccountBean accountBean,String time){
        accountBean.setTime(time);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        accountBean.setYear(year);
        accountBean.setMonth(month);
        accountBean.setDay(day);
    }
    private void refreshTime(AccountBean accountBean){
        String time = getCurrentDateTime();
        refreshTime(accountBean,time);
    }
    private String getCurrentDateTime(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        return sdf.format(date);
    }

    /*pick category icon*/
    private void getGridViewListener() {
        categoryGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                categoryBeanAdapter.setSelectPos(position);
                categoryBeanAdapter.notifyDataSetChanged();
                CategoryBean selectedCategoryBean = categoryBeanList.get(position);
                String categoryName = selectedCategoryBean.getCategoryName();
                Log.d("BaseRecordFragment","categoryName:"+categoryName);

                categoryTv.setText(categoryName);
                accountBean.setCategoryName(categoryName);
                int sImageId = selectedCategoryBean.getsImageId();
                categoryIv.setImageResource(sImageId);
                accountBean.setsImageId(sImageId);
            }
        });
    }

    public void setAccountBeanBean(AccountBean objbean){
        accountBean = objbean;
        flag = true;
    }

    void loadDataToGridView() {
        categoryBeanList = new ArrayList<>();
        categoryBeanAdapter = new RecordCategoryAdapter(getContext(), categoryBeanList);
        categoryGv.setAdapter(categoryBeanAdapter);
    }

    private void initView(View view) {
        keyboardView = (KeyboardView)(view).findViewById(R.id.frag_record_keyboard);
        moneyEt = view.findViewById(R.id.frag_record_et_money);
        categoryIv = view.findViewById(R.id.frag_record_iv);
        categoryGv = view.findViewById(R.id.frag_record_gv);
        categoryTv = view.findViewById(R.id.frag_record_tv_category);
        comment = view.findViewById(R.id.frag_record_tv_comment);
        timeTv = view.findViewById(R.id.frag_record_tv_time);
        comment.setOnClickListener(this);
        timeTv.setOnClickListener(this);

        if(flag) {
            categoryIv.setImageResource(accountBean.getsImageId());
            categoryTv.setText(accountBean.getCategoryName());
            moneyEt.setText(FormatNumberUtil.formatFloat(accountBean.getMoney()));
            moneyEt.setSelection(String.valueOf(FormatNumberUtil.formatFloat(accountBean.getMoney())).length());
            if(accountBean.getComment()!=null&&!accountBean.getComment().trim().equals("")) {
                comment.setText(accountBean.getComment());
                commentStr = accountBean.getComment();
            }
            timeStr = accountBean.getTime();
            timeTv.setText(accountBean.getTime());
        }else{
            initTime();
        }

        // Display defined keyboard
        KeyBoardUtils keyBoardUtils = new KeyBoardUtils(keyboardView, moneyEt);
        keyBoardUtils.showKeyboard();
        // register keyboard listener
        keyBoardUtils.setOnEnsureListener(new KeyBoardUtils.OnEnsureListener() {
            @Override
            public void onEnsure() {
                String moneyStr = moneyEt.getText().toString();
                if (TextUtils.isEmpty(moneyStr) ) {
                    PromptMsgUtil.promptMsg(getActivity(),"Amount can't be empty");
                    return;
                }else {
                    String regex = "[0]+";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(moneyStr);
                    if(matcher.matches()){
                        PromptMsgUtil.promptMsg(getActivity(),"Amount can't be zero");
                        return;
                    }
                }
                float money = Float.parseFloat(moneyStr);
                accountBean.setMoney(money);
                refreshTime(accountBean);
                saveAccountToDb();
                //destroy current activity and go to main activity
                getActivity().finish();
            }
        });
    }


    public abstract void saveAccountToDb();

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.frag_record_tv_time:
                showTimeDialog();
                break;
            case R.id.frag_record_tv_comment:
                showCommentDialog();
                break;
        }
    }

    /**
     * show datetime dialog
     */
    private void showTimeDialog(){
        SelectTimeDialog selectTimeDialog = new SelectTimeDialog(getContext());
        selectTimeDialog.setTime(timeStr);
        selectTimeDialog.show();
        selectTimeDialog.setOnEnsureListener(new SelectTimeDialog.OnEnsureListener() {
            @Override
            public void onEnsure(String time, int year, int month, int day) {
                timeTv.setText(time);
                accountBean.setTime(time);
                accountBean.setYear(year);
                accountBean.setMonth(month);
                accountBean.setDay(day);
            }
        });
    }

    public void showCommentDialog(){
        CommentDialog dialog = new CommentDialog(getContext());
        dialog.show();
        if(commentStr !=null) {
            dialog.setEditText(commentStr);
        }
        dialog.setDialogSize();
        dialog.setOnEnsureListener(new CommentDialog.onEnsureListener() {
            @Override
            public void onEnsure() {
                String msg = dialog.getEditText();
                if (!TextUtils.isEmpty(msg)) {
                    comment.setText(msg);
                    accountBean.setComment(msg);
                }
                dialog.cancel();
            }
        });
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}