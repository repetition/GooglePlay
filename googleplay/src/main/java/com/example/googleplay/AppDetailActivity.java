package com.example.googleplay;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.googleplay.domain.AppInfo;
import com.example.googleplay.holder.DetailAppInfoHolder;
import com.example.googleplay.holder.DetailDesInfoHolder;
import com.example.googleplay.holder.DetailSafeHolder;
import com.example.googleplay.holder.DetailScreenHolder;
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
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UIUtils.getContext(), "返回键点击了", Toast.LENGTH_SHORT).show();
                finish();
                AppDetailActivity.this.overridePendingTransition(R.anim.scale_in, R.anim.side_out_frome_right);
            }
        });

        mLayoutDetail = (LinearLayout) findViewById(R.id.ll_Detail);
/*
        ViewGroup contentFrameLayout = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        View parentView = contentFrameLayout.getChildAt(0);
        if (parentView != null && Build.VERSION.SDK_INT >= 14) {
            parentView.setFitsSystemWindows(true);
        }*/
        /*将loadingPage添加到布局中*/
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER_VERTICAL;
        mLoadingPage.setLayoutParams(params);
        mLayoutDetail.addView(mLoadingPage);

        //开始加载数据
        mLoadingPage.loadData();
    }

    private View onCreateSuccessView() {



        //创建成功的布局
        LinearLayout detailRoot = new LinearLayout(AppDetailActivity.this);
        FrameLayout.LayoutParams paramsRoot = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        detailRoot.setLayoutParams(paramsRoot);
        detailRoot.setOrientation(LinearLayout.VERTICAL);

        //初始化应用信息模块
        //动态加添
        DetailAppInfoHolder detailAppInfoHolder = new DetailAppInfoHolder();
        detailAppInfoHolder.setData(mAppInfo);
        View appInfo = detailAppInfoHolder.getItemView();
        appInfo.setBackgroundResource(R.drawable.app_item_bg_selector);
        LinearLayout.LayoutParams appInfoParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int margins = (int) UIUtils.dip2px(3);
        appInfoParams.setMargins(margins, 0, margins, 0);
        appInfo.setLayoutParams(appInfoParams);
        detailRoot.addView(appInfo);

        /*初始化安全描述模块*/
        DetailSafeHolder safeHolder = new DetailSafeHolder();
        safeHolder.setData(mAppInfo);
        View safeDesView = safeHolder.getItemView();
        // TODO: 2016/6/26 使用mipmap下的图片,解决 安全描述 隐藏时,没有全部隐藏
        safeDesView.setBackgroundResource(R.mipmap.list_item_bg_normal);
        LinearLayout.LayoutParams safeDesParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //int margins = (int) UIUtils.dip2px(3);
        safeDesParams.setMargins(margins, 0, margins, margins);
        safeDesView.setLayoutParams(safeDesParams);
        detailRoot.addView(safeDesView);

        /*初始化截图模块*/
        DetailScreenHolder screenHolder = new DetailScreenHolder();
        screenHolder.setData(mAppInfo);
        LinearLayout.LayoutParams screenParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int screenMargins = (int) UIUtils.dip2px(5);
        screenParams.setMargins(0, screenMargins, 0, screenMargins);
        View screenView = screenHolder.getItemView();
        screenView.setLayoutParams(screenParams);
        detailRoot.addView(screenView);

        /*初始化应用介绍模块*/
        DetailDesInfoHolder desInfoHolder = new DetailDesInfoHolder();
        desInfoHolder.setData(mAppInfo);
        View desInfoView = desInfoHolder.getItemView();
        desInfoView.setBackgroundResource(R.mipmap.list_item_bg_normal);
        LinearLayout.LayoutParams desInfoParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //int margins = (int) UIUtils.dip2px(3);
        desInfoParams.setMargins(margins, 0, margins, margins);
        desInfoView.setLayoutParams(desInfoParams);
        detailRoot.addView(desInfoView);

        /*将所有初始化的模块放到scrollView*/
        // TODO: 2016/6/28   在代码里面添加这个滑动的view,解决加载失败时,布局会出现在顶部
        NestedScrollView nestedScrollView = new NestedScrollView(UIUtils.getContext());
        FrameLayout.LayoutParams scrollParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        nestedScrollView.setLayoutParams(scrollParams);
        nestedScrollView.addView(detailRoot);

        return nestedScrollView;
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
