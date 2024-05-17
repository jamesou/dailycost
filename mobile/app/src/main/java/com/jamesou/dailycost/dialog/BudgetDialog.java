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
                cancel();
                break;
            case R.id.dialog_budget_btn_ensure:
                String moneyStr = moneyEt.getText().toString();
                if(TextUtils.isEmpty(moneyStr)){
                    Toast.makeText(getContext(), "Budget can not be empty" , Toast.LENGTH_SHORT ).show();
                    return;
                }
                float moneyFlo = Float.parseFloat(moneyStr);
                if(moneyFlo < 0){
                    Toast.makeText(getContext() , "Budget must be greater than 0" , Toast.LENGTH_SHORT).show();
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
