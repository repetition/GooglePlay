package com.example.googleplay.manager;

import android.content.Intent;
import android.net.Uri;

import com.example.googleplay.domain.AppInfo;
import com.example.googleplay.domain.DownloadInfo;
import com.example.googleplay.http.HttpHelper;
import com.example.googleplay.utils.UIUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 下载管理者
 * Created by LIANGSE on 2016/6/18.
 */
public class DownloadManager {

    /*观察者集合*/
    private ArrayList<DownloadObserver> mDownloadObservers = new ArrayList<>();
    /*下载对象集合*/
    private HashMap<String, DownloadInfo> mDownloadInfoMap = new HashMap<>();
    /*下载任务集合*/
    private HashMap<String, DownloadTask> mDownloadTaskMap = new HashMap<>();

    public static final int STATE_UNDO = 0;     //未知状态
    public static final int STATE_WAITING = 1;  //等待下载
    public static final int STATE_DOWNLOADING = 2;  //下载中
    public static final int STATE_ERROR = 3;    //下载失败,错误
    public static final int STATE_UPAUSE = 4;   //暂停下载
    public static final int STATE_USUCCESS = 5;   //下载成功

    public static DownloadManager mDownloadManager = new DownloadManager();

    public DownloadManager getInstance() {
        return mDownloadManager;
    }

    public DownloadManager() {

    }

    /**
     * 注册观察者
     *
     * @param observer
     */
    public void registerDownloadObserver(DownloadObserver observer) {
        if (observer!=null&&!mDownloadObservers.contains(observer)) {
            mDownloadObservers.add(observer);
        }

    }

    /**
     * 注销观察者
     */
    public void unRegisterDownloadObserver(DownloadObserver observer) {
        if (observer!=null&&mDownloadObservers.contains(observer)) {
            mDownloadObservers.remove(observer);
        }
    }

    /**
     * 通知观察者下载状态发生改变
     *
     * @param downloadInfo
     */
    public void notifyDownloadStateChanged(DownloadInfo downloadInfo) {
        for (DownloadObserver observer : mDownloadObservers) {
            observer.onDownloadStateChanged(downloadInfo);
        }
    }

    /**
     * 通知观察者下载状态发生改变
     *
     * @param info
     */
    public void notifyDownloadProgressChanged(DownloadInfo info) {
        for (DownloadObserver observer : mDownloadObservers) {
            observer.onDownloadProgressChanged(info);
        }
    }

    /**
     * 下载
     *
     * @param appInfo
     */
    public void download(AppInfo appInfo) {

        /**
         * 如果任务已经在下载中,就不再添加,保证每个任务只下载一次
         */
        DownloadInfo downloadInfo = mDownloadInfoMap.get(String.valueOf(appInfo.id));
        if (downloadInfo == null) {
            downloadInfo = DownloadInfo.copy(appInfo);
        }

        downloadInfo.currentState = STATE_WAITING;   //将状态切换为等在下载
        //通知观察者 ,下载状态发生改变
        notifyDownloadStateChanged(downloadInfo);
        //将下载对象 维护起来
        mDownloadInfoMap.put(String.valueOf(appInfo.id), downloadInfo);

        //执行下载
        DownloadTask task = new DownloadTask(downloadInfo);
        ThreadManager.instance().createLongPool().execute(task);

        //将下载任务放到集合中
        mDownloadTaskMap.put(String.valueOf(appInfo.id), task);
    }

    /**
     * 暂停下载
     *
     * @param appInfo
     */
    public void pause(AppInfo appInfo) {
        DownloadInfo downloadInfo = mDownloadInfoMap.get(String.valueOf(appInfo.id));

        if (downloadInfo != null) {

            if (downloadInfo.currentState==STATE_WAITING||downloadInfo.currentState==STATE_DOWNLOADING) {
                //只有在等待下载,或者下载中 才暂停任务

                //移除下载任务
                DownloadTask task = mDownloadTaskMap.get(String.valueOf(appInfo.id));
                if (task != null) {
                    ThreadManager.instance().createLongPool().cancel(task);
                }

                downloadInfo.currentState = STATE_UPAUSE;  //当前状态变为暂停
                notifyDownloadStateChanged(downloadInfo);
            }

        }
    }

    /**
     * 安装
     */
    public void install(AppInfo appInfo) {

        DownloadInfo downloadInfo = mDownloadInfoMap.get(String.valueOf(appInfo.id));
        if (downloadInfo != null && downloadInfo.currentState == STATE_USUCCESS) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://" + downloadInfo.packageName), "application/vnd.android.package-archive");
            UIUtils.getContext().startActivity(intent);

        }
    }


    public interface DownloadObserver {

        /*下载状态改变*/
        void onDownloadStateChanged(DownloadInfo downloadInfo);

        /*下载进度改变*/
        void onDownloadProgressChanged(DownloadInfo downloadInfo);
    }

    /**
     * 下载人任务线程
     */
    class DownloadTask implements Runnable {

        public DownloadInfo info;

        public DownloadTask(DownloadInfo info) {
            this.info = info;
        }


        @Override
        public void run() {
            info.currentState = STATE_DOWNLOADING;  //下载状态变为开始下载  通知观察者
            notifyDownloadStateChanged(info);

            InputStream is = null;
            File file = new File(info.getFilePath());
            if (!file.exists() && file.length() == 0 && info.currentPos == 0) {
                /*从头下载*/
                try {
                    URL url = new URL(HttpHelper.URL_HOST + "download?name=" + info.downloadUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(5000);
                    connection.setConnectTimeout(5000);
                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        info.size = connection.getContentLength();
                        is = connection.getInputStream();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                /*断点续传*/
                try {
                    URL url = new URL(HttpHelper.URL_HOST + "download?name=" + info.downloadUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(5000);
                    connection.setConnectTimeout(5000);
                    connection.setRequestProperty("Range", "bytes=" + file.length() + info.size);  //请求未下载数据,实现断点续传
                    if (connection.getResponseCode() == HttpURLConnection.HTTP_PARTIAL) {
                        info.size = connection.getContentLength();
                        is = connection.getInputStream();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            /*处理文件下载*/

            if (is != null) {
                /**/
                try {
                    FileOutputStream fos = new FileOutputStream(file, true);   //true  为文件可以叠加  ,实现断点续传
                    byte[] b = new byte[1024];
                    int line = 0;

                    while ((line = is.read(b)) != -1 && info.currentState == STATE_DOWNLOADING) {
                        fos.write(b, 0, line);
                        fos.flush();   //将流刷进文件
                        info.currentPos += line;
                        notifyDownloadProgressChanged(info);  //通知下载进度发生改变
                    }


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                /*下载成功*/
                if (file.length() == info.size) {
                    //文件下载完整
                    info.currentState = STATE_USUCCESS;  //下载陈功,将状态变为陈功状态
                    notifyDownloadStateChanged(info);
                } else if (info.currentState == STATE_UPAUSE) {
                    /*下载过程中,暂停*/
                    //info.currentState = STATE_UPAUSE;  //将当前状态变为暂停
                    notifyDownloadStateChanged(info);
                } else {
                    /*下载失败*/
                    file.delete();  //删除无效文件
                    info.currentState = STATE_ERROR;
                    info.currentPos = 0;   //将下载进度变为0
                    notifyDownloadStateChanged(info);
                }

            } else {
                /*网络异常*/
                file.delete();  //删除无效文件
                info.currentState = STATE_ERROR;
                info.currentPos = 0;
                notifyDownloadStateChanged(info);
            }

            //从集合中移除任务
            mDownloadTaskMap.remove(info.id);

        }
    }
}
