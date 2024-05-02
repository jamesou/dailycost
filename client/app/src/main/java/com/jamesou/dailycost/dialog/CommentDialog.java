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
        setContentView(R.layout.dialog_comment);
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

    public String getEditText(){
        return editText.getText().toString().trim();
    }

    public void setEditText(String content){
        title.setText("Modify Comment");
        editText.setText(content);
        editText.setSelection(editText.getText().length());
    }


    public void setDialogSize(){

        Window window = getWindow();

        WindowManager.LayoutParams wlp = window.getAttributes();
        // get the screen object
        Display d = window.getWindowManager().getDefaultDisplay();
        wlp.width = (int)d.getWidth();
        wlp.gravity = Gravity.BOTTOM;   // from button to up
        window.setAttributes(wlp);
        hendler.sendEmptyMessageDelayed(1 , 100);
    }
    Handler hendler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            // pop up keyboard
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(0 , InputMethodManager.HIDE_NOT_ALWAYS);
        }
    };
}
