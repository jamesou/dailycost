package com.jamesou.dailycost.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.jamesou.dailycost.R;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Description ：it is used to manipulate DB
 */

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
        // 循环读取游标内容，存储到对象中
        while(cursor.moveToNext()){
            String categoryName = cursor.getString(cursor.getColumnIndex("categoryName"));
            int imageId = cursor.getInt(cursor.getColumnIndex("imageId"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int kind1 = cursor.getInt(cursor.getColumnIndex("kind"));
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            CategoryBean categoryBean = new CategoryBean(id , categoryName, imageId, sImageId, kind1);
            list.add(categoryBean);
        }
        return list;
    }

    public static void insertItemTocategorytb(CategoryBean categoryBean){
        ContentValues values = new ContentValues();
        values.put("categoryName" , categoryBean.getCategoryName());
        values.put("imageId" , categoryBean.getImageId());
        values.put("sImageId" , categoryBean.getsImageId());
        values.put("kind" , categoryBean.getKind());

        db.insert("categorytb" , null , values);
    }

    public static void updateItemTocategorytb(CategoryBean categoryBean) {
        ContentValues updateValues = new ContentValues();
        updateValues.put("categoryName", categoryBean.getCategoryName());
        updateValues.put("imageId", categoryBean.getImageId());
        updateValues.put("sImageId", categoryBean.getsImageId());
        updateValues.put("kind", categoryBean.getKind());
        String selection = "id = ?";
        String[] selectionArgs = { String.valueOf(categoryBean.getId()) };
        db.update("categorytb", updateValues, selection, selectionArgs);
    }

    public static List<CategoryBean> getTypeListBydry(int kind){
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

        ContentValues updateValues = new ContentValues();
        updateValues.put("categoryName", bean2.getCategoryName());
        updateValues.put("imageId", bean2.getImageId());
        updateValues.put("sImageId", bean2.getsImageId());
        updateValues.put("kind", bean2.getKind());
        CategoryBean tempBean = new CategoryBean(bean.getId() , bean.getCategoryName(), bean.getImageId(), bean.getsImageId(), kind);
        bean.setCategoryName(bean2.getCategoryName());
        bean.setImageId(bean2.getImageId());
        bean.setsImageId(bean2.getsImageId());
        bean.setKind(bean2.getKind());
        String selection = "id = ?";
        String[] selectionArgs = { String.valueOf(bean.getId()) };
        db.update("categorytb", updateValues, selection, selectionArgs);


        updateValues.clear();
        updateValues.put("categoryName", tempBean.getCategoryName());
        updateValues.put("imageId", tempBean.getImageId());
        updateValues.put("sImageId", tempBean.getsImageId());
        updateValues.put("kind", tempBean.getKind());
        selectionArgs = new String[]{String.valueOf(bean2.getId())};
        db.update("categorytb", updateValues, selection, selectionArgs);
        bean2.setCategoryName(tempBean.getCategoryName());
        bean2.setImageId(tempBean.getImageId());
        bean2.setsImageId(tempBean.getsImageId());
        bean2.setKind(tempBean.getKind());
        return "success";
    }


    public static int deleteItemFromTypeById(int id){
        int i = db.delete("categorytb" , "id=?" , new String[]{id+""});
        return i;
    }

    /**
     *  kind：expense——>1，income——>0
     */
    public static float setSumMoneyOneDay(int year , int month , int day , int kind){
        float total = 0.0f;
        String sql = "select sum(money) from accounttb where year=? and month=? and day=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", day + "", kind + ""});
        if (cursor.moveToFirst()) {
            float money = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            total = money;
        }
        total = Math.round(total * 100) / 100f;
        return total;
    }

    public static float getSumMoneyOneMonth(int year , int month , int kind){
        float total = 0.0f;
        String sql = "select sum(money) from accounttb where year=? and month=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        if (cursor.moveToFirst()) {
            float money = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            total = money;
        }
        total = Math.round(total * 100) / 100f;
        return total;
    }

    public static float getSumMoneyOneYear(int year  , int kind){
        float total = 0.0f;
        String sql = "select sum(money) from accounttb where year=?   and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", kind + ""});
        if (cursor.moveToFirst()) {
            float money = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            total = money;
        }
        total = Math.round(total * 100) / 100f;
        return total;
    }


    public static int getCountItemOneMonth(int year , int month , int kind){
        int total = 0;
        String sql = "select count(money) from accounttb where year=? and month=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        if (cursor.moveToFirst()) {
            int count = cursor.getInt(cursor.getColumnIndex("count(money)"));
            total = count;
        }
        return total;
    }

    public static int getCountItemOneYear(int year , int kind){
        int total = 0;
        String sql = "select count(money) from accounttb where year=?   and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "" , kind + ""});
        if (cursor.moveToFirst()) {
            int count = cursor.getInt(cursor.getColumnIndex("count(money)"));
            total = count;
        }
        return total;
    }

    public static float setSumMoneyOneYear(int year, int kind){
        float total = 0.0f;
        String sql = "select sum(money) from accounttb where year=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", kind + ""});
        // 遍历，因为求出来的是总数，所以应该只有一行
        if (cursor.moveToFirst()) {
            float money = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            total = money;
        }
        total = Math.round(total * 100) / 100f;
        return total;
    }

    public static void insertItemToAccounttb(AccountBean accountBean){
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

        db.insert("accounttb" , null , values);
    }

    public static List<AccountBean> getAccountListOneMonthFromAccounttb(int year , int month , int day){
        List<AccountBean> list = new ArrayList<>();
        String sql = "select * from accounttb where year=? and month=? and day=? order by time asc";
        Cursor cursor = db.rawQuery(sql ,
                new String[]{String.valueOf(year), String.valueOf(month), String.valueOf(day)});
        // 遍历符合要求的每一行数据
        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String categoryName = cursor.getString(cursor.getColumnIndex("categoryName"));
            String comment = cursor.getString(cursor.getColumnIndex("comment"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            float money = cursor.getFloat(cursor.getColumnIndex("money"));
            AccountBean accountBean = new AccountBean(id, categoryName, sImageId, comment, money, time, year, month, day, kind);
            list.add(accountBean);
        }
        return list;
    }

    public static void updateItemToAccounttb(AccountBean accountBean) {
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

        String selection = "id = ?";
        String[] selectionArgs = { String.valueOf(accountBean.getId()) };
        db.update("accounttb", values, selection, selectionArgs);
    }

    /**
     * 根据传入的id删除accountb表中的一条数据
     */
    public static int deleteItemFromAccounttbById(int id){
        int i = db.delete("accounttb" , "id=?" , new String[]{id+""});
        return i;
    }

    /**
     * 搜索方法：根据备注模糊查询
     */
    public static List<AccountBean> getAccountListByRemarkFromAccounttb(String comment){
        List<AccountBean> list = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        int yeardefault = calendar.get(Calendar.YEAR);
        int monthdefault = calendar.get(Calendar.MONTH) + 1;
        //默认为本月
        String sql = "select * from accounttb where (comment like '%"+ comment +"%'  or categoryName like '%"+ comment +"%') and year = "+yeardefault+" and month = "+monthdefault;
        Cursor cursor = db.rawQuery(sql, null);
        float summoney = 0;
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String categoryName = cursor.getString(cursor.getColumnIndex("categoryName"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            float money = cursor.getFloat(cursor.getColumnIndex("money"));
            summoney += money;
            comment = cursor.getString(cursor.getColumnIndex("comment"));
            int year = cursor.getInt(cursor.getColumnIndex("year"));
            int month = cursor.getInt(cursor.getColumnIndex("month"));
            int day = cursor.getInt(cursor.getColumnIndex("day"));
            AccountBean accountBean = new AccountBean(id, categoryName, sImageId, comment, money, time, year, month, day, kind);
            list.add(accountBean);
        }
        AccountBean sumaccountBean = new AccountBean();
        sumaccountBean.setCategoryName("合计");
        summoney = Math.round(summoney * 100) / 100f;
        sumaccountBean.setMoney(summoney);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String time = sdf.format(date);
        sumaccountBean.setTime("今天 "+time);
        sumaccountBean.setsImageId(R.mipmap.ic_icon1);
        sumaccountBean.setComment("系统自动计算");
        list.add(0,sumaccountBean);
        return list;
    }

    public static List<AccountBean> getAccountListByRemarkFromAccounttbYearMonth(String beizhu,int yearparam, int monthparam){
        List<AccountBean> list = new ArrayList<>();
        String sql = "select * from accounttb where (beizhu like '%"+ beizhu +"%'  or categoryName like '%"+ beizhu +"%') and year = "+yearparam+" and month = "+monthparam;
        Cursor cursor = db.rawQuery(sql, null);
        float summoney = 0;
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String categoryName = cursor.getString(cursor.getColumnIndex("categoryName"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            float money = cursor.getFloat(cursor.getColumnIndex("money"));
            summoney += money;
            String beizhu1 = cursor.getString(cursor.getColumnIndex("beizhu"));
            int year = cursor.getInt(cursor.getColumnIndex("year"));
            int month = cursor.getInt(cursor.getColumnIndex("month"));
            int day = cursor.getInt(cursor.getColumnIndex("day"));
            AccountBean accountBean = new AccountBean(id, categoryName, sImageId, beizhu1, money, time, year, month, day, kind);
            list.add(accountBean);
        }
        AccountBean sumaccountBean = new AccountBean();
        sumaccountBean.setCategoryName("合计");
        summoney = Math.round(summoney * 100) / 100f;
        sumaccountBean.setMoney(summoney);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String time = sdf.format(date);
        sumaccountBean.setTime("今天 "+time);
        sumaccountBean.setsImageId(R.mipmap.ic_icon1);
        sumaccountBean.setComment("系统自动计算");
        list.add(0,sumaccountBean);
        return list;
    }
    /**获取某一个月份的所有收入或者支出*/
    public static List<AccountBean> getAccountListOneMonthFromAccounttb(int year , int month){
        List<AccountBean> list = new ArrayList<>();
        String sql = "select * from accounttb where year=? and month=? order by time asc";
        Cursor cursor = db.rawQuery(sql ,
                new String[]{String.valueOf(year), String.valueOf(month)});
        // 遍历符合要求的每一行数据
        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String categoryName = cursor.getString(cursor.getColumnIndex("categoryName"));
            String beizhu = cursor.getString(cursor.getColumnIndex("beizhu"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            float money = cursor.getFloat(cursor.getColumnIndex("money"));
            money = Math.round(money * 100) / 100f;
            int day = cursor.getInt(cursor.getColumnIndex("day"));
            AccountBean accountBean = new AccountBean(id, categoryName, sImageId, beizhu, money, time, year, month, day, kind);
            list.add(accountBean);
        }
        return list;
    }

    public static List<AccountBean> getAccountListFromAccounttb(int year , int month,int dayparam){
        List<AccountBean> list = new ArrayList<>();
        String sql = "select * from accounttb where year=? and month=? and day=? order by time asc";
        Cursor cursor = db.rawQuery(sql ,
                new String[]{String.valueOf(year), String.valueOf(month), String.valueOf(dayparam)});
        // 遍历符合要求的每一行数据
        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String categoryName = cursor.getString(cursor.getColumnIndex("categoryName"));
            String beizhu = cursor.getString(cursor.getColumnIndex("beizhu"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            float money = cursor.getFloat(cursor.getColumnIndex("money"));
            money = Math.round(money * 100) / 100f;
            int day = cursor.getInt(cursor.getColumnIndex("day"));
            AccountBean accountBean = new AccountBean(id, categoryName, sImageId, beizhu, money, time, year, month, day, kind);
            list.add(accountBean);
        }
        return list;
    }

    /**查询记账的表当中有几个年份信息*/
    public static List<Integer> yearListFromAccounttb(){
        List<Integer> list = new ArrayList<>();
        String sql = "select distinct(year) from accounttb order by year asc";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int year = cursor.getInt(cursor.getColumnIndex("year"));
            list.add(year);
        }
        return list;
    }
    /**删除accounttb表格当中的所有数据*/
    public static void deleteAllAccounttb(){
        String sql = "delete from accounttb";
        db.execSQL(sql);
    }




    public static float getMaxMoneyOneDayInMonth(int year , int month , int kind){
        String sql = "select sum(money) from accounttb where year=? and month=? " +
                "and kind=? group by day order by sum(money) desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        if (cursor.moveToFirst()){
            float maxMoneyOneDay = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            maxMoneyOneDay = Math.round(maxMoneyOneDay * 100) / 100f;
            return maxMoneyOneDay;
        }
        return 0;
    }

    public static float getMaxMoneyOneMonthInYear(int year, int kind){
        String sql = "select sum(money) from accounttb where year=?  " +
                "and kind=? group by month order by sum(money) desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", kind + ""});
        if (cursor.moveToFirst()){
            float maxMoneyOneDay = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            maxMoneyOneDay = Math.round(maxMoneyOneDay * 100) / 100f;
            return maxMoneyOneDay;
        }
        return 0;
    }




    public static List<AccountBean> getSumMoneyOneDay(int year , int month , int day){
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

    public static void exportDataToTxt() {
        try {
            /*//传入路径 + 文件名
            File mFile = new File(mStrPath);
            //判断文件是否存在，存在就删除
            if (!mFile.exists()) {
                mFile.createNewFile();
            }else {
                mFile.delete();
                mFile.createNewFile();
            }*/
            //FileOutputStream osw = new FileOutputStream(mStrPath);
            /*
            Context.MODE_PRIVATE：为默认操作模式，代表该文件是私有数据，只能被应用本身访问，在该模式下，写入的内容会覆盖原文件的内容，如果想把新写入的内容追加到原文件中，可以使用Context.MODE_APPEND。
        　　 Context.MODE_APPEND：模式会检查文件是否存在，存在就往文件追加内容，否则就创建新文件。
        　　 Context.MODE_WORLD_READABLE和Context.MODE_WORLD_WRITEABLE用来控制其他应用是否有权限读写该文件。
        　　 MODE_WORLD_READABLE：表示当前文件可以被其他应用读取；MODE_WORLD_WRITEABLE：表示当前文件可以被其他应用写入。
            */
            FileOutputStream osw = mContext.openFileOutput(mStrPath, Context.MODE_PRIVATE);
            //循环读取两张表,写入到文件中
            String sql = "select * from accounttb ";
            Cursor cursor = db.rawQuery(sql, null);
            // 遍历符合要求的每一行数据
            while(cursor.moveToNext()){
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String categoryName = cursor.getString(cursor.getColumnIndex("categoryName"));
                String beizhu = cursor.getString(cursor.getColumnIndex("beizhu"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
                int kind = cursor.getInt(cursor.getColumnIndex("kind"));
                float money = cursor.getFloat(cursor.getColumnIndex("money"));
                money = Math.round(money * 100) / 100f;
                int year = cursor.getInt(cursor.getColumnIndex("year"));
                int month = cursor.getInt(cursor.getColumnIndex("month"));
                int day = cursor.getInt(cursor.getColumnIndex("day"));
                AccountBean accountBean = new AccountBean(id, categoryName, sImageId, beizhu, money, time, year, month, day, kind);
                osw.write((accountBean.toString()+"\n").getBytes());
            }

            sql = "select * from categorytb ";
            cursor = db.rawQuery(sql, null);
            // 循环读取游标内容，存储到对象中
            while(cursor.moveToNext()){
                String categoryName = cursor.getString(cursor.getColumnIndex("categoryName"));
                int imageId = cursor.getInt(cursor.getColumnIndex("imageId"));
                int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
                int kind1 = cursor.getInt(cursor.getColumnIndex("kind"));
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                CategoryBean categoryBean = new CategoryBean(id , categoryName, imageId, sImageId, kind1);
                osw.write((categoryBean.toString()+"\n").getBytes());
            }
            osw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String importDatafromTxt() throws IOException {

        FileInputStream input = mContext.openFileInput(mStrPath);
        String sql = "delete from accounttb";
        db.execSQL(sql);

        sql = "delete from categorytb";
        db.execSQL(sql);

        InputStreamReader inputStreamReader = null;
        BufferedReader br = null;
        try {
            inputStreamReader = new InputStreamReader(input);
            br = new BufferedReader(inputStreamReader);
            String line;
            while (null != (line = br.readLine())){
                if (!"".equals(line)){
                    String[] strArray = line.split(",");
                    if("CategoryBean".equals(strArray[0])){
                        ContentValues values = new ContentValues();
                        values.put("id" , strArray[1]);
                        values.put("categoryName" , strArray[2]);
                        values.put("imageId" , strArray[3]);
                        values.put("sImageId" , strArray[4]);
                        values.put("kind" , strArray[5]);
                        db.insert("categorytb" , null , values);
                    }else {
                        ContentValues values = new ContentValues();
                        values.put("id" , strArray[1]);
                        values.put("categoryName" , strArray[2]);
                        values.put("sImageId" , strArray[3]);
                        values.put("beizhu" , strArray[4].contains("null")?"":strArray[4]);
                        values.put("money" , strArray[5]);
                        values.put("time" , strArray[6]);
                        values.put("year" ,strArray[7]);
                        values.put("month" , strArray[8]);
                        values.put("day" , strArray[9]);
                        values.put("kind" , strArray[10]);
                        db.insert("accounttb" , null , values);
                    }
                }
            }
        }finally {
            if (null != br){
                br.close();
            }
            if (null != inputStreamReader){
                inputStreamReader.close();
            }
        }

        return "import sucess";
    }
}

