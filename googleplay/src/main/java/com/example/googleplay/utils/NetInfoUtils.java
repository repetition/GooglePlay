package com.example.googleplay.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.googleplay.MyApplication;

/**
 * Created by LIANGSE on 2016/3/11.
 */
public class NetInfoUtils {

    /**
     * 获取网络连接状态
     *
     * @return true 网络可用  false 网络不可用
     */
    public static boolean getNetWorkState() {
        //获取网络管理对象
        ConnectivityManager connectivityManager = (ConnectivityManager) MyApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable())
        {
            return true;//网络连接可用时,返回true
        }
        else
        {
            return false;//网络连接不可用时,返回false
        }

    }

    /**
     * 获取网络连接类型  WiFi MOBILE
     *
     * @return 网络连接状态为WIFI  网络连接状态为移动网络
     */
    public static String getNetWorkType() {
        //获取网络管理对象
        ConnectivityManager connectivityManager = (ConnectivityManager) MyApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected())
        {
            String netWorkTypeName = networkInfo.getTypeName();

            if (netWorkTypeName.equals("WIFI")) {
                return netWorkTypeName;
            } else {
                return netWorkTypeName;
            }
        }
        else
        {
            return "";
        }
    }

}
