/*
 *  Created with IntelliJ IDEA  Community.
 *  Description: taily
 *  Author: mabb
 *  DateTime: 2024-1-16
 *
 *
 */

package com.jamesou.dailycost.utils;

import java.text.DecimalFormat;

public class FormatNumberUtil {
    private static final DecimalFormat df = new DecimalFormat("#.##");
    public static String formatFloat(float num){
        return df.format(num);
    }
}
