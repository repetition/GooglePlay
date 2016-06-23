package com.example.googleplay.fragment;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.googleplay.R;
import com.example.googleplay.adapter.HomeAdapter;
import com.example.googleplay.domain.AppInfo;
import com.example.googleplay.holder.HomeHeaderHolder;
import com.example.googleplay.http.protocol.HomeProtocol;
import com.example.googleplay.utils.UIUtils;
import com.example.googleplay.AppDetailActivity;

import java.util.List;

import static com.example.googleplay.view.LoadingPage.ResultState;

/**
 * Created by LIANGSE on 2016/3/7.
 */
public class HomeFragment extends BaseFragment<AppInfo> {


    private ListView mListView;

    private List<AppInfo> mAppInfoList;
    private View mView;
    private HomeProtocol mProtocol;

    @Override
    public View onCreateSuccessView() {

        mView = UIUtils.inflate(R.layout.home_fragment);
        mListView = (ListView) mView.findViewById(R.id.listView);
        HomeAdapter adapter = new HomeAdapter(mAppInfoList);
        mListView.setAdapter(adapter);
        /*添加首页轮播图*/
        HomeHeaderHolder holder = new HomeHeaderHolder();
        holder.setData(mProtocol.getPictureList());
        mListView.addHeaderView(holder.getItemView(), null, false);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), AppDetailActivity.class);

                AppInfo appInfo = mAppInfoList.get((int) id);
                intent.putExtra("packageName", appInfo.packageName);
                intent.putExtra("appName", appInfo.name);
                getActivity().startActivity(intent);
                Log.i("HomeFragment", "position:" + position + "id:" + id);
            }
        });

        return mView;
    }

    @Override
    public ResultState onLoad() {
        mProtocol = new HomeProtocol();
        mAppInfoList = mProtocol.getData(0);
       /* HomeHeaderHolder holder = new HomeHeaderHolder();
        holder.setData(mProtocol.getPictureList());*/
        // SystemClock.sleep(2000);
        try {
            Log.i("HomeFragment", "解析数据:" + mAppInfoList.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return checkData(mAppInfoList);
    }

}
