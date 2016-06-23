package com.example.googleplay.adapter;

import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.googleplay.holder.BaseHolder;
import com.example.googleplay.holder.MoreHolder;
import com.example.googleplay.manager.ThreadManager;
import com.example.googleplay.utils.UIUtils;

import java.util.List;

/**
 * Created by v_bzhzhang on 2016/3/23.
 */
public abstract class AppAdapter<T> extends BaseAdapter {

    public static final int TYPE_MORE = 0;
    public static final int TYPE_NORMAL = 1;
    private boolean isLoadMore = false;

    private boolean ismoreNone = false;

    public List<T> mList;

    public AppAdapter(List<T> list) {
        mList = list;

    }

    @Override
    public int getItemViewType(int position) {
        if (position == (getCount() - 1)) {
            return TYPE_MORE;
        } else {
            return getInnerType(position);
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseHolder holder = null;

        /*判断没有更多数据时,就不在去加载数据,直接return 刷新界面, 没有更多数据*/
        if (ismoreNone && getItemViewType(position) == TYPE_MORE) {
            holder = new MoreHolder(true);
            holder.setData(MoreHolder.STATE_MORE_NO);
            return holder.getItemView();
        }

        if (hasMore() && mList.size() < 20) {
            holder = new MoreHolder(true);
            holder.setData(MoreHolder.STATE_MORE_NO);
            return holder.getItemView();
        }


        if (convertView == null) {

            if (getItemViewType(position) == TYPE_MORE) {
                holder = new MoreHolder(hasMore());
            } else {
                //1.设置holder
                //2.设置tag
                holder = getHolder(position);
            }
        } else {
            //3.取出holder
            holder = (BaseHolder) convertView.getTag();
        }

/*        if (getItemViewType(position) != TYPE_MORE) {
            //4.设置数据
            holder.setData(mList.get(position));
        } else {
            //如果是加载更多的类型,就去请求加载数据
            MoreHolder moreHolder = (MoreHolder) holder;
            if (hasMore()){
                loadMore(moreHolder);   //加载数据
            }
        }*/

        if (getItemViewType(position) == TYPE_MORE) {

            //如果是加载更多的类型,就去请求加载数据
            MoreHolder moreHolder = (MoreHolder) holder;
            if (hasMore()) {
                loadMore(moreHolder);   //加载数据
            }

        } else {//如果是普通布局就去设置数据

            //4.设置数据
            holder.setData(mList.get(position));
            Log.i("Appadapter","数量:"+mList.size()+":holder.setData:"+mList.get(position).toString()+"position:"+position);

        }


        return holder.getItemView();

    }

    /**
     * 加载数据
     *
     * @return
     */
    public void loadMore(final MoreHolder moreHolder) {
        /*判断只在没有加载更多的时候去加载数据,防止重复加载*/
        if (!isLoadMore) {
            isLoadMore = true;//标记是否正在加载中
            ThreadManager.instance().createLongPool().execute(new Runnable() {
                @Override
                public void run() {
                    SystemClock.sleep(2000);
                    final List<T> mLoadData = onLoadData();  //请求数据, 子类实现

                    UIUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (mLoadData != null) {    //如果数据请求成功 就去判断数据数量

                                if (mLoadData.size() < 20) {//数量小于20条 就没有更多数据了

                                    mList.addAll(mLoadData);
                                    AppAdapter.this.notifyDataSetChanged();
                                    if (hasMore()) {
                                        moreHolder.setData(MoreHolder.STATE_MORE_NO);
                                        //Toast.makeText(UIUtils.getContext(),"没有更多数据了",Toast.LENGTH_SHORT).show();
                                        ismoreNone = true; //标记已经没有更多数据了
                                    } else {
                                        moreHolder.setData(MoreHolder.STATE_MORE_NONE);
                                    }

                                } else {//数量大于20条 就没有更多数据了
                                    mList.addAll(mLoadData);
                                    AppAdapter.this.notifyDataSetChanged();
                                    moreHolder.setData(MoreHolder.STATE_MORE_MORE);
                                }

                            } else {
                                moreHolder.setData(MoreHolder.STATE_MORE_ERROR);
                            }
                            isLoadMore = false;
                        }
                    });
                }
            });
        }

    }

    /**
     * 返回布局类型
     *
     * @param position listView Item位置
     * @return
     */
    public int getInnerType(int position) {

        return TYPE_NORMAL;
    }

    public int getDataSize() {
        return mList.size();
    }

    /**
     * 是否有加载更多
     *
     * @return
     */
    public boolean hasMore() {
        return true;
    }

    public abstract List<T> onLoadData();

    public abstract BaseHolder getHolder(int position);
}
