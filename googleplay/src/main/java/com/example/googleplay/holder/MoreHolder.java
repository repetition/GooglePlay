package com.example.googleplay.holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.googleplay.R;
import com.example.googleplay.utils.UIUtils;

/**
 * 处理加载更多 页面显示逻辑
 * Created by LIANGSE on 2016/5/30.
 */
public class MoreHolder extends BaseHolder<Integer> {

    public static final int STATE_MORE_MORE = 1;    //加载更多
    public static final int STATE_MORE_ERROR = 2;   //加载失败,请重试
    public static final int STATE_MORE_NONE = 3;    //隐藏加载更多布局
    public static final int STATE_MORE_NO = 4;    //隐藏没有数据了


    private View view;
    private LinearLayout mMoreLoading;
    private TextView mClickLoad;
    private TextView mMoreNone;


    public MoreHolder(boolean hasMore) {

        if (hasMore) {      //是否有加载更多
            setData(STATE_MORE_MORE);
        } else {
            setData(STATE_MORE_NONE);
        }
    }

    @Override
    public View initView() {
        view = UIUtils.inflate(R.layout.more_holder);
        mMoreLoading = (LinearLayout) view.findViewById(R.id.ll_more);
        mClickLoad = (TextView) view.findViewById(R.id.tv_more_error);
        mMoreNone = (TextView) view.findViewById(R.id.tv_more_none);
        return view;
    }

    @Override
    protected void refreshView(Integer data) {
        switch (data) {
            case STATE_MORE_MORE:
                mMoreLoading.setVisibility(View.VISIBLE);
                mClickLoad.setVisibility(View.GONE);
                mMoreNone.setVisibility(View.GONE);
                break;
            case STATE_MORE_ERROR:
                mMoreLoading.setVisibility(View.GONE);
                mClickLoad.setVisibility(View.VISIBLE);
                mMoreNone.setVisibility(View.GONE);
                break;
            case STATE_MORE_NONE:
                mMoreLoading.setVisibility(View.GONE);
                mClickLoad.setVisibility(View.GONE);
                mMoreNone.setVisibility(View.GONE);
                break;
            case STATE_MORE_NO:
                mMoreLoading.setVisibility(View.GONE);
                mClickLoad.setVisibility(View.GONE);
                mMoreNone.setVisibility(View.VISIBLE);
                break;
        }
    }
}
