package com.example.googleplay.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.example.googleplay.MyApplication;

/**
 * Created by LIANGSE on 2016/5/7.
 */
public class UIUtils {

    public static Handler getHandler() {
        return MyApplication.getHandler();
    }

    public static Context getContext() {
        return MyApplication.getContext();
    }

    /*获取颜色值*/
    public static int getColor(int colorId) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getContext().getResources().getColor(colorId, null);
        } else {
            return getContext().getResources().getColor(colorId);
        }
    }
    /*获取颜色选择器*/
    public static ColorStateList getColorSelector(int colorRes) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getContext().getResources().getColorStateList(colorRes, null);
        } else {
            return getContext().getResources().getColorStateList(colorRes);
        }

    }

    /**
     * 加载布局文件, 转换成view
     */
    public static View inflate(int layoutRes) {
        return View.inflate(getContext(), layoutRes, null);
    }


    public static void runOnUiThread(Runnable runnable) {
        getHandler().post(runnable);
    }


    public static SharedPreferences getPreferences(String PresName) {
        SharedPreferences preferences = getContext().getSharedPreferences(PresName, Context.MODE_PRIVATE);
        return preferences;
    }


    public static int px2dip(int pxValue)
    {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    public static float dip2px(float dipValue)
    {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        Log.i("UIUtils","scale:"+scale);
        return  (dipValue * scale + 0.5f);
    }

}
