package com.example.googleplay.holder;

import android.text.format.Formatter;
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
 * Created by LIANGSE on 2016/5/24.
 */
public class HomeHolder extends BaseHolder<AppInfo> {
    private View mItemView;

    public ImageView mImageIcon;//app图片
    public TextView mTvName;    //app名字
    public RatingBar mRatingBar;//app评分
    public TextView mTvDes;     //app介绍
    public TextView mTvSize;    //app大小

    @Override
    public View initView() {
        mItemView = UIUtils.inflate(R.layout.app_item_list);
        mImageIcon = (ImageView) mItemView.findViewById(R.id.iv_appIcon);
        mTvName = (TextView) mItemView.findViewById(R.id.tv_appName);
        mRatingBar = (RatingBar) mItemView.findViewById(R.id.ratingBar);
        mTvDes = (TextView) mItemView.findViewById(R.id.tv_appDes);
        mTvSize = (TextView) mItemView.findViewById(R.id.tv_AppSize);
        return mItemView;
    }

    @Override
    protected void refreshView(AppInfo data) {
        if (data.getIconUrl()!=null){
            CacheUtils.newInstance().display(mImageIcon, HttpHelper.URL_HOST+"image?name="+data.getIconUrl());
        }
        mTvName.setText(data.getName());
        mRatingBar.setRating( data.getStars());
        mTvDes.setText(data.getDes());
        mTvSize.setText(Formatter.formatFileSize(UIUtils.getContext(),data.getSize()));
    }
}
