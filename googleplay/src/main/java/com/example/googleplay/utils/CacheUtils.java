package com.example.googleplay.utils;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by LIANGSE on 2016/2/28.
 */
public class CacheUtils {

    public FileCache mFileCache;
    public MemoryCache mMemoryCache;

    public CacheUtils() {
        mFileCache = new FileCache();
        mMemoryCache = new MemoryCache();
    }

    private static CacheUtils mCacheUtils;

    public static CacheUtils newInstance() {
        if (mCacheUtils == null) {
            synchronized (CacheUtils.class) {
                if (mCacheUtils == null) {
                    mCacheUtils = new CacheUtils();
                }
            }
        }
        return mCacheUtils;
    }


    /**
     * 设置图片
     *
     * @param icon
     * @param url
     */
    public void display(ImageView icon, String url) {

        Log.i("CacheUtils","图片Url:"+url);

        if (TextUtils.isEmpty(url)){
            Log.i("CacheUtils","url为空了");
            return;
        }

        Bitmap bitmap = null;
        //*******内存中读取图片,如果没有就从存储中读取****************
        bitmap = mMemoryCache.getBitmapForCache(url);
        if (bitmap != null) {
            icon.setImageBitmap(bitmap);
            Log.i("CacheUtils", "使用的内存缓存");
            return;
        }

        //*******从外置存储读取图片,如果没有就网络下载****************
        bitmap = mFileCache.getBitmapForFile(url);
        if (bitmap != null) {
            icon.setImageBitmap(bitmap);
            mMemoryCache.setBitmapForCache(url, bitmap);//使用了文件缓存时,没存没有图片就将图片添加到内存中
            Log.i("CacheUtils", "使用的文件缓存");
            return;
        }

        //*******从网路下载图片************************************
        MyAsyncTask task = new MyAsyncTask(mMemoryCache, mFileCache);
        icon.setTag(url);//将url绑定imageView
        task.execute(icon, url);
    }

}
