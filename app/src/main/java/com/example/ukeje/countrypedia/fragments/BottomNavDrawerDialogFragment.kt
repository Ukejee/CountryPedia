package com.example.ukeje.countrypedia.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.ukeje.countrypedia.R
import com.example.ukeje.countrypedia.adapters.BottomNavDrawerManager
import com.example.ukeje.countrypedia.databinding.FragmentBottomNavDrawerDialogBinding
import com.example.ukeje.countrypedia.dto.HomeNavItem
import com.example.ukeje.countrypedia.dto.HomeNavItem.Companion.FAVORITE_POSITION
import com.example.ukeje.countrypedia.dto.HomeNavItem.Companion.HOME_POSITION
import com.example.ukeje.countrypedia.dto.HomeNavItem.Companion.REGION_POSITION
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * @author .: Oriaghan Uyi
 * @email ..: uyioriaghan@gmail.com, uyi.oriaghan@cwg-plc.com
 * @created : 2019-07-09 19:38
 */
class BottomNavDrawerDialogFragment(private val homeNavItemList: ArrayList<HomeNavItem>) : BottomSheetDialogFragment() {

    private lateinit var uiBinding: FragmentBottomNavDrawerDialogBinding

    var bottomNavDrawerAdapter = BottomNavDrawerManager(homeNavItemList)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        uiBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_bottom_nav_drawer_dialog, container, false)
        initUi()

        return uiBinding.root
    }

    private fun initUi() {

        bottomNavDrawerAdapter.navItemLayoutList = mutableListOf(uiBinding.homeItemLayout, uiBinding.regionItemLayout, uiBinding.favoriteItemLayout)
        bottomNavDrawerAdapter.initView()

        uiBinding.homeItemLayout.setOnClickListener {
            bottomNavDrawerAdapter.onItemClick(HOME_POSITION)
            dismiss()
        }

        uiBinding.regionItemLayout.setOnClickListener {
            bottomNavDrawerAdapter.onItemClick(REGION_POSITION)
            dismiss()
        }

        uiBinding.favoriteItemLayout.setOnClickListener {
            bottomNavDrawerAdapter.onItemClick(FAVORITE_POSITION)
            dismiss()
        }
    }
}

