package com.jamesou.dailycost.utils;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.jamesou.dailycost.R;


public class KeyBoardUtils {

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
        // use self-defined keyboard instead of system keyboard
        this.editText.setInputType(InputType.TYPE_NULL);
        k1 = new Keyboard(this.editText.getContext() , R.xml.key);
        // set the keyboard style
        this.keyboardView.setKeyboard(k1);
        this.keyboardView.setEnabled(true);
        this.keyboardView.setPreviewEnabled(false);
        // listener keyboard click event
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
            int start = editText.getSelectionStart();
            // key's ascii code
            switch (primaryCode){
                case Keyboard.KEYCODE_DELETE:
                    if (text != null && text.length() > 0 ){
                        if(start > 0){
                            text.delete(start - 1 , start);
                        }
                    }
                    break;
                case Keyboard.KEYCODE_CANCEL:
                    text.clear();
                    break;
                case Keyboard.KEYCODE_DONE:
                    onEnsureListener.onEnsure();    //click 'Confirm' to trigger the method
                    break;
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


    public void showKeyboard(){
        int visibility = keyboardView.getVisibility();
        if(visibility == View.INVISIBLE || visibility == View.GONE){
            keyboardView.setVisibility(View.VISIBLE);
        }
    }

    public void hideKeyboard(){
        int visibility = keyboardView.getVisibility();
        if(visibility == View.VISIBLE || visibility == View.INVISIBLE){
            keyboardView.setVisibility(View.GONE);
        }
    }
}
