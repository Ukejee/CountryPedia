package com.example.ukeje.countrypedia;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ukeje.countrypedia.databinding.ActivityMainBinding;
import com.example.ukeje.countrypedia.fragments.BottomNavigationDrawerFragment;
import com.example.ukeje.countrypedia.fragments.CountryListFragment;
import com.example.ukeje.countrypedia.fragments.CountyDetailsFragment;
import com.example.ukeje.countrypedia.fragments.FavoriteListFragment;
import com.example.ukeje.countrypedia.fragments.HomeFragment;
import com.example.ukeje.countrypedia.fragments.RegionListFragment;
import com.example.ukeje.countrypedia.fragments.SearchCountryFragment;
import com.example.ukeje.countrypedia.utils.AppUtils;
import com.google.android.material.navigation.NavigationView;

import java.util.List;
import java.util.Objects;

import static android.view.View.GONE;
import static com.example.ukeje.countrypedia.fragments.BaseFragment.BOTTOM_NAV_DRAWER_FRAGMENT;
import static com.example.ukeje.countrypedia.fragments.BaseFragment.COUNTRY_DETAILS_FRAGMENT;
import static com.example.ukeje.countrypedia.fragments.BaseFragment.COUNTRY_LIST_FRAGMENT;
import static com.example.ukeje.countrypedia.fragments.BaseFragment.FAVORITE_LIST_FRAGMENT;
import static com.example.ukeje.countrypedia.fragments.BaseFragment.HOME_FRAGMENT;
import static com.example.ukeje.countrypedia.fragments.BaseFragment.SEARCH_COUNTRY_FRAGMENT;

public class MainActivity extends AppCompatActivity implements RegionListFragment.OnFragmentInteractionListener,
        HomeFragment.OnFragmentInteractionListener,
        CountyDetailsFragment.OnFragmentInteractionListener,
        CountryListFragment.OnFragmentInteractionListener,
        FavoriteListFragment.OnFragmentInteractionListener,
        SearchCountryFragment.OnFragmentInteractionListener {


    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private RegionListFragment regionListFragment;
    private HomeFragment homeFragment;
    private CountyDetailsFragment countyDetailsFragment;
    private CountryListFragment countryListFragment;
    private FavoriteListFragment favoriteListFragment;
    private BottomNavigationDrawerFragment navMenu;
    private SearchCountryFragment searchCountryFragment;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        regionListFragment = new RegionListFragment();
        homeFragment = new HomeFragment();
        countyDetailsFragment = new CountyDetailsFragment();
        countryListFragment = new CountryListFragment();
        favoriteListFragment = new FavoriteListFragment();
        searchCountryFragment = new SearchCountryFragment();

        fragmentManager = getSupportFragmentManager();

        loadFragment(homeFragment, homeFragment.getFragmentTag(), true);

        navMenu = new BottomNavigationDrawerFragment(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.region_menu_item:

                        loadFragment(regionListFragment, regionListFragment.getFragmentTag(), true);

                        //Suppressed something here not really sure what incase of error
                        binding.exploreBtn.setVisibility(GONE);
                        navMenu.dismiss();
                        return true;

                    case R.id.home_menu_item:

                        loadFragment(homeFragment, homeFragment.getFragmentTag(), true);

                        binding.exploreBtn.setVisibility(View.VISIBLE);
                        navMenu.dismiss();
                        return true;

                    case R.id.favorite_menu_item:

                        loadFragment(favoriteListFragment, favoriteListFragment.getFragmentTag(), true);

                        binding.exploreBtn.setVisibility(GONE);
                        navMenu.dismiss();
                        return true;
                }
                return true;
            }
        });

        binding.bottomAppBar.replaceMenu(R.menu.bottomappbar_menu);

        binding.bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                AboutDialog aboutDialog = new AboutDialog(MainActivity.this);
                aboutDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                aboutDialog.show();
                return false;
            }
        });

        binding.bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navMenu.show(fragmentManager.beginTransaction(), BOTTOM_NAV_DRAWER_FRAGMENT);
            }
        });

        binding.exploreBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
//                if (fragmentManager.getFragments().contains(regionListFragment)) {
                loadFragment(regionListFragment, regionListFragment.getFragmentTag(), true);
//                } else {
//                    addFragment(regionListFragment, regionListFragment.getFragmentTag());
//                }
                //Suppressed something here not really sure what in case of error
                binding.exploreBtn.setVisibility(GONE);
            }
        });

        toggleExploreButton(fragmentManager.findFragmentByTag(HOME_FRAGMENT));


    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onBackPressed() {
        List<Fragment> backStackFragments = getSupportFragmentManager().getFragments();
        AppUtils.debug("fragment list size**: {}", backStackFragments.size());

        if (backStackFragments.size() == 1) {
            finish();
            return;
        }

        if (getCurrentFragment().getTag().equals(HOME_FRAGMENT)) {
            if (backStackFragments.contains(searchCountryFragment)) {
                fragmentManager.beginTransaction().remove(searchCountryFragment).commit();
            }
            binding.bottomAppBar.setVisibility(View.VISIBLE);
            binding.exploreBtn.setVisibility(View.VISIBLE);
        } else {
            binding.bottomAppBar.setVisibility(View.VISIBLE);
        }

        super.onBackPressed();

    }

    @SuppressLint("RestrictedApi")
    public void toggleExploreButton(Fragment fragment) {

        if (fragment != null) {
            if (fragmentManager.getPrimaryNavigationFragment().equals(fragment)) {
                binding.exploreBtn.setVisibility(GONE);
            }

        }

    }

    //METHOD THAT CONTROLS FRAGMENT NAVIGATION IN THE APP
    @SuppressLint("RestrictedApi")
    @Override
    public void onFragmentInteraction(String tag) {

        if (tag.equalsIgnoreCase(COUNTRY_DETAILS_FRAGMENT)) {

            //getting current fragment
//            if (getCurrentFragment() == searchCountryFragment) {
            loadFragment(countyDetailsFragment, countyDetailsFragment.getFragmentTag(),
                    !Objects.equals(getCurrentFragment().getTag(), SEARCH_COUNTRY_FRAGMENT));
//            } else {
//                loadFragment(countyDetailsFragment, countyDetailsFragment.getFragmentTag(), true);
//            }
            binding.bottomAppBar.setVisibility(GONE);
            binding.exploreBtn.setVisibility(GONE);
        }
        if (tag.equalsIgnoreCase(COUNTRY_LIST_FRAGMENT)) {
            binding.bottomAppBar.setVisibility(View.VISIBLE);
            loadFragment(countryListFragment, countryListFragment.getFragmentTag(), true);
        }
        if (tag.equalsIgnoreCase(HOME_FRAGMENT)) {
            loadFragment(homeFragment, homeFragment.getFragmentTag(), true);
        }
        if (tag.equalsIgnoreCase(FAVORITE_LIST_FRAGMENT)) {
            loadFragment(favoriteListFragment, favoriteListFragment.getFragmentTag(), true);
            binding.bottomAppBar.setVisibility(View.VISIBLE);
        }
        if (tag.equalsIgnoreCase(SEARCH_COUNTRY_FRAGMENT)) {
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.your_placeholder, searchCountryFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            binding.exploreBtn.setVisibility(GONE);
        }
        if (tag.equalsIgnoreCase("back")) {
            onBackPressed();
        }
    }

    public Fragment getCurrentFragment() {
        return fragmentManager.findFragmentById(R.id.your_placeholder);
    }

    /**
     * method to add and replace fragments
     */
    public void loadFragment(Fragment fragment, String tag, boolean addToBackStack) {
//        if (fragmentManager.getFragments().contains(fragment)) {
        replaceFragment(fragment, tag, addToBackStack);
        /* }else {
            replaceFragment(fragment, tag, true);
        }*/

        AppUtils.debug("loadFragment: {}; addToBackStack: {}", tag, addToBackStack);
        AppUtils.debug("fragment list size: {}", getSupportFragmentManager().getFragments().size());
    }

    public void replaceFragment(Fragment fragment, String tag, boolean addToBackStack) {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.your_placeholder, fragment, tag);
        if (addToBackStack) fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
