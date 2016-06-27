package com.example.googleplay.holder;

import android.view.View;
import android.widget.ImageView;

import com.example.googleplay.R;
import com.example.googleplay.domain.AppInfo;
import com.example.googleplay.http.HttpHelper;
import com.example.googleplay.utils.CacheUtils;
import com.example.googleplay.utils.UIUtils;

import java.util.ArrayList;

/**
 * Created by LIANGSE on 2016/6/27.
 */
public class DetailScreenHolder extends BaseHolder<AppInfo> {
    private ImageView[] mImageScreens;
    private ArrayList<String> mScreens;

    @Override
    public View initView() {

        View view = UIUtils.inflate(R.layout.detail_screen);

        mImageScreens = new ImageView[5];
        mImageScreens[0] = (ImageView) view.findViewById(R.id.iv_screen1);
        mImageScreens[1] = (ImageView) view.findViewById(R.id.iv_screen2);
        mImageScreens[2] = (ImageView) view.findViewById(R.id.iv_screen3);
        mImageScreens[3] = (ImageView) view.findViewById(R.id.iv_screen4);
        mImageScreens[4] = (ImageView) view.findViewById(R.id.iv_screen5);

        return view;
    }

    @Override
    protected void refreshView(AppInfo data) {
        mScreens = data.screen;

        for (int i = 0; i < mScreens.size(); i++) {
            CacheUtils.newInstance().display(mImageScreens[i], HttpHelper.URL_HOST+"image?name="+mScreens.get(i));
        }
    }
}
