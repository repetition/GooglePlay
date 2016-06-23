package com.example.googleplay.fragment;

import android.view.View;

import com.example.googleplay.R;
import com.example.googleplay.utils.UIUtils;
import com.example.googleplay.view.LoadingPage;

/**
 * Created by LIANGSE on 2016/3/7.
 */
public class SubjectFragment extends BaseFragment {

    @Override
    public View onCreateSuccessView() {
        View view = UIUtils.inflate(R.layout.subject_fragment);

        return view;
    }

    @Override
    public LoadingPage.ResultState onLoad() {
        return LoadingPage.ResultState.STATE_EMPTY;
    }
}
