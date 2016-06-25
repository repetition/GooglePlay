package com.example.googleplay;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by LIANGSE on 2016/3/11.
 */
public class MyApplication extends Application {
    private static Context mContext;

    private static Handler handler = new Handler();

    private static List<Activity> mActivityList = new LinkedList<>();

    private static boolean isRun = true;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }


    public static Context getContext() {
        return mContext;
    }

    public static Handler getHandler() {
        return handler;
    }

    public static boolean isRun() {
        return isRun;
    }

    public static void setIsRun(boolean run) {
        isRun = run;
    }

    public static void addActivity(Activity activity) {
        mActivityList.add(activity);
    }

    public static void removeActivity() {
        for (Activity activity : mActivityList) {
            activity.finish();
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

    }
}
