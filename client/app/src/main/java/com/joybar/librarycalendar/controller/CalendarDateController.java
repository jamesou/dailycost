package com.joybar.librarycalendar.controller;


import com.jamesou.dailycost.db.AccountBean;
import com.jamesou.dailycost.db.DBManager;
import com.jamesou.dailycost.utils.FormatNumberUtil;
import com.joybar.librarycalendar.data.CalendarDate;
import com.joybar.librarycalendar.data.Lunar;
import com.joybar.librarycalendar.data.Solar;
import com.joybar.librarycalendar.utils.CalendarUtils;
import com.joybar.librarycalendar.utils.LunarSolarConverter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by joybar on 2/24/16.
 * Modified by jamesou on 11/05/2024
 */
public class CalendarDateController {

    public static List<CalendarDate> getCalendarDate(int year, int month) {
        List<CalendarDate> mListDate = new ArrayList<>();
        List<CalendarUtils.CalendarSimpleDate> list = null;
        try {
            list = CalendarUtils.getEverydayOfMonth(year, month);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int count = list.size();

        for (int i = 0; i < count; i++) {
            Solar solar = new  Solar();
            solar.solarYear = list.get(i).getYear();
            solar.solarMonth = list.get(i).getMonth();
            solar.solarDay = list.get(i).getDay();
            Lunar lunar = LunarSolarConverter.SolarToLunar(solar);
            //查询下当天的支出和收入
            List<AccountBean> accountBeans = DBManager.getSumMoneyOneDayByKind(list.get(i).getYear(),list.get(i).getMonth(),list.get(i).getDay());
            String outcome="",income = "";
            for(AccountBean bean:accountBeans){
                if(0==bean.getKind()) {
                    outcome = "-"+ FormatNumberUtil.formatFloat(bean.getMoney());
                }else if(1==bean.getKind()){
                    income = "+"+FormatNumberUtil.formatFloat(bean.getMoney());
                }
            }
            mListDate.add(new CalendarDate( month == list.get(i).getMonth(), false,solar,lunar,outcome,income));
        }

        return mListDate;
    }


}
