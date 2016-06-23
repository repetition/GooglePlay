package com.example.googleplay.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.googleplay.utils.UIUtils;
import com.example.googleplay.view.LoadingPage;

import java.util.ArrayList;
import java.util.List;

import static com.example.googleplay.view.LoadingPage.ResultState;

/**
 * Created by LIANGSE on 2016/3/11.
 */
public abstract class BaseFragment<T> extends Fragment {

    private LoadingPage mLoadingPage;
    /**
     * 标识首页第一次可见时是否已经加载了数据
     */
    private boolean isLoadData = false;
    private int pageType = -1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            pageType = bundle.getInt("pageType");
        }
        /**
         * mLoadingPage从这次创建,防止view没有创建完成而去加载数据
         */
        mLoadingPage = new LoadingPage(UIUtils.getContext()) {

            @Override
            public View onCreateSuccessView() {
                return BaseFragment.this.onCreateSuccessView();
            }

            @Override
            public ResultState onLoad() {
                return BaseFragment.this.onLoad();
            }
        };

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return mLoadingPage;
    }

    /**
     * 创建
     *
     * @return
     */
    public abstract View onCreateSuccessView();
    /*加载数据,子线程中运行*/
    public abstract ResultState onLoad();

    /**
     * 请求数据
     */
    public void loadData() {

        if (mLoadingPage != null) {
            mLoadingPage.loadData();
        }
    }


    public ResultState checkData(List<T> data) {

        if (data != null) {
            Log.i("BaseFragment", "------------------检查json");

            if (data instanceof ArrayList) {

                if (((ArrayList) data).size() > 0) {
                    Log.i("BaseFragment", "------------------检查成功,刷新UI");
                    Log.i("BaseFragment","解析数据:"+data.toString());
                    return ResultState.STATE_SUCCESS;
                }

                if (((ArrayList) data).size() <= 0) {
                    return ResultState.STATE_EMPTY;
                }
            }
        }
        return ResultState.STATE_ERROR;
    }

    /**
     * 获取Fragment显示隐藏的状态,从而回到方法,来进行加载数据 懒加载
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //判断view创建完成和显示的时候调用回调方法
        if (getUserVisibleHint()) {
            onVisible();
        } else {
        }
    }

    /**
     * 首页首次可见的时候去加载数据,解决  LoadingPage没有创建完 就去请求数据 bug
     */
    private void onVisible() {
        if (mLoadingPage != null && pageType == 0) {
            if (!isLoadData) {
                mLoadingPage.loadData();
                isLoadData=true;
                UIUtils.getPreferences("appConfig").edit().putBoolean("isFirstLoadData"+pageType,true).commit();
            }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isLoadData=false;
    }
}
