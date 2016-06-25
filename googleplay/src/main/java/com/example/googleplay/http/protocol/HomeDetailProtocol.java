package com.example.googleplay.http.protocol;

import android.util.Log;

import com.example.googleplay.domain.AppInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.googleplay.domain.AppInfo.*;

/**
 * Created by LIANGSE on 2016/6/20.
 */
public class HomeDetailProtocol extends BaseProtocol<AppInfo> {
    public String packageName;

    public HomeDetailProtocol(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public String getKey() {
        return "detail";
    }

    @Override
    public String getParams() {
        return "&packageName=" + packageName;
    }

    @Override
    public AppInfo parseJson(String result) {
        Log.i("HomeDetailProtocol", result);
        JSONObject object = null;
        try {
            object = new JSONObject(result);
            AppInfo info = new AppInfo();

            info.name = object.getString("name");
            info.packageName = object.getString("packageName");
            info.iconUrl = (object.getString("iconUrl"));
            info.stars = (Float.parseFloat(object.getString("stars")));
            if (object.has("size")){

                info.setSize(object.getLong("size"));
            }
            info.downloadUrl = (object.getString("downloadUrl"));
            info.des = (object.getString("des"));

            info.author = object.getString("author");
            info.date = object.getString("date");
            info.downloadNum = object.getString("downloadNum");
            info.version = object.getString("version");

            /*解析安全数据*/
            JSONArray arraySafe = object.getJSONArray("safe");
            ArrayList<AppInfo.SafeInfo> safeList = new ArrayList<>();
            for (int i = 0; i < arraySafe.length(); i++) {
                Log.i("HomeDetailProtocol", arraySafe.toString());
                JSONObject obj1 = arraySafe.getJSONObject(i);
                SafeInfo safeInfo = new SafeInfo();
                safeInfo.safeDes = obj1.getString("safeDes");
                safeInfo.safeDesColor = obj1.getString("safeDesColor");
                safeInfo.safeDesUrl = obj1.getString("safeDesUrl");
                safeInfo.safeUrl = obj1.getString("safeUrl");
                safeList.add(safeInfo);
            }
            info.safe = safeList;

            /*解析截图信息*/
            JSONArray arrayScreen = object.getJSONArray("screen");
            ArrayList<String> screenList = new ArrayList<>();
            for (int i = 0; i < arrayScreen.length(); i++) {
                screenList.add(arrayScreen.getString(i));
            }
            info.screen = screenList;

            return info;
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }
}
