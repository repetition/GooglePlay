package com.example.googleplay.holder;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.googleplay.R;
import com.example.googleplay.domain.CategoryInfoBean;
import com.example.googleplay.http.HttpHelper;
import com.example.googleplay.utils.CacheUtils;
import com.example.googleplay.utils.UIUtils;

/**
 * Created by LIANGSE on 2016/6/4.
 */
public class CategoryInfoHolder extends BaseHolder<CategoryInfoBean> implements View.OnClickListener {

    private View mView;

    private TextView mTextView1;
    private TextView mTextView2;
    private TextView mTextView3;

    private ImageView mImageView1;
    private ImageView mImageView2;
    private ImageView mImageView3;
    private LinearLayout mLl1;
    private LinearLayout mLl2;
    private LinearLayout mLl3;

    @Override
    public View initView() {
        mView = UIUtils.inflate(R.layout.list_item_category_info);

        mTextView1 = (TextView) mView.findViewById(R.id.tv1);
        mTextView2 = (TextView) mView.findViewById(R.id.tv2);
        mTextView3 = (TextView) mView.findViewById(R.id.tv3);

        mImageView1 = (ImageView) mView.findViewById(R.id.iv1);
        mImageView2 = (ImageView) mView.findViewById(R.id.iv2);
        mImageView3 = (ImageView) mView.findViewById(R.id.iv3);

        mLl1 = (LinearLayout) mView.findViewById(R.id.ll1);
        mLl2 = (LinearLayout) mView.findViewById(R.id.ll2);
        mLl3 = (LinearLayout) mView.findViewById(R.id.ll3);

        mLl1.setOnClickListener(this);
        mLl2.setOnClickListener(this);
        mLl3.setOnClickListener(this);

        return mView;
    }

    @Override
    protected void refreshView(CategoryInfoBean data) {

        Log.i("CategoryInfoHolder", "返回的数据:" + data.toString());
        mTextView1.setText(data.name1);
        mTextView2.setText(data.name2);
        mTextView3.setText(data.name3);
        CacheUtils.newInstance().display(mImageView1, HttpHelper.URL_HOST + "image?name=" + data.url1);
        CacheUtils.newInstance().display(mImageView2, HttpHelper.URL_HOST + "image?name=" + data.url2);
        CacheUtils.newInstance().display(mImageView3, HttpHelper.URL_HOST + "image?name=" + data.url3);

    }

    @Override
    public void onClick(View v) {
       CategoryInfoBean info =  getData();
        switch (v.getId()) {
            case R.id.ll1:
                Toast.makeText(UIUtils.getContext(),info.name1,Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll2:
                Toast.makeText(UIUtils.getContext(),info.name2,Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll3:
                Toast.makeText(UIUtils.getContext(),info.name3,Toast.LENGTH_SHORT).show();
                break;
        }

    }
}
