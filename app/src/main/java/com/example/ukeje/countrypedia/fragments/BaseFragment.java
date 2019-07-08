package com.example.ukeje.countrypedia.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ukeje.countrypedia.utils.AppUtils;

/**
 * @author .: Oriaghan Uyi
 * @email ..: uyioriaghan@gmail.com, uyi.oriaghan@cwg-plc.com
 * @created : 2019-07-08 14:27
 */
public abstract class BaseFragment extends Fragment {

    AppUtils appUtils;

    public static String COUNTRY_LIST_FRAGMENT = "country.list.fragment";
    public static String COUNTRY_DETAILS_FRAGMENT = "country.details.fragment";
    public static String FAVORITE_LIST_FRAGMENT = "favorite.list.fragment";
    public static String HOME_FRAGMENT = "home.fragment";
    public static String REGION_LIST_FRAGMENT = "region.list.fragment";
    public static String SEARCH_COUNTRY_FRAGMENT = "search.country.fragment";
    public static String BOTTOM_NAV_DRAWER_FRAGMENT = "bottom.navigation.drawer.fragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appUtils = new AppUtils(getActivity());

    }

    abstract public String getFragmentTag();

}