package com.jamesou.dailycost.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import com.jamesou.dailycost.R;



public class DBOpenHelper extends SQLiteOpenHelper {
    public DBOpenHelper(@Nullable Context context) {
        super(context, "dailycost.db", null, 1);
    }

    /**
     * init tables
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // create an expense category table
        String sql = "create table categorytb(id integer primary key autoincrement , " +
                "categoryName varchar(10) ,imageId integer , sImageId integer , kind integer)";
        db.execSQL(sql);
        // init the category
        insertCategory(db);
        // create an expense table
        sql = "create table accounttb(id integer primary key autoincrement,categoryName varchar(10),sImageId integer," +
                "comment varchar(80),money float,time varchar(60),year integer, month integer ,day integer,kind integer)";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private void insertCategory(SQLiteDatabase db) {
        String sql = "insert into categorytb(categoryName , imageId , sImageId , kind) values (? , ? , ? , ?)";
        db.execSQL(sql , new Object[]{"Catering" , R.mipmap.ic_catering, R.mipmap.ic_catering_fs, 0});
        db.execSQL(sql , new Object[]{"Transp." , R.mipmap.ic_transport, R.mipmap.ic_snacks_fs, 0});
        db.execSQL(sql , new Object[]{"Shopping" , R.mipmap.ic_shopping, R.mipmap.ic_shopping_fs, 0});
        db.execSQL(sql , new Object[]{"Clothes" , R.mipmap.ic_clothes, R.mipmap.ic_clothes_fs, 0});
        db.execSQL(sql , new Object[]{"DailyNec." , R.mipmap.ic_dailynecessarities, R.mipmap.ic_dailynecessarities_fs, 0});
        db.execSQL(sql , new Object[]{"Entertain." , R.mipmap.ic_entertainment, R.mipmap.ic_entertainment_fs, 0});
        db.execSQL(sql , new Object[]{"Snack" , R.mipmap.ic_snacks, R.mipmap.ic_snacks_fs, 0});
        db.execSQL(sql , new Object[]{"Bar" , R.mipmap.ic_bars, R.mipmap.ic_bars_fs, 0});
        db.execSQL(sql , new Object[]{"Educ." , R.mipmap.ic_education, R.mipmap.ic_education_fs, 0});
        db.execSQL(sql , new Object[]{"Medic." , R.mipmap.ic_medicine, R.mipmap.ic_medicine_fs, 0});
        db.execSQL(sql , new Object[]{"HouseRent" , R.mipmap.ic_houserent, R.mipmap.ic_houserent_fs, 0});
        db.execSQL(sql , new Object[]{"Bill" , R.mipmap.ic_bills, R.mipmap.ic_bills_fs, 0});
        db.execSQL(sql , new Object[]{"Telecoms." , R.mipmap.ic_telecommunication, R.mipmap.ic_telecommunication_fs, 0});
        db.execSQL(sql , new Object[]{"Others" , R.mipmap.ic_others, R.mipmap.ic_others_fs, 0});
        db.execSQL(sql , new Object[]{"Wages" , R.mipmap.in_wage, R.mipmap.in_wage_fs, 1});
        db.execSQL(sql , new Object[]{"Bonus" , R.mipmap.in_bonus, R.mipmap.in_bonus_fs, 1});
        db.execSQL(sql , new Object[]{"Borrow" , R.mipmap.in_borrow, R.mipmap.in_borrow_fs, 1});
        db.execSQL(sql , new Object[]{"Invest." , R.mipmap.in_investment , R.mipmap.in_investment_fs , 1});
        db.execSQL(sql , new Object[]{"Others" , R.mipmap.in_others, R.mipmap.in_others_fs, 1});


    }


}
