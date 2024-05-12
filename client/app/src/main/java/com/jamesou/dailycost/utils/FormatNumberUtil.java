/*
 *  Created with IntelliJ IDEA  Community.
 *  Description: taily
 *  Author: mabb
 *  DateTime: 2024-1-16
 *
 *
 */

package com.jamesou.dailycost.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class FormatNumberUtil {
    private static final DecimalFormat df = new DecimalFormat("#.##");
    public static String formatFloat(float num){
        return df.format(num);
    }

    public static String percent(float v1 , float v2){
        float v3 = v1 / v2;
        v3 = v3 * 100;
        BigDecimal decimal = new BigDecimal(v3);
        float val = decimal.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
        return val+ "%";
    }
}
