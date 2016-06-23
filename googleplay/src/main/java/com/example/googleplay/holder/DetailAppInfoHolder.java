package com.example.googleplay.holder;

import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.googleplay.R;
import com.example.googleplay.domain.AppInfo;
import com.example.googleplay.http.HttpHelper;
import com.example.googleplay.utils.CacheUtils;
import com.example.googleplay.utils.UIUtils;

/**
 * Created by LIANGSE on 2016/6/21.
 */
public class DetailAppInfoHolder extends BaseHolder<AppInfo> {
    public ImageView mImageIcon;//app图片
    public TextView mTvName;    //app名字
    public RatingBar mRatingBar;//app评分
    public TextView mTvDownNum;  //app下载量
    public TextView mTvSize;    //app大小
    public TextView mTvVersion;
    private TextView mTvDate;

    @Override
    public View initView() {
        View view = UIUtils.inflate(R.layout.detail_appinfo);

        mImageIcon = (ImageView) view.findViewById(R.id.iv_appIcon);
        mTvName = (TextView) view.findViewById(R.id.tv_appName);
        mRatingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        mTvDownNum = (TextView) view.findViewById(R.id.tv_appDownNum);
        mTvSize = (TextView) view.findViewById(R.id.tv_appSize);
        mTvVersion = (TextView) view.findViewById(R.id.tv_appVersion);
        mTvDate = (TextView) view.findViewById(R.id.tv_appDate);
        return view;
    }

    @Override
    protected void refreshView(AppInfo data) {

        Log.i("DetailAppInfoHolder",data.getSize()+"");

        if (data.getIconUrl() != null) {
            CacheUtils.newInstance().display(mImageIcon, HttpHelper.URL_HOST + "image?name=" + data.getIconUrl());
        }
        mTvName.setText(data.name);
        mRatingBar.setRating(data.stars);
        mTvDownNum.setText("下载量: "+data.downloadNum);
        mTvSize.setText(Formatter.formatFileSize(UIUtils.getContext(),data.getSize()));
        mTvVersion.setText("版本: "+data.version);
        mTvDate.setText(data.date);
      /*  SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd");
        Date date = new Date(Long.valueOf(data.date));
        format.format(date);*/

    }
}
