package com.example.googleplay.holder;

import android.view.View;

/**
 * Created by LIANGSE on 2016/5/24.
 */
public abstract class BaseHolder<T> {
    private View mItemView;

    private T data;

    public BaseHolder(){
        if (mItemView==null){
            mItemView = initView();
            mItemView.setTag(this);
        }
    }

    public abstract View initView();

    /**
     * 将数据传递过来 给控件设置数据
     * @param data
     */
    public  void setData(T data) {
        this.data = data;
        //回调刷新界面的方法
        refreshView(data);
    }

    public T getData() {
        return data;
    }

    /**
     * 给控件设置数据,子类必须实现
     * @param data
     */
    protected abstract void refreshView(T data);

    public View getItemView() {
        return mItemView;
    }
}
