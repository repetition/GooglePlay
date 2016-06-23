package com.example.googleplay.utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Created by LIANGSE on 2016/2/28.
 */
public class MemoryCache {

    public static LruCache<String, Bitmap> mLruCache;

    public MemoryCache() {
        long maxMemory = Runtime.getRuntime().maxMemory();
        long maxSize = maxMemory / 8;
        mLruCache = new LruCache<String, Bitmap>((int) maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    /**
     * 将图片添加到内存缓存中
     *
     * @param key    添加的键
     * @param bitmap 添加的值 Value
     */
    public void setBitmapForCache(String key, Bitmap bitmap) {
        if (mLruCache.get(key) == null) {
            mLruCache.put(key, bitmap);
        }
    }

    /**
     * 根据key,将对应的bitmap返回
     *
     * @param key
     * @return
     */
    public Bitmap getBitmapForCache(String key) {
        return mLruCache.get(key);
    }
}
