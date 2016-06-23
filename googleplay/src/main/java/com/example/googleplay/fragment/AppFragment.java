package com.example.googleplay.fragment;

import android.view.View;

import com.example.googleplay.R;
import com.example.googleplay.utils.UIUtils;

import static com.example.googleplay.view.LoadingPage.ResultState;

/**
 * Created by LIANGSE on 2016/3/7.
 */
public class AppFragment extends BaseFragment {

    @Override
    public View onCreateSuccessView() {
        View view = UIUtils.inflate(R.layout.app_fragment);

        return view;
    }

    @Override
    public ResultState onLoad() {
        return ResultState.STATE_EMPTY;
    }
}
