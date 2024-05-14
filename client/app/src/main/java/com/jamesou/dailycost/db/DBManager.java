package com.jamesou.dailycost.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.jamesou.dailycost.R;
import com.jamesou.dailycost.utils.FormatNumberUtil;
import com.jamesou.dailycost.utils.StringUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * @Description ：it is used to manipulate DB
 */
@SuppressLint("Range")
public class DBManager {
    private static SQLiteDatabase db;

    private static String mStrPath;

    private static Context mContext;


    public static void initDB(Context context){
        DBOpenHelper helper = new DBOpenHelper(context);
        db = helper.getWritableDatabase();
        mStrPath = "dailycost.txt";
        mContext = context;
    }
    /**
     * get data from localDb and insert into memory
     */
    public static List<CategoryBean> getCategoryList(int kind){
        List<CategoryBean> list = new ArrayList<>();
        String sql = "select * from categorytb where kind = " + kind+"  order by id";
        if(kind==-1){
            sql = "select * from categorytb  order by id";
        }
        Cursor cursor = db.rawQuery(sql, null);
        while(cursor.moveToNext()){
            String categoryName = cursor.getString(cursor.getColumnIndex("categoryName"));
            int imageId = cursor.getInt(cursor.getColumnIndex("imageId"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int kindDB = cursor.getInt(cursor.getColumnIndex("kind"));
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            CategoryBean categoryBean = new CategoryBean(id , categoryName, imageId, sImageId, kindDB);
            list.add(categoryBean);
        }
        return list;
    }

    public static void insertCategorytb(CategoryBean categoryBean){
        ContentValues values = convertCategoryBeanToDBbean(categoryBean);
        db.insert("categorytb" , null , values);
    }

    public static void updateCategorytb(CategoryBean categoryBean) {
        ContentValues values = convertCategoryBeanToDBbean(categoryBean);
        String selection = "id = ?";
        String[] selectionArgs = { String.valueOf(categoryBean.getId()) };
        db.update("categorytb", values, selection, selectionArgs);
    }
    private static ContentValues convertCategoryBeanToDBbean(CategoryBean categoryBean){
        ContentValues updateValues = new ContentValues();
        updateValues.put("categoryName", categoryBean.getCategoryName());
        updateValues.put("imageId", categoryBean.getImageId());
        updateValues.put("sImageId", categoryBean.getsImageId());
        updateValues.put("kind", categoryBean.getKind());
        return updateValues;
    }
    public static List<CategoryBean> getNoneValueCategoryList(int kind){
        List<CategoryBean> list = new ArrayList<>();
        if(kind == 0) {
            list.add(new CategoryBean(0,"" , R.mipmap.ic_catering, R.mipmap.ic_catering_fs, 0));
            list.add(new CategoryBean(0,"" , R.mipmap.ic_transport, R.mipmap.ic_snacks_fs, 0));
            list.add(new CategoryBean(0,"" , R.mipmap.ic_shopping, R.mipmap.ic_shopping_fs, 0));
            list.add(new CategoryBean(0,"" , R.mipmap.ic_clothes, R.mipmap.ic_clothes_fs, 0));
            list.add(new CategoryBean(0,"" , R.mipmap.ic_dailynecessarities, R.mipmap.ic_dailynecessarities_fs, 0));
            list.add(new CategoryBean(0,"" , R.mipmap.ic_entertainment, R.mipmap.ic_entertainment_fs, 0));
            list.add(new CategoryBean(0,"" , R.mipmap.ic_snacks, R.mipmap.ic_snacks_fs, 0));
            list.add(new CategoryBean(0,"" , R.mipmap.ic_bars, R.mipmap.ic_bars_fs, 0));
            list.add(new CategoryBean(0,"" , R.mipmap.ic_education, R.mipmap.ic_education_fs, 0));
            list.add(new CategoryBean(0,"" , R.mipmap.ic_medicine, R.mipmap.ic_medicine_fs, 0));
            list.add(new CategoryBean(0,"" , R.mipmap.ic_houserent, R.mipmap.ic_houserent_fs, 0));
            list.add(new CategoryBean(0,"" , R.mipmap.ic_bills, R.mipmap.ic_bills_fs, 0));
            list.add(new CategoryBean(0,"" , R.mipmap.ic_telecommunication, R.mipmap.ic_telecommunication_fs, 0));
            list.add(new CategoryBean(0,"" , R.mipmap.ic_others, R.mipmap.ic_others_fs, 0));
        }else {
            list.add(new CategoryBean(0,"" , R.mipmap.in_wage, R.mipmap.in_wage_fs, 1));
            list.add(new CategoryBean(0,"" , R.mipmap.in_bonus, R.mipmap.in_bonus_fs, 1));
            list.add(new CategoryBean(0,"" , R.mipmap.in_borrow, R.mipmap.in_borrow_fs, 1));
            list.add(new CategoryBean(0,"" , R.mipmap.in_investment , R.mipmap.in_investment_fs , 1));
            list.add(new CategoryBean(0,"" , R.mipmap.in_others, R.mipmap.in_others_fs, 1));
        }
        return list;
    }
    public static String updateCategoryBeanById(int selectId,int kind,int flag,List<CategoryBean> list) {
        int count = 0;
        int objindex = 0;
        for(CategoryBean bean:list) {
            if(bean.getKind() == kind){
                if(bean.getId() == selectId){
                    objindex = count;
                }
            }
            count++;
        }
        // flag 0 move down，1 move up
        if(flag == 0) {
            if((objindex)+1 == list.size()) {
                return "tail";
            }
        }else {
            if(objindex==0){
                return "first";
            }
        }
        CategoryBean bean = list.get(objindex);
        CategoryBean bean2 = null;
        if(flag == 0) {
            bean2 = list.get(objindex+1);
        }else {
            bean2 = list.get(objindex-1);
        }
        ContentValues updateValues = convertCategoryBeanToDBbean(bean2);
        String selection = "id = ?";
        String[] selectionArgs = { String.valueOf(bean.getId()) };
//        System.out.println("bean.toString():"+bean.toString());
//        System.out.println("bean2.toString():"+bean2.toString());
//        System.out.println("updateValues:"+updateValues);
        db.update("categorytb", updateValues, selection, selectionArgs);
        CategoryBean tempBean = new CategoryBean(bean.getId() , bean.getCategoryName(), bean.getImageId(), bean.getsImageId(), kind);
        updateValues = convertCategoryBeanToDBbean(tempBean);
//        System.out.println("tempBean.toString():"+tempBean.toString());
//        System.out.println("updateValues:"+updateValues.toString());
        updateValues.put("categoryName", tempBean.getCategoryName());
        updateValues.put("imageId", tempBean.getImageId());
        updateValues.put("sImageId", tempBean.getsImageId());
        updateValues.put("kind", tempBean.getKind());
        selectionArgs = new String[]{String.valueOf(bean2.getId())};
        db.update("categorytb", updateValues, selection, selectionArgs);
        return "success";
    }


    public static int deleteCategoryById(int id){
        int i = db.delete("categorytb" , "id=?" , new String[]{id+""});
        return i;
    }

    /**
     *  kind：expense——>1，income——>0
     */
    public static float getSumMoneyByDay(int year , int month , int day , int kind){
        String sql = "select sum(money) from accounttb where year=? and month=? and day=? and kind=?";
        return getSumMoney(sql, new String[]{year + "", month + "", day + "", kind + ""});
    }

    public static float getSumMoneyByMonth(int year , int month , int kind){
        String sql = "select sum(money) from accounttb where year=? and month=? and kind=?";
        return getSumMoney(sql,new String[]{year + "", month + "", kind + ""});
    }

    public static float getSumMoneyByYear(int year  , int kind){
        String sql = "select sum(money) from accounttb where year=?   and kind=?";
        return getSumMoney(sql,new String[]{year + "", kind + ""});
    }
    public static int getCountItemByMonth(int year , int month , int kind){
        String sql = "select count(money) from accounttb where year=? and month=? and kind=?";
        return getCountItem(sql,new String[]{year + "", month + "", kind + ""});
    }

    public static int getCountItemByYear(int year , int kind){
        String sql = "select count(money) from accounttb where year=?   and kind=?";
        return getCountItem(sql,new String[]{year + "" , kind + ""});
    }

    public static List<AccountBean> getAccountListByDate(int year , int month , int day){
        StringBuilder sql = new StringBuilder("select * from accounttb where 1=1 ");
        Vector<String> vector = new Vector<>();
        if(StringUtil.isNotNullOrEmpty(String.valueOf(year))) {
            sql.append(" and year=? ");
            vector.add(String.valueOf(year));
        }
        if(StringUtil.isNotNullOrEmpty(String.valueOf(month))) {
            sql.append(" and month=? ");
            vector.add(String.valueOf(month));
        }
        if(StringUtil.isNotNullOrEmpty(String.valueOf(day))) {
            sql.append(" and day=? ");
            vector.add(String.valueOf(day));
        }
        sql.append(" order by id desc");
        String[] sqlParams = vector.toArray(new String[vector.size()]);
        return getAccountList(sql.toString(),sqlParams,0);
    }

    public static void insertItemToAccounttb(AccountBean accountBean){
        ContentValues values = convertAccountBeanToDBbean(accountBean);
        db.insert("accounttb" , null , values);
    }

    public static void updateItemToAccounttb(AccountBean accountBean) {
        ContentValues values = convertAccountBeanToDBbean(accountBean);
        String selection = "id = ?";
        String[] selectionArgs = { String.valueOf(accountBean.getId()) };
        db.update("accounttb", values, selection, selectionArgs);
    }


    public static int deleteRecordById(int id){
        int i = db.delete("accounttb" , "id=?" , new String[]{id+""});
        return i;
    }

    public static List<AccountBean> getAccountListByComment(String comment){
        Calendar calendar = Calendar.getInstance();
        int yeardefault = calendar.get(Calendar.YEAR);
        int monthdefault = calendar.get(Calendar.MONTH) + 1;
        //default current month
        String sql = "select * from accounttb where (comment like '%"+ comment +"%'  or categoryName like '%"+ comment +"%') and year = "+yeardefault+" and month = "+monthdefault;
        float summoney = 0;
        List<AccountBean> list = getAccountList(sql,null,summoney);
        list.add(0,getSumAccountBean(summoney));
        return list;
    }

    public static List<AccountBean> getAccountListBySearchCondition(String comment, int yearparam, int monthparam){
        String sql = "select * from accounttb where (comment like '%"+ comment +"%'  or categoryName like '%"+ comment +"%') and year = "+yearparam+" and month = "+monthparam;
        float summoney = 0;
        List<AccountBean> list = getAccountList(sql,null,summoney);
        list.add(0,getSumAccountBean(summoney));
        return list;
    }

    public static List<Integer> getYearListFromAccounttb(){
        List<Integer> list = new ArrayList<>();
        String sql = "select distinct(year) from accounttb order by year asc";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int year = cursor.getInt(cursor.getColumnIndex("year"));
            list.add(year);
        }
        return list;
    }

    public static void deleteAllAccounttb(){
        String sql = "delete from accounttb";
        db.execSQL(sql);
    }

    public static List<BarChartItemBean> getBarChartListGroupByDay(int year , int month , int kind){
        List<BarChartItemBean> list = new ArrayList<>();
        String sql = "select day ,sum(money) from accounttb where year=? and month=? " +
                "and kind=? group by day";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        while (cursor.moveToNext()) {
            int day = cursor.getInt(cursor.getColumnIndex("day"));
            float sumMoney = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            sumMoney = Math.round(sumMoney * 100) / 100f;
            BarChartItemBean bean = new BarChartItemBean(year, month, day, sumMoney);
            list.add(bean);
        }
        return list;
    }

    public static List<BarChartItemBean> getBarChartListGroupByMonth(int year, int kind){
        List<BarChartItemBean> list = new ArrayList<>();
        String sql = "select month ,sum(money) from accounttb where year=?   " +
                "and kind=? group by month";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", kind + ""});
        while (cursor.moveToNext()) {
            int month = cursor.getInt(cursor.getColumnIndex("month"));
            float sumMoney = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            sumMoney = Math.round(sumMoney * 100) / 100f;
            BarChartItemBean bean = new BarChartItemBean(year, month, sumMoney);
            list.add(bean);
        }
        return list;
    }
    public static float getSumMoneyGroupByDay(int year , int month , int kind){
        String sql = "select sum(money) from accounttb where year=? and month=? " +
                "and kind=? group by day order by sum(money) desc";
        return getSumMoney(sql,new String[]{year + "", month + "", kind + ""});
    }

    public static float getSumMoneyGroupByMonth(int year, int kind){
        String sql = "select sum(money) from accounttb where year=?  " +
                "and kind=? group by month order by sum(money) desc";
        return getSumMoney(sql,new String[]{year + "", kind + ""});
    }

    public static List<AccountBean> getSumMoneyOneDayByKind(int year , int month , int day){
        List<AccountBean> list = new ArrayList<>();
        String sql = "select kind ,sum(money) from accounttb where year=? and month=?  " +
                "and day =? group by kind";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", day + ""});
        while (cursor.moveToNext()) {
            int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            float sumMoney = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            sumMoney = Math.round(sumMoney * 100) / 100f;
            AccountBean bean = new AccountBean();
            bean.setKind(kind);
            bean.setMoney(sumMoney);
            list.add(bean);
        }
        return list;
    }
    public static List<ChartItemBean> getChartListByMonth(int year , int month , int kind){
        float oneMonth = getSumMoneyByMonth(year, month, kind);
        String sql = "select categoryName,sImageId,sum(money) as total from accounttb " +
                "where year=? and month=? and kind=? group by categoryName order by " +
                "total desc";
        return getChartList(sql,new String[]{year + "", month + "", kind + ""},oneMonth);
    }

    public static List<ChartItemBean> getChartListByYear(int year , int kind){
        float oneYear = getSumMoneyByYear(year, kind);
        String sql = "select categoryName,sImageId,sum(money) as total from accounttb " +
                "where year=?  and kind=? group by categoryName order by " +
                "total desc";
        return getChartList(sql,new String[]{year + "", kind + ""},oneYear);
    }
    private static ContentValues convertAccountBeanToDBbean(AccountBean accountBean){
        ContentValues values = new ContentValues();
        values.put("categoryName" , accountBean.getCategoryName());
        values.put("sImageId" , accountBean.getsImageId());
        values.put("comment" , accountBean.getComment());
        values.put("money" , accountBean.getMoney());
        values.put("time" , accountBean.getTime());
        values.put("year" , accountBean.getYear());
        values.put("month" , accountBean.getMonth());
        values.put("day" , accountBean.getDay());
        values.put("kind" , accountBean.getKind());
        return values;
    }
    private static AccountBean getSumAccountBean(float summoney){
        AccountBean sumaccountBean = new AccountBean();
        sumaccountBean.setCategoryName("Total");
        summoney = Math.round(summoney * 100) / 100f;
        sumaccountBean.setMoney(summoney);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String time = sdf.format(date);
        sumaccountBean.setTime("Today "+time);
        sumaccountBean.setsImageId(R.mipmap.ic_dollar);
        sumaccountBean.setComment("Automatic Calculation");
        return sumaccountBean;
    }
    private static int getCountItem(String sql, String[] sql_params){
        int total = 0;
        Cursor cursor = db.rawQuery(sql, sql_params);
        if (cursor.moveToFirst()) {
            int count = cursor.getInt(cursor.getColumnIndex("count(money)"));
            total = count;
        }
        return total;
    }
    private static float getSumMoney(String sql, String[] sql_params){
        Cursor cursor = db.rawQuery(sql, sql_params);
        if (cursor.moveToFirst()){
            float sumMoney = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            return Math.round(sumMoney * 100) / 100f;
        }
        return 0;
    }

    private static List<ChartItemBean> getChartList(String sql, String[] sql_params, float oneDayMoney){
        List<ChartItemBean> list= new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, sql_params);
        while (cursor.moveToNext()){
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            String categoryName = cursor.getString(cursor.getColumnIndex("categoryName"));
            float total = cursor.getFloat(cursor.getColumnIndex("total"));
            String percent = FormatNumberUtil.percent(total, oneDayMoney);
            total = Math.round(total * 100) / 100f;
            ChartItemBean bean = new ChartItemBean(sImageId, categoryName, percent, total);
            list.add(bean);
        }
        return list;
    }
    private static List<AccountBean> getAccountList(String sql,String[] sql_params,float summoney){
        List<AccountBean> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql ,sql_params);
        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String categoryName = cursor.getString(cursor.getColumnIndex("categoryName"));
            String comment = cursor.getString(cursor.getColumnIndex("comment"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            float money = cursor.getFloat(cursor.getColumnIndex("money"));
            summoney += money;
            money = Math.round(money * 100) / 100f;
            int year = cursor.getInt(cursor.getColumnIndex("year"));
            int month = cursor.getInt(cursor.getColumnIndex("month"));
            int day = cursor.getInt(cursor.getColumnIndex("day"));
            AccountBean accountBean = new AccountBean(id, categoryName, sImageId, comment, money, time, year, month, day, kind);
            list.add(accountBean);
        }
        return list;
    }
}

