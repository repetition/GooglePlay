package com.example.googleplay.http.protocol;

import android.os.Environment;
import android.text.format.Formatter;
import android.util.Log;

import com.example.googleplay.http.HttpHelper;
import com.example.googleplay.utils.IOUtils;
import com.example.googleplay.utils.TextUtils;
import com.example.googleplay.utils.UIUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 数据请求,解析json 基类
 * Created by LIANGSE on 2016/5/27.
 */
public abstract class BaseProtocol<T> {
    private String mCacheKey;

    /**
     * 请求数据
     *
     * @param index 分页
     * @return
     */
    public T getData(int index) {
        String url = HttpHelper.URL_HOST + getKey() + "?index=" + index + getParams();
        mCacheKey = getKey() + "?index=" + index+getParams();
        String result = "";
        Log.i("BaseProtocol", "---------是否有缓存");
        result = getCache(mCacheKey);     //从缓存读取
        if (result == null) {       //缓存没有就访问网络
            result = getDataFromServer(index);
        }
        if (!TextUtils.isEmpty(result)) {
            return parseJson(result);
        }
        createFileDir();
        return null;
    }


    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    public String getCache(String key) {
        File fileCache = new File(createFileDir(), key);

        if (fileCache.exists()) {
            Log.i("BaseProtocol", "---------有缓存,判断是否过期");
            FileReader reader = null;
            BufferedReader br = null;
            String result = "";
            try {
                reader = new FileReader(fileCache);
                br = new BufferedReader(reader);
                String time = br.readLine();
                if (null != time) {
                    long deadLine = Long.parseLong(time);  //获取有效期
                    if (System.currentTimeMillis() < deadLine) {       //判断有效期
                        Log.i("BaseProtocol", "---------缓存没有过期,读取缓存---------");
                        String line;
                        while ((line = br.readLine()) != null) {
                            result += line;
                        }
                        Log.i("BaseProtocol", "---------读取缓存完毕------");
                        return result;
                    } else {
                        Log.i("BaseProtocol", "---------缓存过期,请求网络");
                        return null;
                    }
                } else {
                    Log.i("BaseProtocol", "---------读取缓存失败,请求网络");
                    return null;
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                IOUtils.close(reader);
                IOUtils.close(br);
            }
        }
        Log.i("BaseProtocol", "---------没有缓存,请求网络");
        return null;
    }

    /**
     * 设置缓存
     *
     * @param key
     * @param value
     */
    public void setCache(String key, String value) {

        File fileCache = new File(createFileDir(), key);
        FileWriter writer = null;
        try {
            writer = new FileWriter(fileCache);

            long deadLine = System.currentTimeMillis() + 10 * 60 * 1000;//过期时间  10分钟
            writer.write(deadLine + "\n");
            writer.write(value);
            Log.i("BaseFragment", "---------缓存写入成功");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(writer);
        }
    }

    /**
     * 从服务器请求数据
     *
     * @param index 分页
     * @return
     */
    public String getDataFromServer(int index) {
        String url = HttpHelper.URL_HOST + getKey() + "?index=" + index + getParams();
        Log.i("BaseProtocol", "---------请求的地址:" + url);

        String result = HttpHelper.Get(url);        //请求从网路数据

        if (result != null) {
            if (!TextUtils.isEmpty(result)) {
                Log.i("BaseProtocol", "---------请求成功");
                Log.i("BaseProtocol", "---------写入缓存>>" + mCacheKey + ":" + Formatter.formatFileSize(UIUtils.getContext(), result.length()));
                setCache(mCacheKey, result);
            }

            return result;

        } else {
            Log.i("BaseProtocol", "---------网络异常");
            return null;
        }
    }

    /**
     * 解析json数据
     * @param result
     * @return
     */
    public abstract T parseJson(String result);

    /**
     * 获取URL参数
     * @return
     */
    public abstract String getParams();

    /**
     * 获取每个页卡的请求参数
     * @return
     */
    public abstract String getKey();


    /**
     * 创建文件夹
     *
     * @return 返回创建的文件夹
     */
    public File createFileDir() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/GooglePlay/Cache/TextCache");
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            if (!file.exists()) {
                boolean isCreate = file.mkdirs();
                Log.i("BaseProtocol", "缓存文件夹是否创建:" + isCreate);
                //Toast.makeText(UIUtils.getContext(),"文件夹是否创建:"+isCreate,Toast.LENGTH_SHORT).show();
                return file;
            } else {
                return file;
            }
        }
        return null;
    }
}
