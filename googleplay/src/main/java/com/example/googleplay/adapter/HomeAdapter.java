package com.example.googleplay.adapter;

import com.example.googleplay.domain.AppInfo;
import com.example.googleplay.holder.BaseHolder;
import com.example.googleplay.holder.HomeHolder;
import com.example.googleplay.http.protocol.HomeProtocol;

import java.util.List;

/**
 * Created by v_bzhzhang on 2016/3/23.
 */
public class HomeAdapter extends AppAdapter<AppInfo> {
    public HomeAdapter(List list) {
        super(list);
    }

    @Override
    public BaseHolder getHolder(int position) {
        return new HomeHolder();
    }

    @Override
    public List<AppInfo> onLoadData() {
        /*分页加载数据*/
        HomeProtocol protocol = new HomeProtocol();
        List<AppInfo> mloadData = protocol.getData(getDataSize());

        return mloadData;
    }

}
