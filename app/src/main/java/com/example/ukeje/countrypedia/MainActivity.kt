package com.example.ukeje.countrypedia

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import com.example.ukeje.countrypedia.adapters.BottomNavDrawerManager.Companion.RAW_HOME_NAV_ITEMS_LIST
import com.example.ukeje.countrypedia.databinding.ActivityMainBinding
import com.example.ukeje.countrypedia.dto.HomeNavItem
import com.example.ukeje.countrypedia.extensions.getFragmentTag
import com.example.ukeje.countrypedia.extensions.obtainNavHostFragment
import com.example.ukeje.countrypedia.extensions.setupWithNavDrawerMenuController
import com.example.ukeje.countrypedia.fragments.BaseFragment.BOTTOM_NAV_DRAWER_FRAGMENT
import com.example.ukeje.countrypedia.fragments.BottomNavDrawerDialogFragment

class MainActivity : AppCompatActivity() {


    private var fragmentManager: FragmentManager? = null

    private var binding: ActivityMainBinding? = null
    private var navDrawerDialogFragment: BottomNavDrawerDialogFragment? = null

    private var navHostList: ArrayList<NavHostFragment> = ArrayList()
    private var homeNavItemList: ArrayList<HomeNavItem> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        fragmentManager = supportFragmentManager

        setUpUi()

    }

    private fun setUpUi() {

        binding!!.bottomAppBar.replaceMenu(R.menu.bottomappbar_menu)

        setUpNav()
    }

    fun setUpNav() {
        val navGraphIds = listOf(
                R.navigation.home_graph,
                R.navigation.region_graph,
                R.navigation.favorite_graph
        )

        navGraphIds.forEachIndexed { index, navGraphId ->
            val fragmentTag = getFragmentTag(index)

            // Find or create the Navigation host fragment
            val navHostFragment = obtainNavHostFragment(
                    supportFragmentManager,
                    fragmentTag,
                    navGraphId,
                    R.id.nav_host_fragment_layout
            )
            navHostList.add(navHostFragment)

            val homeNavItem: HomeNavItem = RAW_HOME_NAV_ITEMS_LIST[index]
            homeNavItem.id = navHostFragment.navController.graph.id
            homeNavItemList.add(homeNavItem)

        }

        navDrawerDialogFragment = BottomNavDrawerDialogFragment(homeNavItemList)

        binding!!.bottomAppBar.setNavigationOnClickListener { navDrawerDialogFragment!!.show(fragmentManager!!.beginTransaction(), BOTTOM_NAV_DRAWER_FRAGMENT) }

        val controller = navDrawerDialogFragment?.bottomNavDrawerAdapter?.setupWithNavDrawerMenuController(
                navHostFragList = navHostList,
                fragManager = supportFragmentManager,
                intent = intent,
                itemClickListener = {

                }
        )
    }
}