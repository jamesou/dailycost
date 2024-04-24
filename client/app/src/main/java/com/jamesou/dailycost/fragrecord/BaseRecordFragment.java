package com.jamesou.dailycost.fragrecord;

import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jamesou.dailycost.R;
import com.jamesou.dailycost.adapter.CategoryBeanAdapter;

import com.jamesou.dailycost.db.AccountBean;
import com.jamesou.dailycost.db.CategoryBean;
import com.jamesou.dailycost.dialog.CommentDialog;
import com.jamesou.dailycost.utils.FormatNumberUtil;
import com.jamesou.dailycost.utils.KeyBoardUtils;
import com.jamesou.dailycost.dialog.SelectTimeDialog;

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
    List<CategoryBean> categoryBeanList;// GridView 的数据源
    CategoryBeanAdapter categoryBeanAdapter;
    AccountBean accountBean;//将需要插入到记账本中的数据保存成对象的形式
    Boolean paramBean = false;  //判断是否是值传递过来了
    private String direction = "";
    private String commentStr = null;
    private String timeStr = null;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!paramBean) {
            accountBean = new AccountBean();
            accountBean.setCategoryName("Others");
            accountBean.setsImageId(R.mipmap.ic_others_fs);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_expenditure, container, false);
        initView(view);
        loadDataToGridView();
        getGridViewListener();
        if(!paramBean) {
            setInitTime();
        }
        return view;

    }

    private void setInitTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String time = sdf.format(date);
        timeTv.setText(time);
        accountBean.setTime(time);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        accountBean.setYear(year);
        accountBean.setMonth(month);
        accountBean.setDay(day);
    }

    /*获取GV的点击事件*/
    private void getGridViewListener() {
        categoryGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                categoryBeanAdapter.setSelectPos(position);
                categoryBeanAdapter.notifyDataSetChanged(); //显示绘制
                CategoryBean categoryBean = categoryBeanList.get(position);
                String typeName = categoryBean.getCategoryName();
                categoryTv.setText(typeName);
                accountBean.setCategoryName(typeName);
                int sImageId = categoryBean.getsImageId();
                categoryIv.setImageResource(sImageId);
                accountBean.setsImageId(sImageId);
            }
        });
    }

    public void setAccountBeanBean(AccountBean objbean){
        accountBean = objbean;
        paramBean = true;
    }

    void loadDataToGridView() {
        categoryBeanList = new ArrayList<>();
        categoryBeanAdapter = new CategoryBeanAdapter(getContext(), categoryBeanList);
        categoryGv.setAdapter(categoryBeanAdapter);

    }

    private void initView(View view) {
        keyboardView = (KeyboardView)(view).findViewById(R.id.frag_record_keyboard);
        moneyEt = view.findViewById(R.id.frag_record_et_money);
        categoryIv = view.findViewById(R.id.frag_record_iv);
        categoryGv = view.findViewById(R.id.frag_record_gv);
        categoryTv = view.findViewById(R.id.frag_record_tv_type);
        comment = view.findViewById(R.id.frag_record_tv_comment);
        timeTv = view.findViewById(R.id.frag_record_tv_time);
        comment.setOnClickListener(this);
        timeTv.setOnClickListener(this);

        if(paramBean) {
            categoryIv.setImageResource(accountBean.getsImageId());
            categoryTv.setText(accountBean.getCategoryName());
            moneyEt.setText(FormatNumberUtil.formatFloat(accountBean.getMoney()));
            moneyEt.setSelection(String.valueOf(FormatNumberUtil.formatFloat(accountBean.getMoney())).length());
            if(accountBean.getComment()==null||accountBean.getComment().equals("")) {
                comment.setText("Add comment");
            }else {
                comment.setText(accountBean.getComment());
                commentStr = accountBean.getComment();
            }
            timeStr = accountBean.getTime();
            timeTv.setText(accountBean.getTime());
        }


        // 让自定义的键盘显示出来
        KeyBoardUtils keyBoardUtils = new KeyBoardUtils(keyboardView, moneyEt);
        keyBoardUtils.showKeyboard();
        // 设置接口，监听确定键盘被点击
        keyBoardUtils.setOnEnsureListener(new KeyBoardUtils.OnEnsureListener() {
            @Override
            public void onEnsure() {
                // 获取输入钱数
                String moneyStr = moneyEt.getText().toString();
                if (TextUtils.isEmpty(moneyStr) ) {
                    //getActivity().finish();
                    //这里不在默认结束本层会话，而是添加提示
                    Toast.makeText(getActivity(), "Amount can't be empty", Toast.LENGTH_LONG).show();
                    return;
                }else {
                    String regex = "[0]+";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(moneyStr);
                    if(matcher.matches()){
                        Toast.makeText(getActivity(), "Amount can't be zero", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                float money = Float.parseFloat(moneyStr);
                accountBean.setMoney(money);
                // 获取记录的信息，插入到数据库中
                saveAccountToDb();
                // 返回上一级
                getActivity().finish();
            }
        });
    }

    /*让子类必须重写该方法*/
    public abstract void saveAccountToDb();

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.frag_record_tv_time:
                showTimeDialog();
                break;
            case R.id.frag_record_tv_comment:
                showDialog();
                break;
        }
    }

    /**
     * 弹出显示时间的对话框
     */
    private void showTimeDialog(){
        SelectTimeDialog selectTimeDialog = new SelectTimeDialog(getContext());
        selectTimeDialog.setTime(timeStr);
        selectTimeDialog.show();
        // 设定确定按钮被点击的监听
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

    public void showDialog(){
        /*弹出备注对话框*/
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