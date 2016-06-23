package com.example.googleplay.fragment;

import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LIANGSE on 2016/5/26.
 */
public class FragmentFactory {
    private static Map<Integer, BaseFragment> mFragmentMap = new HashMap<>();

    public static BaseFragment createFragment(int postion) {
        BaseFragment fragment;

        fragment = mFragmentMap.get(postion);

        if (fragment == null) {

            switch (postion) {

                case 0:
                    fragment = new HomeFragment();

                    break;
                case 1:
                    fragment = new AppFragment();

                    break;
                case 2:
                    fragment = new GameFragment();
                    break;
                case 3:
                    fragment = new SubjectFragment();
                    break;
                case 4:
                    fragment = new CategoryFragment();
                    break;
                case 5:
                    fragment = new TopFragment();
                    break;
            }

            Bundle bundle = new Bundle();
            bundle.putInt("pageType", postion);
            fragment.setArguments(bundle);

            mFragmentMap.put(postion, fragment);
        }
        return fragment;
    }

}
