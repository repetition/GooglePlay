package com.example.googleplay.http.protocol;

import android.util.Log;

import com.example.googleplay.domain.AppInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LIANGSE on 2016/5/27.
 */
public class HomeProtocol extends BaseProtocol<List<AppInfo>> {

    private ArrayList<String> mPictureList;

    @Override
    public List<AppInfo> parseJson(String result) {
        Log.i("HomeProtocol", "------------------解析json");

        List<AppInfo> mInfoBeanList = new ArrayList<>();
        if (result != null) {

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("list");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);

                    AppInfo bean = new AppInfo();
                    bean.setName(object.getString("name"));
                    bean.setPackageName(object.getString("packageName"));
                    bean.setIconUrl(object.getString("iconUrl"));
                    bean.setStars(Float.parseFloat(object.getString("stars")));
                    bean.setSize(object.getLong("size"));
                    bean.setDownloadUrl(object.getString("downloadUrl"));
                    bean.setDes(object.getString("des"));
                    mInfoBeanList.add(bean);
                }
                /*解析首页轮播图数据*/
                JSONArray pictureArray = jsonObject.getJSONArray("picture");
                mPictureList = new ArrayList<>();
                for (int i = 0; i < pictureArray.length(); i++) {
                    mPictureList.add(pictureArray.getString(i));
                }

                Log.i("HomeProtocol", "------------------解析json完毕");
                return mInfoBeanList;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.i("HomeProtocol", "------------------json为空");
        return null;
    }


    public ArrayList<String> getPictureList() {
        return mPictureList;
    }

    @Override
    public String getParams() {
        return "";
    }

    @Override
    public String getKey() {
        return "home";
    }

}
