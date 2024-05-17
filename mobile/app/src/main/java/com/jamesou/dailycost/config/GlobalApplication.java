package com.jamesou.dailycost.config;

import android.app.Application;
import com.jamesou.dailycost.db.DBManager;
import com.jamesou.dailycost.ex.CrashHandler;

/**
 * @Author: Jamesou
 * @Date: 3/03/24 21:26
 */
public class GlobalApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //Catch global exception
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
        DBManager.initDB(getApplicationContext());
    }
}
