package com.jamesou.dailycost.utils;

/**
 * Created by jamesou on 11/05/2024
 * Describe:
 */
public class StringUtil {

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }


    public static boolean isNotNullOrEmpty(String str) {
        return !isNullOrEmpty(str);
    }

    public static String formatString(String str) {
        return str != null ? str : "";
    }

}
