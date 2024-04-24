package com.jamesou.dailycost.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.jamesou.dailycost.R;

/**
 * @author:0.2℃
 * @date： 2021-07-15 12:29
 * @Description ：
 */

public class BudgetDialog extends Dialog implements View.OnClickListener {
    ImageView cancelIv;
    Button ensureBtn;
    EditText moneyEt;
    OnEnsureListener onEnsureListener;
    public BudgetDialog(@NonNull Context context) {
        super(context);
    }

    public interface OnEnsureListener{
        public void onEnsure(float money);
    }

    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_budget);
        cancelIv = findViewById(R.id.dialog_budget_iv_error);
        ensureBtn = findViewById(R.id.dialog_budget_btn_ensure);
        moneyEt = findViewById(R.id.dialog_budget_et);
        cancelIv.setOnClickListener(this);
        ensureBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_budget_iv_error:
                cancel();   // 取消对话框
                break;
            case R.id.dialog_budget_btn_ensure:
                // 获取输入的数值
                String moneyStr = moneyEt.getText().toString();
                if(TextUtils.isEmpty(moneyStr)){
                    Toast.makeText(getContext(), "输入数据不能为空" , Toast.LENGTH_SHORT ).show();
                    return;
                }
                float moneyFlo = Float.parseFloat(moneyStr);
                if(moneyFlo <= 0){
                    Toast.makeText(getContext() , "预算金额不能小于0" , Toast.LENGTH_SHORT).show();
                    return;
                }
                if (onEnsureListener != null) {
                    onEnsureListener.onEnsure(moneyFlo);
                }
                cancel();
                break;

        }
    }
}
