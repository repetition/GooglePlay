package com.example.googleplay.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by LIANGSE on 2016/2/29.
 */
public class FileCache {

    public FileCache() {
        createFileDir();//创建文件夹
    }

    /**
     * 将图片保存到内存卡中
     *
     * @param key
     */
    public void setBitmapForFile(String key, Bitmap bitmap) {
        /**将图片保存到内存卡中*/
        File bitmapFile = new File(createFileDir(), getBitmapName(key));
        if (!bitmapFile.exists()) {
            Log.i("setBitmapForFile", "文件名:" + getBitmapName(key));
            FileOutputStream fos = null;
            try {
                //开启输出流写入文件
                fos = new FileOutputStream(bitmapFile);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                Log.i("setBitmapForFile", "调用了");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    /**
     * 获取一个位图
     *
     * @param key 位图名字
     * @return 返回这个名字的位图
     */
    public Bitmap getBitmapForFile(String key) {
        File bitmapFile = new File(createFileDir(), getBitmapName(key));
        Bitmap bitmap;
        //判断如果文件不存在就跳出
        if (!bitmapFile.exists()) {
            return null;
        }
        bitmap = BitmapFactory.decodeFile(bitmapFile.getAbsolutePath());
        return bitmap;
    }

    /**
     * 创建文件夹
     *
     * @return 返回创建的文件夹
     */
    public File createFileDir() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/GooglePlay/Cache/ImageCache");
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            if (!file.exists()) {
                file.mkdirs();
                return file;
            } else {
                return file;
            }
        }
        return null;
    }

    /**
     * 从url中得到bitmap的名字
     *
     * @param url
     * @return
     */
    public String getBitmapName(String url) {
        //得到连接/字符之后的文字
        //int index = url.lastIndexOf("app/");

        //return MD5.GetMD5Code(url.substring(index + 1));//将结尾的名字返回过去
        return MD5.GetMD5Code(url);//将结尾的名字返回过去

    }

}
