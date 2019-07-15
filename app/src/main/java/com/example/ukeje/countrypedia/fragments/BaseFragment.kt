package com.example.ukeje.countrypedia.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.ukeje.countrypedia.MainActivity

import com.example.ukeje.countrypedia.utils.AppUtils

/**
 * @author .: Oriaghan Uyi
 * @email ..: uyioriaghan@gmail.com, uyi.oriaghan@cwg-plc.com
 * @created : 2019-07-08 14:27
 */
abstract class BaseFragment : Fragment() {

    lateinit var appUtils: AppUtils

    abstract val fragmentTag: String

    lateinit var mainActivity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appUtils = AppUtils(requireActivity())
        mainActivity = requireActivity() as MainActivity

    }

    companion object {

        var COUNTRY_LIST_FRAGMENT = "country.list.fragment"
        var COUNTRY_DETAILS_FRAGMENT = "country.details.fragment"
        var FAVORITE_LIST_FRAGMENT = "favorite.list.fragment"
        var HOME_FRAGMENT = "home.fragment"
        var REGION_LIST_FRAGMENT = "region.list.fragment"
        var SEARCH_COUNTRY_FRAGMENT = "search.country.fragment"
        var BOTTOM_NAV_DRAWER_FRAGMENT = "bottom.navigation.drawer.fragment"
    }

}