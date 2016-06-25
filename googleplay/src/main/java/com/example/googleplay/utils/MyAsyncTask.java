package com.example.googleplay.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by LIANGSE on 2016/2/28.
 */
public class MyAsyncTask extends AsyncTask<Object, Void, Bitmap> {
    private ImageView icon;
    String url;
    MemoryCache mMemoryCache;//内存缓存
    FileCache mFileCache;//文件缓存

    public MyAsyncTask(MemoryCache memoryCache, FileCache fileCache) {
        mMemoryCache = memoryCache;
        mFileCache = fileCache;
    }

    @Override
    protected Bitmap doInBackground(Object... params) {
        url = (String) params[1];
        icon = (ImageView) params[0];
        /** 下载图片 */
        InputStream is = null;
        Bitmap bitmap;
        try {
            URL http = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) http.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                is = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(is);
                return bitmap;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(is);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        String tag = (String) icon.getTag();
        //如果url于绑定的ImageView的Tag一样就设置图片,防止listview图片错位
        if (tag.equals(url)&&bitmap!=null) {
            icon.setImageBitmap(bitmap);//设置
            Log.i("MyAsyncTask", "从网路下载"+"url:"+url+"bitmap:"+bitmap);

            mMemoryCache.setBitmapForCache(url, bitmap);//将图片添加到内存缓存中
            mFileCache.setBitmapForFile(url, bitmap);//将图片保存到外置存储中
        }

    }
}
