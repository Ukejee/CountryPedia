package com.example.ukeje.countrypedia

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.ukeje.countrypedia.databinding.ActivityMainBinding
import com.example.ukeje.countrypedia.dto.HomeNavItem
import com.example.ukeje.countrypedia.extensions.getFragmentTag
import com.example.ukeje.countrypedia.extensions.obtainNavHostFragment
import com.example.ukeje.countrypedia.extensions.setupWithNavDrawerMenuController
import com.example.ukeje.countrypedia.fragments.BaseFragment.Companion.BOTTOM_NAV_DRAWER_FRAGMENT
import com.example.ukeje.countrypedia.fragments.BottomNavDrawerDialogFragment

class MainActivity : AppCompatActivity() {

    private var currentNavController: LiveData<NavController>? = null

    private lateinit var binding: ActivityMainBinding
    private var navDrawerDialogFragment: BottomNavDrawerDialogFragment? = null

    private var navHostList: ArrayList<NavHostFragment> = ArrayList()
    private var homeNavItemList: ArrayList<HomeNavItem> = ArrayList()

    companion object {
        val NAV_ITEM_NAMES = listOf("Home", "Region", "Favourites")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setUpUi()

    }

    private fun setUpUi() {

        //adds menu icon to bottom appbar
        binding.bottomAppBar.replaceMenu(R.menu.bottom_appbar_menu)

        setUpNav()
    }

    /**
     * sets up bottom navigation items and multiple back stack management
     */
    fun setUpNav() {
        //create a list of nav graph ids
        val navGraphIds = listOf(
                R.navigation.home_graph,
                R.navigation.region_graph,
                R.navigation.favorite_graph
        )

        //loops through nav graph ids and creates navHost fragments for each graph
        navGraphIds.forEachIndexed { index, navGraphId ->

            val fragmentHostTag = getFragmentTag(index)

            // Find or create the Navigation host fragment
            val navHostFragment = obtainNavHostFragment(
                    supportFragmentManager,
                    fragmentHostTag,
                    navGraphId,
                    R.id.nav_host_fragment_layout
            )
            navHostList.add(navHostFragment)

            //creates HomeNavItem and add to a list of homeNavItems
            homeNavItemList.add(HomeNavItem(NAV_ITEM_NAMES[index], index, navHostFragment.navController.graph.id))

        }

        //initialize the BottomNavDrawerDialogFragment with the created homeNavItemList
        navDrawerDialogFragment = BottomNavDrawerDialogFragment(homeNavItemList)

        //show BottomNavDrawerDialogFragment when navigation icon is clicked
        binding.bottomAppBar.setNavigationOnClickListener { navDrawerDialogFragment!!.show(supportFragmentManager.beginTransaction(), BOTTOM_NAV_DRAWER_FRAGMENT) }


        val controller = navDrawerDialogFragment?.bottomNavDrawerManager?.setupWithNavDrawerMenuController(
                navHostFragList = navHostList,
                fragManager = supportFragmentManager,
                intent = intent,
                itemClickListener = {

                }
        )

        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }
}