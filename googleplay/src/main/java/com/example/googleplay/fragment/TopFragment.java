package com.example.googleplay.fragment;

import android.view.View;

import static com.example.googleplay.view.LoadingPage.ResultState;

/**
 * Created by LIANGSE on 2016/3/7.
 */
public class TopFragment extends BaseFragment {
    @Override
    public View onCreateSuccessView() {
        return null;
    }

    @Override
    public ResultState onLoad() {
        return ResultState.STATE_LOADING;
    }

}
