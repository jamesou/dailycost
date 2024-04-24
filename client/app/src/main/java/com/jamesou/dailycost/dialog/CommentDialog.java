package com.jamesou.dailycost.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.jamesou.dailycost.R;



public class CommentDialog extends Dialog implements View.OnClickListener {
    EditText editText;
    Button btn_cancel , btn_ensure;
    onEnsureListener onEnsureListener;
    TextView title;
    // set callback function
    public void setOnEnsureListener(CommentDialog.onEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    public CommentDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_comment); // 设置对话框显示布局
        editText = (EditText) findViewById(R.id.dialog_comment_et);
        btn_cancel = (Button) findViewById(R.id.dialog_comment_btn_cancel);
        btn_ensure = (Button) findViewById(R.id.dialog_comment_btn_ensure);
        title = findViewById(R.id.dialog_comment_tv);
        btn_cancel.setOnClickListener(this);
        btn_ensure.setOnClickListener(this);
    }

    public interface onEnsureListener{
        public void onEnsure();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dialog_comment_btn_cancel:
                cancel();
                break;
            case R.id.dialog_comment_btn_ensure:
                if (onEnsureListener != null) {
                    onEnsureListener.onEnsure();
                }
                break;
        }
    }
    /**
     * 获取输入数据的方法
     */
    public String getEditText(){
        return editText.getText().toString().trim();    // trim()可以去掉空格
    }

    public void setEditText(String content){
        title.setText("修改备注");
        editText.setText(content);
        editText.setSelection(editText.getText().length());
    }

    /**
     * 设置Dialog的尺寸和屏幕尺寸一致
     */
    public void setDialogSize(){
        // 获取当前窗口对象
        Window window = getWindow();
        // 获取窗口对象的参数
        WindowManager.LayoutParams wlp = window.getAttributes();
        // 获取屏幕宽度
        Display d = window.getWindowManager().getDefaultDisplay();
        wlp.width = (int)d.getWidth(); // 对话框窗口为屏幕窗口
        wlp.gravity = Gravity.BOTTOM;   // 从下往上弹出
//        window.setBackgroundDrawableResource(android.R.color.transparent);  // 设置为透明
        window.setAttributes(wlp);
        hendler.sendEmptyMessageDelayed(1 , 100);
    }
    Handler hendler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            // 自动弹出软键盘的方法
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(0 , InputMethodManager.HIDE_NOT_ALWAYS);
        }
    };
}
