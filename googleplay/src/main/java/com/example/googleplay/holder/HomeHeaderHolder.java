package com.example.googleplay.holder;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.googleplay.MyApplication;
import com.example.googleplay.R;
import com.example.googleplay.adapter.HomeHeaderAdapter;
import com.example.googleplay.utils.UIUtils;

import java.util.ArrayList;

/**
 * Created by LIANGSE on 2016/6/5.
 */
public class HomeHeaderHolder extends BaseHolder<ArrayList<String>> {

    private ViewPager mPager;
    private LinearLayout mDotRoot;

    @Override
    public View initView() {
        /*创建跟布局*/
        RelativeLayout relativeLayout = new RelativeLayout(UIUtils.getContext());
        /*创建header的属性*/
        AbsListView.LayoutParams paramsRoot = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                (int) UIUtils.dip2px(150));
        relativeLayout.setLayoutParams(paramsRoot);

        /*创建ViewPager*/
        mPager = new ViewPager(UIUtils.getContext());
        /*创建ViewPager的属性*/
        RelativeLayout.LayoutParams pagePrams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        relativeLayout.addView(mPager, pagePrams);


        /*初始化指示器*/
        mDotRoot = new LinearLayout(UIUtils.getContext());
        mDotRoot.setOrientation(LinearLayout.HORIZONTAL);
        //设置内填充
        int padding = (int) UIUtils.dip2px(10);
        mDotRoot.setPadding(padding, padding, padding, padding);

        RelativeLayout.LayoutParams dotParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        dotParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        dotParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        relativeLayout.addView(mDotRoot, dotParams);

        return relativeLayout;
    }

    @Override
    protected void refreshView(final ArrayList<String> data) {
        mPager.setAdapter(new HomeHeaderAdapter(data));

        /*添加指示器*/
        int dotCount = data.size();
        for (int i = 0; i < dotCount; i++) {

            ImageView view = new ImageView(UIUtils.getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            if (i != 0) {
                params.leftMargin = (int) UIUtils.dip2px(3);
            }
            view.setBackgroundResource(R.drawable.indicator_normal);
            mDotRoot.addView(view, params);

        }
        //首次打开应用,更换dot显示
        switchDot(mPager.getCurrentItem() % data.size());

        HomeHeaderTask task = new HomeHeaderTask();
        task.start();

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //图片每次更换就切换指示器
                switchDot(position % data.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    /**
     * 根据ViewPager的图片切换,来改变dot显示
     *
     * @param position
     */
    public void switchDot(int position) {

        for (int i = 0; i < mDotRoot.getChildCount(); i++) {
            ImageView view = (ImageView) mDotRoot.getChildAt(i);
            if (i == position) {
                view.setBackgroundResource(R.drawable.indicator_selected);
            } else {
                view.setBackgroundResource(R.drawable.indicator_normal);
            }
        }
    }

    /**
     * 图片轮播线程
     */
    class HomeHeaderTask implements Runnable {

        public void start() {
            UIUtils.getHandler().removeCallbacksAndMessages(this);
            UIUtils.getHandler().postDelayed(this, 4000);
        }

        @Override
        public void run() {

            if (MyApplication.isRun()) {
                //isRun=true 才去滑动轮播图
                int position = mPager.getCurrentItem();
                mPager.setCurrentItem(position + 1);
            }
            UIUtils.getHandler().postDelayed(this, 4000);
        }
    }

}
