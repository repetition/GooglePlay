package com.example.googleplay.http.protocol;

import android.util.Log;

import com.example.googleplay.domain.CategoryInfoBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LIANGSE on 2016/6/3.
 */
public class CategoryProtocol extends BaseProtocol<List<CategoryInfoBean>> {
    @Override
    public List<CategoryInfoBean> parseJson(String result) {

        Log.i("CategoryProtocol", result);
        List<CategoryInfoBean> mCategoryInfoBeanList = new ArrayList<>();
        try {
            JSONArray categoryArray = new JSONArray(result);
            for (int i = 0; i < categoryArray.length(); i++) {

                JSONObject object = categoryArray.getJSONObject(i);
                if (object.has("title")) {
                    String title = object.getString("title");
                    CategoryInfoBean bean = new CategoryInfoBean();
                    bean.title = title;
                    bean.isTitle = true;
                    mCategoryInfoBeanList.add(bean);
                }

                if (object.has("infos")) {
                    JSONArray array = object.getJSONArray("infos");

                    for (int j = 0; j < array.length(); j++) {

                        JSONObject infos = array.getJSONObject(j);
                        CategoryInfoBean info = new CategoryInfoBean();
                        info.name1 = infos.getString("name1");
                        info.name2 = infos.getString("name2");
                        info.name3 = infos.getString("name3");
                        info.url1 = infos.getString("url1");
                        info.url2 = infos.getString("url2");
                        info.url3 = infos.getString("url3");
                        info.isTitle = false;
                        mCategoryInfoBeanList.add(info);
                    }
                }
            }

            return mCategoryInfoBeanList;

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    public String getParams() {
        return "";
    }

    @Override
    public String getKey() {
        return "category";
    }
}
