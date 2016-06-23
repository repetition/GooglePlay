package com.example.googleplay.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.googleplay.http.HttpHelper;
import com.example.googleplay.utils.CacheUtils;
import com.example.googleplay.utils.UIUtils;

import java.util.ArrayList;

import static android.widget.ImageView.ScaleType;

/**
 * Created by LIANGSE on 2016/6/5.
 */
public class HomeHeaderAdapter extends PagerAdapter {
    private ArrayList<String> mStringArrayList;

 //   private static List<ImageView> mImageViewList = new ArrayList<>();

    public HomeHeaderAdapter(ArrayList<String> list){
        mStringArrayList = list;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        /*取模,防止角标越界*/
        position = position%mStringArrayList.size();


        ImageView view = null;
            view = new ImageView(UIUtils.getContext());
            view.setScaleType(ScaleType.FIT_XY);
            CacheUtils.newInstance().display(view, HttpHelper.URL_HOST+"image?name="+mStringArrayList.get(position));
        container.addView(view);
        return view;
    }
}
