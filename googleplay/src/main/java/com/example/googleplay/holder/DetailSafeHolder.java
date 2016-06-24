package com.example.googleplay.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.googleplay.R;
import com.example.googleplay.domain.AppInfo;
import com.example.googleplay.http.HttpHelper;
import com.example.googleplay.utils.CacheUtils;
import com.example.googleplay.utils.UIUtils;

import java.util.ArrayList;

/**
 * Created by v_bzhzhang on 2016/6/24.
 */
public class DetailSafeHolder extends BaseHolder<AppInfo> {

    private ImageView[] mSafeIcons;
    private ImageView[] mDesIcons;
    private TextView[] mDesTexts;
    private LinearLayout[] mSafeDesBars;

    @Override
    public View initView() {

        View view = UIUtils.inflate(R.layout.detail_safeinfo);

        mSafeIcons = new ImageView[4];
        mSafeIcons[0] = (ImageView) view.findViewById(R.id.iv_safe1);
        mSafeIcons[1] = (ImageView) view.findViewById(R.id.iv_safe2);
        mSafeIcons[2] = (ImageView) view.findViewById(R.id.iv_safe3);
        mSafeIcons[3] = (ImageView) view.findViewById(R.id.iv_safe4);

        mDesIcons = new ImageView[4];
        mDesIcons[0] = (ImageView) view.findViewById(R.id.iv_des1);
        mDesIcons[1] = (ImageView) view.findViewById(R.id.iv_des2);
        mDesIcons[2] = (ImageView) view.findViewById(R.id.iv_des3);
        mDesIcons[3] = (ImageView) view.findViewById(R.id.iv_des4);

        mDesTexts = new TextView[4];
        mDesTexts[0] = (TextView) view.findViewById(R.id.tv_des1);
        mDesTexts[1] = (TextView) view.findViewById(R.id.tv_des2);
        mDesTexts[2] = (TextView) view.findViewById(R.id.tv_des3);
        mDesTexts[3] = (TextView) view.findViewById(R.id.tv_des4);


        mSafeDesBars = new LinearLayout[4];
        mSafeDesBars[0] = (LinearLayout) view.findViewById(R.id.ll_des1);
        mSafeDesBars[1] = (LinearLayout) view.findViewById(R.id.ll_des2);
        mSafeDesBars[2] = (LinearLayout) view.findViewById(R.id.ll_des3);
        mSafeDesBars[3] = (LinearLayout) view.findViewById(R.id.ll_des4);

        return view;
    }

    @Override
    protected void refreshView(AppInfo data) {

        ArrayList<AppInfo.SafeInfo> safeInfos = data.safe;
        //写成4 遍历的时候,有就显示,没有就隐藏
        for (int i = 0; i < 4; i++) {
            if (i < safeInfos.size()) {
                AppInfo.SafeInfo safeInfo = safeInfos.get(i);
                CacheUtils.newInstance().display(mSafeIcons[i], HttpHelper.URL_HOST + "image?name=" + safeInfo.safeUrl);

                CacheUtils.newInstance().display(mDesIcons[i], HttpHelper.URL_HOST + "image?name=" + safeInfo.safeDesUrl);
                mDesTexts[0].setText(safeInfo.safeDes);

            } else {
                mSafeIcons[i].setVisibility(View.GONE);
                mSafeDesBars[i].setVisibility(View.GONE);
            }
        }

    }
}
