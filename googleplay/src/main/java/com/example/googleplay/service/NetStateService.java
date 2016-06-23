package com.example.googleplay.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.googleplay.utils.NetInfoUtils;

/**
 * Created by LIANGSE on 2016/3/11.
 */
public class NetStateService extends Service {

    public OnNetStateListener onNetStateListener;
    private NetStateBroadcastReceiver receiver;

    public void setOnNetStateListener(OnNetStateListener onNetStateListener) {
        this.onNetStateListener = onNetStateListener;
    }

    public interface OnNetStateListener {
        void onNetState(boolean state);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        /************动态注册广播***********/
        receiver = new NetStateBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);//设置监听广播的Action
        registerReceiver(receiver, filter);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        /************服务销毁时,取消广播接收***********/
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }

    /**
     * 监听网络连接状态的广播接受者
     */
    class NetStateBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //判断接收网络变化广播
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                if (onNetStateListener != null) {
                    if (NetInfoUtils.getNetWorkState()) {
                        //网络可用时,调用接口
                        onNetStateListener.onNetState(true);
                    } else {
                        //网络不可用时,调用接口
                        onNetStateListener.onNetState(false);
                    }
                }
            }

        }
    }

    public class MyBinder extends Binder {
        /**
         * 获取service 实例 以便设置监听 实现网络变化回调
         *
         * @return 返回service实例
         */
        public NetStateService getService() {
            return NetStateService.this;
        }
    }
}
