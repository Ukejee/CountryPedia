package com.example.ukeje.countrypedia.activity

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.room.Room
import com.example.ukeje.countrypedia.R
import com.example.ukeje.countrypedia.database.CountryPediaDatabase
import com.example.ukeje.countrypedia.databinding.ActivityMainBinding
import com.example.ukeje.countrypedia.dto.HomeNavItem
import com.example.ukeje.countrypedia.extensions.getFragmentTag
import com.example.ukeje.countrypedia.extensions.obtainNavHostFragment
import com.example.ukeje.countrypedia.extensions.setupWithNavDrawerMenuController
import com.example.ukeje.countrypedia.fragments.BaseFragment.Companion.BOTTOM_NAV_DRAWER_FRAGMENT
import com.example.ukeje.countrypedia.fragments.BottomNavDrawerDialogFragment
import com.example.ukeje.countrypedia.repository.CountryPediaRepository
import com.example.ukeje.countrypedia.utils.AppUtils
import com.example.ukeje.countrypedia.viewmodel.MainActivityViewModel

class MainActivity : AppCompatActivity() {

    lateinit var countryPediaDatabase: CountryPediaDatabase
    private var currentNavControllerLiveData: LiveData<NavController>? = null

    private lateinit var binding: ActivityMainBinding
    private var navDrawerDialogFragment: BottomNavDrawerDialogFragment? = null

    private var navHostList: ArrayList<NavHostFragment> = ArrayList()
    private var homeNavItemList: ArrayList<HomeNavItem> = ArrayList()

    private lateinit var viewModel: MainActivityViewModel
    lateinit var countryPediaRepository: CountryPediaRepository

    companion object {
        val NAV_ITEM_NAMES = listOf("Home", "Region", "Favourites")
        val COUNTRY_PEDIA_DB_NAME = "country_pedia_db"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        //set up db
        countryPediaDatabase = Room.databaseBuilder(this, CountryPediaDatabase::class.java, COUNTRY_PEDIA_DB_NAME).build()
        //initialize CountryRepository with countryPediaDatabase from the mainActivity
        countryPediaRepository = CountryPediaRepository(countryPediaDatabase)

        setUpUi()

    }

    private fun setUpUi() {

        setSupportActionBar(binding.bottomAppBar)

        setUpNav()
    }

    /**
     * sets up bottom navigation items and multiple back stack management
     */
    private fun setUpNav() {
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


        val controllerLiveData = navDrawerDialogFragment?.bottomNavDrawerManager?.setupWithNavDrawerMenuController(
                intent = intent,
                fragManager = supportFragmentManager,
                navHostFragList = navHostList,
                itemClickListener = {}
        )

        //list of root fragments ids
        val rootFragmentsIds = listOf(R.id.homeFragment, R.id.favoriteListFragment, R.id.regionListFragment)

        //listening on the controllerLiveData
        controllerLiveData?.observe(this, Observer { navController ->

            val appBarConfiguration = AppBarConfiguration(navController.graph)

            //initialize toolbar
            navController?.let {
                binding.bottomAppBar.setupWithNavController(navController, appBarConfiguration)
            }

            //listening on the controller for destination change
            navController.addOnDestinationChangedListener { _, destination, _ ->
                if (destination.id == R.id.countryDetailsFragment) {
                    binding.bottomAppBar.replaceMenu(R.menu.appbar_fav_unselected_menu)
                } else {
                    binding.bottomAppBar.replaceMenu(R.menu.appbar_about_menu)
                }

                if (destination.id == R.id.homeFragment) {
                    binding.exploreBtn.show()
                } else {
                    binding.exploreBtn.hide()
                }

                if (destination.id == R.id.splashScreenFragment) {
                    binding.bottomAppBar.visibility = View.GONE
                } else {
                    binding.bottomAppBar.visibility = View.VISIBLE

                }

                AppUtils.debug("current fragment: {}", navController.currentDestination?.label)

                if (rootFragmentsIds.contains(navController.currentDestination?.id)) {
                    AppUtils.debug("we are in a in root fragment")
                    binding.bottomAppBar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.menu_icon)
                    binding.bottomAppBar.setNavigationOnClickListener { navDrawerDialogFragment!!.show(supportFragmentManager.beginTransaction(), BOTTOM_NAV_DRAWER_FRAGMENT) }

                } else {
                    AppUtils.debug("we are not in a root fragment")
                    binding.bottomAppBar.setNavigationOnClickListener {
                        NavigationUI.navigateUp(navController, appBarConfiguration)
                    }
                }

            }

        })

        currentNavControllerLiveData = controllerLiveData

        binding.exploreBtn.setOnClickListener {
            navDrawerDialogFragment?.bottomNavDrawerManager!!.goToRegionMenu()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.appbar_about_menu, menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.about_menu_item -> {
            showAboutDialog()
            true
        }

        R.id.favorite_unselected_menu_item -> {
            // perform action to make country a favourite

            true
        }

        R.id.favorite_selected_menu_item -> {
            // perform action to remove country from favourite list
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavControllerLiveData?.value?.navigateUp() ?: false
    }


    private fun showAboutDialog() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        val viewGroup = findViewById<ViewGroup>(android.R.id.content)
        //then we will inflate the custom alert dialog xml that we created
        val dialogView = LayoutInflater.from(this).inflate(R.layout.about_dialog, viewGroup, false)
        //Now we need an AlertDialog.Builder object
        val builder = AlertDialog.Builder(this)
        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView)
        //finally creating the alert dialog and displaying it
        val alertDialog = builder.create()
        alertDialog.show()

    }
}