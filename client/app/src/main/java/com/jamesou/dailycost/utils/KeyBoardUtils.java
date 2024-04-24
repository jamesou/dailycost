package com.jamesou.dailycost.utils;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.jamesou.dailycost.R;


public class KeyBoardUtils {
    // 自定义的键盘
    private final Keyboard k1;
    private KeyboardView keyboardView;
    private EditText editText;

    public interface OnEnsureListener{
        public void onEnsure();
    }

    OnEnsureListener onEnsureListener;
    public void setOnEnsureListener(OnEnsureListener onEnsureListener){
        this.onEnsureListener = onEnsureListener;
    }
    public KeyBoardUtils(KeyboardView keyboardView, EditText editText) {
        this.keyboardView = keyboardView;
        this.editText = editText;
        // 为了避免获取焦点的时候弹出系统自带的键盘，而不是我们自定义的键盘,进行如下的设置
        this.editText.setInputType(InputType.TYPE_NULL);
        // 将键盘显示到哪个数据——>editText这个数据的改变
        k1 = new Keyboard(this.editText.getContext() , R.xml.key);

        // 设置键盘显示的样式
        this.keyboardView.setKeyboard(k1);
        // 键盘是可以使用的
        this.keyboardView.setEnabled(true);
        // 优先使用自定义键盘,可以预览
        this.keyboardView.setPreviewEnabled(false);
        // 设置键盘被点击的监听
        this.keyboardView.setOnKeyboardActionListener(listener);
    }
    KeyboardView.OnKeyboardActionListener listener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void onPress(int primaryCode) {

        }

        @Override
        public void onRelease(int primaryCode) {

        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            Editable text = editText.getText();
            // 从最初开始写入
            int start = editText.getSelectionStart();
            // 被选中的那一项的ASCII码
            switch (primaryCode){
                case Keyboard.KEYCODE_DELETE:   // 点击了删除
                    if (text != null && text.length() > 0 ){
                        if(start > 0){
                            text.delete(start - 1 , start);
                        }
                    }
                    break;
                case Keyboard.KEYCODE_CANCEL:   // 点击了清零
                    text.clear();
                    break;
                case Keyboard.KEYCODE_DONE:     // 点击了完成
                    onEnsureListener.onEnsure();    // 通过接口回调的方法，放点击确定时，可以调用这个方法
                    break;
                    /*
                    其他类型的直接插入
                    从点击的第一个开始插入
                    最终插入的是String类型
                    将primaryCode转成char，直接变成的ASCII码对应的数据
                     */
                default:
                    text.insert(start , Character.toString((char)primaryCode));
                    break;
            }
        }

        @Override
        public void onText(CharSequence text) {

        }

        @Override
        public void swipeLeft() {

        }

        @Override
        public void swipeRight() {

        }

        @Override
        public void swipeDown() {

        }

        @Override
        public void swipeUp() {

        }
    };

    /**
     * 显示键盘的方法
      */
    public void showKeyboard(){
        int visibility = keyboardView.getVisibility();
        if(visibility == View.INVISIBLE || visibility == View.GONE){
            keyboardView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏键盘的方法
      */
    public void hideKeyboard(){
        int visibility = keyboardView.getVisibility();
        if(visibility == View.VISIBLE || visibility == View.INVISIBLE){
            keyboardView.setVisibility(View.GONE);
        }
    }
}
