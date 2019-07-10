package com.example.ukeje.countrypedia.adapters

import android.util.SparseArray
import android.widget.LinearLayout
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.ukeje.countrypedia.dto.HomeNavItem

/**
 * @author .: Oriaghan Uyi
 * @email ..: uyioriaghan@gmail.com, uyi.oriaghan@cwg-plc.com
 * @created : 2019-07-10 11:33
 */

class BottomNavDrawerManager(var menuList: List<HomeNavItem>) {

    var navItemLayoutList = mutableListOf<LinearLayout>()

    var navHostFragmentList: List<NavHostFragment> = listOf()

    lateinit var fragmentManager: FragmentManager

    var selectedNavHostFragment: NavHostFragment? = null

    // Map of tags
    val graphIdToTagMap = SparseArray<String>()

    // Result. Mutable live data with the selected controlled
    val selectedNavController = MutableLiveData<NavController>()

    var firstFragmentGraphId = 0

    var selectedItemPosition = 0

    var listener: ((HomeNavItem) -> Boolean)? = null

    var selectedItemId = menuList[HomeNavItem.HOME_POSITION].id
        set(value) {
            val navItem = getHomeNavItemById(value)
            if (navItem != null)
                listener?.let {
                    it(navItem)
                    selectedItemPosition = menuList.indexOf(navItem)
                }
            field = value
        }

    companion object {
        val RAW_HOME_NAV_ITEMS_LIST = listOf(
                HomeNavItem("HOME", HomeNavItem.HOME_POSITION),
                HomeNavItem("REGION", HomeNavItem.REGION_POSITION),
                HomeNavItem("FAVORITE", HomeNavItem.FAVORITE_POSITION)
        )
    }

    fun initView() {
        setLayoutSelection(selectedItemPosition)

    }

    fun onItemClick(itemPosition: Int){
        setLayoutSelection(itemPosition)
        this.selectedItemPosition = itemPosition
        selectedItemId = menuList[itemPosition].id
    }

    private fun setLayoutSelection(selectedItemPosition: Int) {
        for (i in navItemLayoutList.indices) {
            navItemLayoutList[i].isSelected = navItemLayoutList[i] === navItemLayoutList[selectedItemPosition]
        }
    }

    fun setOnNavigationItemSelectedListener(listener: (HomeNavItem) -> Boolean) {
        this.listener = listener
    }

    fun getHomeNavItemById(id: Int): HomeNavItem? {
        var result: HomeNavItem? = null
        for (item: HomeNavItem in menuList) {
            if (item.id == id) {
                result = item
                break
            }
        }
        return result
    }
}