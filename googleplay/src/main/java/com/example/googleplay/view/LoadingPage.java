package com.example.googleplay.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.googleplay.R;
import com.example.googleplay.manager.ThreadManager;
import com.example.googleplay.utils.UIUtils;

/**
 * 网络加载的各种状态的封装
 * Created by LIANGSE on 2016/5/25.
 */
public abstract class LoadingPage extends FrameLayout {
    public LoadingPage(Context context) {
        super(context);
        init();
    }

    public LoadingPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadingPage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public static final int STATE_LOAD_UNDO = 1;     //未知状态
    public static final int STATE_LOAD_ERROR = 2;   //加载失败状态
    public static final int STATE_LOAD_EMPTY = 3;   //数据为空状态
    public static final int STATE_LOAD_LOADING = 4; //加载中状态
    public static final int STATE_LOAD_SUCCESS = 5; //加载成功状态

    private int mCurrentState = STATE_LOAD_UNDO; //当前状态默认为未知

    private View mLoadingView; //加载中
    private View mErrorView;   //错误界面
    private View mEmptyView;   //数据为空的界面
    private View mSuccessView; //数据加载成功显示的界面

    /**
     * 初始化不同的View
     */
    private void init() {

        if (mLoadingView == null) {
            mLoadingView = UIUtils.inflate(R.layout.loadingpage_view);
        }
        addView(mLoadingView);

        if (mErrorView == null) {
            mErrorView = UIUtils.inflate(R.layout.error_view);
            Button mBtnError = (Button) mErrorView.findViewById(R.id.btn_error);
            mBtnError.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadData();
                }
            });
        }
        addView(mErrorView);

        if (mEmptyView == null) {
            mEmptyView = UIUtils.inflate(R.layout.empty_view);
        }
        addView(mEmptyView);

        showRightPage();
    }

    /**
     * 根据状态 显示不同的 界面
     */
    private void showRightPage() {

        mLoadingView.setVisibility(mCurrentState==STATE_LOAD_UNDO||mCurrentState == STATE_LOAD_LOADING ? View.VISIBLE : View.GONE);

        mErrorView.setVisibility(mCurrentState == STATE_LOAD_ERROR ? View.VISIBLE : View.GONE);

        mEmptyView.setVisibility(mCurrentState == STATE_LOAD_EMPTY ? View.VISIBLE : View.GONE);

        if (mSuccessView == null && mCurrentState == STATE_LOAD_SUCCESS) {
            mSuccessView = onCreateSuccessView();

            if (mSuccessView!=null){
                addView(mSuccessView);
            }
        }

        if (mSuccessView != null) {
            mSuccessView.setVisibility(mCurrentState == STATE_LOAD_SUCCESS ? View.VISIBLE : View.GONE);
        }

        //Log.i("showRightPage","mLoadingView"+mLoadingView.getVisibility()+";mErrorView"+mErrorView.getVisibility()+";mEmptyView"+mEmptyView.getVisibility());

    }

    /**
     * 执行请求操作
     */
    public void loadData() {

        if (mCurrentState!=STATE_LOAD_LOADING) {

            mCurrentState = STATE_LOAD_LOADING;
            showRightPage();

            ThreadManager.instance().createLongPool().execute(new Runnable() {
                @Override
                public void run() {
                    Log.i("LoadingPage","正在请求数据>>>");
                    mCurrentState = onLoad().getState(); //请求数据

                    UIUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("LoadingPage","刷新UI");
                            showRightPage(); //数据请求完 刷新UI 判断是哪个显示在界面上面
                        }
                    });
                }
            });
        }
    }

    /**
     * 创建界面正常显示的View
     * @return
     */
    public abstract View onCreateSuccessView();

    /**
     * 加载数据 子线程运行
     * @return  请求结果吗
     */

    public abstract ResultState onLoad();

    /**
     * 请求状态的封装
     */
    public enum ResultState {

        STATE_LOADING(STATE_LOAD_LOADING), STATE_ERROR(STATE_LOAD_ERROR), STATE_EMPTY(STATE_LOAD_EMPTY), STATE_SUCCESS(STATE_LOAD_SUCCESS);

        int state;

        private ResultState(int state) {
            this.state = state;
        }

        public int getState() {
            return state;
        }
    }

}
