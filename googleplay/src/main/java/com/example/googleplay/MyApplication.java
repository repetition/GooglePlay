package com.example.googleplay;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
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

    private static SharedPreferences mPres;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        // mPres = getSharedPreferences("appConfig", MODE_PRIVATE);
   /*     ThreadManager.instance().createLongPool().execute(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 6; i++) {
                    UIUtils.getPreferences("appConfig").edit().remove("isFirstLoadData" + i).commit();
                    Log.e("onDestroy", "isFirstLoadData" + i + ":被清除");
                }
            }
        });*/
    }


    public static Context getContext() {
        return mContext;
    }

    public static Handler getHandler() {
        return handler;
    }

    public static SharedPreferences getPres() {
        return mPres;
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
