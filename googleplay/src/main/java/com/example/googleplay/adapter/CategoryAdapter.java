package com.example.googleplay.adapter;

import android.util.Log;

import com.example.googleplay.domain.CategoryInfoBean;
import com.example.googleplay.holder.BaseHolder;
import com.example.googleplay.holder.CategoryInfoHolder;
import com.example.googleplay.holder.CategoryTitleHolder;

import java.util.List;

/**
 * 分类 适配器
 * Created by LIANGSE on 2016/6/4.
 */
public class CategoryAdapter extends AppAdapter<CategoryInfoBean> {

    public List<CategoryInfoBean> mCategoryInfoBeanList;

    public CategoryAdapter(List<CategoryInfoBean> list) {
        super(list);
        mCategoryInfoBeanList = list;
        Log.i("CategoryAdapter","mCategoryInfoBeanList"+mCategoryInfoBeanList.size());
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount()+1; //添加一种布局
    }

    @Override
    public int getInnerType(int position) {

        CategoryInfoBean info =  mCategoryInfoBeanList.get(position); //获取每个item数据
        /*根据是否有title返回不同的类型*/
        if (info.isTitle){
            return super.getInnerType(position)+1;
        }else {
            return super.getInnerType(position);
        }

    }

    @Override
    public BaseHolder getHolder(int position) {
        CategoryInfoBean info = mCategoryInfoBeanList.get(position);
        if (info.isTitle) {
            Log.i("CategoryAdapter","info.isTitle:"+info.isTitle+"position:"+position);
            return new CategoryTitleHolder();
        }else {
            Log.i("CategoryAdapter","info.isTitle:"+info.isTitle+"position:"+position);
            return new CategoryInfoHolder();
        }

    }

    @Override
    public boolean hasMore() {
        return false;
    }

    @Override
    public List<CategoryInfoBean> onLoadData() {
        return null;
    }


}
