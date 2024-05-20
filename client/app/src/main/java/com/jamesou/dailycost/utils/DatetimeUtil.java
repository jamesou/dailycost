package com.jamesou.dailycost.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DatetimeUtil {
    public static String getCurrentDateTime(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        return sdf.format(date);
    }
}
