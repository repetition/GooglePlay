package com.example.googleplay.domain;

import android.os.Environment;

import com.example.googleplay.manager.DownloadManager;

import java.io.File;

/**
 * Created by LIANGSE on 2016/6/15.
 */
public class DownloadInfo {

    public String id;
    public String name;
    public long size;
    public String packageName;
    public String downloadUrl;

    public String path;

    public long currentPos;
    public int currentState;

    public static final String GOOGLE_MARKET = "google_market";
    public static final String DOWNLOAD = "download";


    /*获取当前下载进度*/
    public float getProgress() {
        if (size == 0) {
            return 0;
        }
        float progress = currentPos / size;
        return  progress;
    }


    public static DownloadInfo copy(AppInfo appInfo){
        DownloadInfo downloadInfo = new DownloadInfo();
        downloadInfo.id = String.valueOf(appInfo.id);
        downloadInfo.name = appInfo.name;
        downloadInfo.packageName = appInfo.packageName;
        downloadInfo.downloadUrl = appInfo.downloadUrl;

        downloadInfo.currentPos = 0;    //默认下砸进度为0
        downloadInfo.currentState = DownloadManager.STATE_UNDO; //默认状态为未知

        downloadInfo.path = downloadInfo.getFilePath();

        return downloadInfo;

    }


    /**
     * 获取下载路径
     *
     * @return
     */
    public String getFilePath() {
        String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
        StringBuffer sb = new StringBuffer();

        sb.append(sdcard);
        sb.append(File.separator);
        sb.append(GOOGLE_MARKET);
        sb.append(File.separator);
        sb.append(DOWNLOAD);

        if (crateDir(sb.toString())) {
            return sb.toString()+File.separator+name+".apk";
        }

        return null;
    }

    /**
     * 创建下载路径
     *
     * @param path
     * @return
     */
    private boolean crateDir(String path) {

        File fileDir = new File(path);

        if (!fileDir.exists() && !fileDir.isDirectory()) {
            boolean success = fileDir.mkdirs();
            if (success) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }

    }

}
