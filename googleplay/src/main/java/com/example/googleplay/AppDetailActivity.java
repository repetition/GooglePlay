package com.example.googleplay;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.googleplay.domain.AppInfo;
import com.example.googleplay.holder.DetailAppInfoHolder;
import com.example.googleplay.http.protocol.HomeDetailProtocol;
import com.example.googleplay.utils.UIUtils;
import com.example.googleplay.view.LoadingPage;

import static com.example.googleplay.view.LoadingPage.ResultState;

/**
 * Created by LIANGSE on 2016/6/20.
 */
public class AppDetailActivity extends AppCompatActivity {

    private LoadingPage mLoadingPage;
    private String packageName;
    private AppInfo mAppInfo;
    private String appName;
    private LinearLayout mLayoutDetail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //MyApplication.addActivity(this);
        //supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        packageName = getIntent().getStringExtra("packageName");
        appName = getIntent().getStringExtra("appName");

        mLoadingPage = new LoadingPage(UIUtils.getContext()) {
            @Override
            public View onCreateSuccessView() {
                return AppDetailActivity.this.onCreateSuccessView();
            }

            @Override
            public ResultState onLoad() {
                return AppDetailActivity.this.onLoad();
            }
        };

        setContentView(R.layout.app_detail_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(appName);
        getSupportActionBar().setHomeButtonEnabled(true);//设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mLayoutDetail = (LinearLayout) findViewById(R.id.ll_Detail);
/*
        ViewGroup contentFrameLayout = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        View parentView = contentFrameLayout.getChildAt(0);
        if (parentView != null && Build.VERSION.SDK_INT >= 14) {
            parentView.setFitsSystemWindows(true);
        }*/
        /*将loadingPage添加到布局中*/
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mLoadingPage.setLayoutParams(params);
        mLayoutDetail.addView(mLoadingPage);

        //开始加载数据
        mLoadingPage.loadData();
    }

    private View onCreateSuccessView() {


        //创建成功的布局
        // View view = UIUtils.inflate(R.layout.detail_appinfo);

        FrameLayout detailInfo = new FrameLayout(AppDetailActivity.this);
        detailInfo.setBackgroundResource(R.drawable.app_item_bg_selector);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int margins = (int) UIUtils.dip2px(3);
        params.setMargins(margins,margins,margins,margins);
        detailInfo.setLayoutParams(params);

        //初始化应用信息模块
        //FrameLayout flDetailAppInfo = (FrameLayout) view.findViewById(R.id.fl_detail_appinfo);
        //动态加添
        DetailAppInfoHolder detailAppInfoHolder = new DetailAppInfoHolder();
        detailAppInfoHolder.setData(mAppInfo);
        detailInfo.addView(detailAppInfoHolder.getItemView());

        return detailInfo;
    }

    /**
     * 请求网络加载数据
     *
     * @return
     */
    private ResultState onLoad() {

        HomeDetailProtocol protocol = new HomeDetailProtocol(packageName);
        mAppInfo = protocol.getData(0);
        if (mAppInfo != null) {
            return ResultState.STATE_SUCCESS;
        }
        return ResultState.STATE_ERROR;
    }
}