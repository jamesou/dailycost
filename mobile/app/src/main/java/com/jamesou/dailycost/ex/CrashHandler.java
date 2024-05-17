package com.jamesou.dailycost.ex;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.*;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * customised exception handler
 */
public class CrashHandler implements UncaughtExceptionHandler {
    public static final String TAG = "CrashHandler";
    private UncaughtExceptionHandler mDefaultHandler;
    private static CrashHandler INSTANCE = new CrashHandler();
    private Context mContext;
    String filePath="";

    public static CrashHandler getInstance() {
        return INSTANCE;
    }
    private DateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");

    public void init(Context context) {
        mContext = context;
        //OS system's UncaughtException
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();

        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            //uncaught exception handled by os system
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Log.e(TAG, "error : ", e);
            }
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }

    }

    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        //add a thread to display exception msg
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "Sorry, the program has an exception", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();

        Map<String,String>  infos = collectDeviceInfo(mContext);
        saveCrashInfo2File(ex,infos);
        return true;
    }

    public Map<String,String> collectDeviceInfo(Context ctx) {
        Map<String,String> infos = new HashMap<>();
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "an error occured when collect package info", e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
                Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                Log.e(TAG, "an error occured when collect crash info", e);
            }
        }
        return infos;
    }

    private String saveCrashInfo2File(Throwable ex,Map<String,String> infos) {

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append("[" + key + ", " + value + "]\n");
        }
        sb.append("\n" + getStackTraceString(ex));
        try {
            String time = formatter.format(new Date());
            String fileName = "dailycost_" + time + ".txt";
            File sdDir = null;
            sdDir = mContext.getExternalFilesDir("logs").getAbsoluteFile();
            File file = null;
            if (!TextUtils.isEmpty(filePath)) {
                File files = new File(filePath);
                if (!files.exists()) {
                    //Create a directory
                    files.mkdirs();
                }
                file = new File(filePath + File.separator + fileName);
            } else {
                file = new File(sdDir + File.separator + fileName);
            }
            if (file == null) {
                file = new File(sdDir + File.separator + fileName);
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(sb.toString().getBytes());
            fos.close();
            return file.getAbsolutePath();
        } catch (Exception e) {
        }
        return null;
    }
    public static String getStackTraceString(Throwable tr) {
        try {
            if (tr == null) {
                return "";
            }
            Throwable t = tr;
            while (t != null) {
                if (t instanceof UnknownHostException) {
                    return "";
                }
                t = t.getCause();
            }
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            tr.printStackTrace(pw);
            return sw.toString();
        } catch (Exception e) {
            return "";
        }
    }
}
