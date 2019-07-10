package com.example.ukeje.countrypedia.adapters

import android.util.SparseArray
import android.widget.LinearLayout
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.ukeje.countrypedia.dto.HomeNavItem
import com.example.ukeje.countrypedia.dto.HomeNavItem.Companion.HOME_POSITION
import com.example.ukeje.countrypedia.fragments.BottomNavDrawerDialogFragment

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

    val graphIdToTagMap = SparseArray<String>()

    val selectedNavController = MutableLiveData<NavController>()

    var initialPosition = HOME_POSITION

    var firstFragmentGraphId = menuList[initialPosition].id

    var selectedItemPosition = initialPosition

    var listener: ((HomeNavItem) -> Boolean)? = null

    var selectedItemId = firstFragmentGraphId
        set(value) {
            //gets the HomeNavItem by the selectedId
            val navItem = getHomeNavItemById(value)
            //if navItem is not null call the listener closure method with navItem
            navItem?.let {
                listener?.let {
                    it(navItem)
                    selectedItemPosition = menuList.indexOf(navItem)
                }
            }
            field = value
        }

    /**
     * initializes ui
     */
    fun initView() {
        //sets initial selected layout
        setLayoutSelection(selectedItemPosition)
    }

    /**
     * receives click from [BottomNavDrawerDialogFragment] class and calls [setLayoutSelection] method
     * and sets the position and also sets the [selectedItemId]
     * @param itemPosition: clicked position
     */
    fun onItemClick(itemPosition: Int) {
        setLayoutSelection(itemPosition)
        this.selectedItemPosition = itemPosition

        selectedItemId = menuList[itemPosition].id
    }

    /**
     *this highlights the layout selected
     * @param selectedItemPosition: position selected
     */
    private fun setLayoutSelection(selectedItemPosition: Int) {
        navItemLayoutList.forEach {
            it.isSelected = it === navItemLayoutList[selectedItemPosition]
        }
    }

    /**
     * sets listener for item clicks
     */
    fun setOnNavigationItemSelectedListener(listener: (HomeNavItem) -> Boolean) {
        this.listener = listener
    }

    /**
     *gets the [HomeNavItem] to which the [homeNavItemId] belongs
     */
    private fun getHomeNavItemById(homeNavItemId: Int): HomeNavItem? {
        var result: HomeNavItem? = null

        for (item in menuList) {
            if (item.id == homeNavItemId) {
                result = item
                break
            }
        }
        return result
    }
}