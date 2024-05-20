package com.jamesou.dailycost.config;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import com.jamesou.dailycost.db.DBManager;
import com.jamesou.dailycost.ex.CrashHandler;

import java.util.Locale;

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

        Locale locale = new Locale("en"); // Replace "en" with chosen language code
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        }
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

    }
}
