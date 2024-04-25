package com.jamesou.dailycost.utils;

import android.widget.Toast;
import android.content.Context;
/**
 * Created by jamesou on
 * Describe:
 */
public class PromptMsgUtil {
    public static void promptMsg(Context context,String PROMPT_MSG){
        Toast.makeText(context, PROMPT_MSG, Toast.LENGTH_SHORT).show();
    }
}
