package com.example.googleplay.fragment;

import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.example.googleplay.R;
import com.example.googleplay.adapter.CategoryAdapter;
import com.example.googleplay.domain.CategoryInfoBean;
import com.example.googleplay.http.protocol.CategoryProtocol;
import com.example.googleplay.utils.UIUtils;

import java.util.List;

import static com.example.googleplay.view.LoadingPage.ResultState;

/**
 * Created by LIANGSE on 2016/3/7.
 */
public class CategoryFragment extends BaseFragment {

    private List<CategoryInfoBean> mCategoryInfoBeanList;
    private ListView mListView;

    @Override
    public View onCreateSuccessView() {

        View view = UIUtils.inflate(R.layout.category_fragment);
        mListView = (ListView) view.findViewById(R.id.lv_Category);
        mListView.setAdapter(new CategoryAdapter(mCategoryInfoBeanList));
        return view;
    }

    @Override
    public ResultState onLoad() {

        CategoryProtocol protocol = new CategoryProtocol();
        mCategoryInfoBeanList = protocol.getData(0);
        try {

            Log.i("CategoryFragment", mCategoryInfoBeanList.size() + "mCategoryInfoBeanList:" + mCategoryInfoBeanList.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return checkData(mCategoryInfoBeanList);
    }

}
