package com.example.googleplay.utils;

/**
 * Created by LIANGSE on 2016/5/27.
 */
public class TextUtils {
    /**
     * 判断字符是否是空串
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {

        if (str != null && str.trim().length() == 0 && str.trim().equals("null")) {
            return true;
        }
        if (str==null){
            return true;
        }

        return false;
    }
}
