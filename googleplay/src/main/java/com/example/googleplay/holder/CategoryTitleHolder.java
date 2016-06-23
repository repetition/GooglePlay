package com.example.googleplay.holder;

import android.view.View;
import android.widget.TextView;

import com.example.googleplay.R;
import com.example.googleplay.domain.CategoryInfoBean;
import com.example.googleplay.utils.UIUtils;

/**
 * Created by LIANGSE on 2016/6/4.
 */
public class CategoryTitleHolder extends BaseHolder<CategoryInfoBean> {

    private View mView;
    private TextView mMTextTitle;

    @Override
    public View initView() {
        mView = UIUtils.inflate(R.layout.list_tiem_category_title);
        mMTextTitle = (TextView) mView.findViewById(R.id.tv_CategoryTitle);
        return mView;
    }

    @Override
    protected void refreshView(CategoryInfoBean data) {
        mMTextTitle.setText(data.title);
    }
}
