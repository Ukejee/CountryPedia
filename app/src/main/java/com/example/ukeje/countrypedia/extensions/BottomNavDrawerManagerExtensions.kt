/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.ukeje.countrypedia.extensions

import android.content.Intent
import androidx.core.util.forEach
import androidx.core.util.set
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.ukeje.countrypedia.R
import com.example.ukeje.countrypedia.adapters.BottomNavDrawerManager
import com.example.ukeje.countrypedia.dto.HomeNavItem

/**
 * Manages the various graphs needed for a [BottomNavDrawerManager].
 *
 * This sample is a workaround until the Navigation Component supports multiple back stacks.
 */
fun BottomNavDrawerManager.setupWithNavDrawerMenuController(
        navHostFragList: List<NavHostFragment>,
        fragManager: FragmentManager,
        intent: Intent,
        itemClickListener: ((HomeNavItem) -> Unit)
): LiveData<NavController> {

    navHostFragmentList = navHostFragList
    fragmentManager = fragManager

    // First create a NavHostFragment for each NavGraph ID
    navHostFragmentList.forEachIndexed { index, navHostFragment ->
        val fragmentTag = getFragmentTag(index)

        // Obtain its id
        val graphId = navHostFragment.navController.graph.id

        // Save to the map
        graphIdToTagMap[graphId] = fragmentTag

        // Attach or detach nav host fragment depending on whether it's the selected item.
        if (this.selectedItemId == graphId) {
            // Update livedata with the selected graph
            selectedNavController.value = navHostFragment.navController
            attachNavHostFragment(fragmentManager, navHostFragment, index == 0)
        } else {
            detachNavHostFragment(fragmentManager, navHostFragment)

        }
    }

    // Now connect selecting an item with swapping Fragments
    var selectedItemTag = graphIdToTagMap[this.selectedItemId]
    val firstFragmentTag = graphIdToTagMap[firstFragmentGraphId]
    var isOnFirstFragment = selectedItemTag == firstFragmentTag

    // When a navigation item is selected
    setOnNavigationItemSelectedListener { item ->
        itemClickListener(item)
        // Don't do anything if the state is state has already been saved.
        if (fragmentManager.isStateSaved) {

            false
        } else {
            val newlySelectedItemTag = graphIdToTagMap[item.id]
            if (selectedItemTag != newlySelectedItemTag) {
                // Pop everything above the first fragment (the "fixed start destination")

                fragmentManager.popBackStack(
                        firstFragmentTag,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE
                )
                selectedNavHostFragment = fragmentManager.findFragmentByTag(newlySelectedItemTag) as NavHostFragment

                // Exclude the first fragment tag because it's always in the back stack.
                if (firstFragmentTag != newlySelectedItemTag) {
                    // Commit a transaction that cleans the back stack and adds the first fragment
                    // to it, creating the fixed started destination.
                    fragmentManager.beginTransaction()
                            .attach(selectedNavHostFragment!!)
                            .setPrimaryNavigationFragment(selectedNavHostFragment)
                            .apply {
                                // Detach all other Fragments
                                graphIdToTagMap.forEach { _, fragmentTagIter ->
                                    if (fragmentTagIter != newlySelectedItemTag) {
                                        detach(fragmentManager.findFragmentByTag(firstFragmentTag)!!)
                                    }
                                }
                            }
                            .addToBackStack(firstFragmentTag)
                            .setCustomAnimations(
                                    R.anim.nav_default_enter_anim,
                                    R.anim.nav_default_exit_anim,
                                    R.anim.nav_default_pop_enter_anim,
                                    R.anim.nav_default_pop_exit_anim
                            )
                            .setReorderingAllowed(true)
                            .commit()
                }
                selectedItemTag = newlySelectedItemTag
                isOnFirstFragment = selectedItemTag == firstFragmentTag
                selectedNavController.value = selectedNavHostFragment!!.navController
                true
            } else {

                false
            }

        }
    }

    // Handle deep link
    setupDeepLinks(navHostFragmentList, intent)

    // Finally, ensure that we update our BottomNavigationView when the back stack changes
    fragmentManager.addOnBackStackChangedListener {
        if (!isOnFirstFragment && !fragmentManager.isOnBackStack(firstFragmentTag)) {
            this.selectedItemId = firstFragmentGraphId
        }

        // Reset the graph if the currentDestination is not valid (happens when the back
        // stack is popped after using the back button).
        selectedNavController.value?.let { controller ->
            if (controller.currentDestination == null) {

                controller.navigate(controller.graph.id)
            }
        }
    }
    return selectedNavController
}

private fun BottomNavDrawerManager.setupDeepLinks(
        navHostFragmentList: List<NavHostFragment>,
        intent: Intent
) {
    navHostFragmentList.forEachIndexed { _, navHostFragment ->

        // Handle Intent
        if (navHostFragment.navController.handleDeepLink(intent)) {
            this.selectedItemId = navHostFragment.navController.graph.id
        }
    }
}

private fun detachNavHostFragment(
        fragmentManager: FragmentManager,
        navHostFragment: NavHostFragment
) {
    fragmentManager.beginTransaction()
            .detach(navHostFragment)
            .commitNow()
}

private fun attachNavHostFragment(
        fragmentManager: FragmentManager,
        navHostFragment: NavHostFragment,
        isPrimaryNavFragment: Boolean
) {
    fragmentManager.beginTransaction()
            .attach(navHostFragment)
            .apply {
                if (isPrimaryNavFragment) {
                    setPrimaryNavigationFragment(navHostFragment)
                }
            }
            .commitNow()

}

fun obtainNavHostFragment(
        fragmentManager: FragmentManager,
        fragmentHostTag: String,
        navGraphId: Int,
        containerId: Int
): NavHostFragment {
    // If the Nav Host fragment exists, return it
    val existingFragment = fragmentManager.findFragmentByTag(fragmentHostTag) as NavHostFragment?
    existingFragment?.let { return it }

    // Otherwise, create it and return it.
    val navHostFragment = NavHostFragment.create(navGraphId)

    fragmentManager.beginTransaction()
            .add(containerId, navHostFragment, fragmentHostTag)
            .commitNow()

    return navHostFragment
}

private fun FragmentManager.isOnBackStack(backStackName: String): Boolean {
    val backStackCount = backStackEntryCount
    for (index in 0 until backStackCount) {
        if (getBackStackEntryAt(index).name == backStackName) {
            return true
        }
    }
    return false
}

fun getFragmentTag(index: Int) = "bottomNavigation#$index"
