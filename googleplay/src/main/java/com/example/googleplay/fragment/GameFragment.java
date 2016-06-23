package com.example.googleplay.fragment;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.googleplay.R;
import com.example.googleplay.utils.UIUtils;

import static com.example.googleplay.view.LoadingPage.ResultState;

/**
 * Created by LIANGSE on 2016/3/7.
 */
public class GameFragment extends BaseFragment {
    private View view;

    private View errorView;
    private ProgressBar mProgressBar;
    private ListView mListView;

    private boolean isRequestData = true;//是否请求完数据
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgressBar.setVisibility(View.GONE);
            //HomeAdapter adapter = new HomeAdapter(getActivity());
//            mListView.setAdapter(adapter);
//            mListView.setVisibility(View.VISIBLE);
            isRequestData = false;
        }
    };


    @Override
    public View onCreateSuccessView() {
        view = UIUtils.inflate(R.layout.game_fragment);

        return view;
    }

    @Override
    public ResultState onLoad() {
        return ResultState.STATE_EMPTY;
    }
}
