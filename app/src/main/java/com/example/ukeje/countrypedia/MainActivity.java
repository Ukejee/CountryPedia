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
import com.example.ukeje.countrypedia.fragments.FavoriteListFragment;
import com.example.ukeje.countrypedia.fragments.RegionListFragment;
import com.example.ukeje.countrypedia.fragments.CountyDetailsFragment;
import com.example.ukeje.countrypedia.fragments.HomeFragment;
import com.example.ukeje.countrypedia.fragments.SearchCountryFragment;
import com.google.android.material.navigation.NavigationView;

import static android.view.View.GONE;
import static com.example.ukeje.countrypedia.fragments.BaseFragment.BOTTOM_NAV_DRAWER_FRAGMENT;
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

        addFragment(homeFragment, homeFragment.getFragmentTag());

        navMenu = new BottomNavigationDrawerFragment(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.region_menu_item:
                        if (fragmentManager.getFragments().contains(regionListFragment)) {
                            replaceFragment(regionListFragment, regionListFragment.getFragmentTag());
                        } else {
                            addFragment(regionListFragment, regionListFragment.getFragmentTag());
                        }
                        //Suppressed something here not really sure what incase of error
                        binding.exploreBtn.setVisibility(GONE);
                        navMenu.dismiss();
                        return true;

                    case R.id.home_menu_item:
                        if (fragmentManager.getFragments().contains(homeFragment)) {
                            replaceFragment(homeFragment, homeFragment.getFragmentTag());
                        } else {
                            addFragment(homeFragment, homeFragment.getFragmentTag());
                        }
                        binding.exploreBtn.setVisibility(View.VISIBLE);
                        navMenu.dismiss();
                        return true;

                    case R.id.favorite_menu_item:
                        if (fragmentManager.getFragments().contains(favoriteListFragment)) {
                            replaceFragment(favoriteListFragment, favoriteListFragment.getFragmentTag());
                        } else {
                            addFragment(favoriteListFragment, favoriteListFragment.getFragmentTag());
                        }
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
                if (fragmentManager.getFragments().contains(regionListFragment)) {
                    replaceFragment(regionListFragment, regionListFragment.getFragmentTag());
                } else {
                    addFragment(regionListFragment, regionListFragment.getFragmentTag());
                }
                //Suppressed something here not really sure what in case of error
                binding.exploreBtn.setVisibility(GONE);
            }
        });

        toggleExploreButton(fragmentManager.findFragmentByTag(HOME_FRAGMENT));


    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (fragmentManager.findFragmentById(R.id.your_placeholder) == homeFragment) {
            if (getSupportFragmentManager().getFragments().contains(searchCountryFragment)) {
                fragmentManager.beginTransaction().remove(searchCountryFragment).commit();
            }
            binding.bottomAppBar.setVisibility(View.VISIBLE);
            binding.exploreBtn.setVisibility(View.VISIBLE);
        } else {
            binding.bottomAppBar.setVisibility(View.VISIBLE);
        }
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

        if (tag.equalsIgnoreCase(countyDetailsFragment.getFragmentTag())) {

            //getting current fragment
            if (getCurrentFragment() == searchCountryFragment) {
                replaceFragment(countyDetailsFragment, countyDetailsFragment.getFragmentTag());
            } else {
                addFragment(countyDetailsFragment, countyDetailsFragment.getFragmentTag());
            }
            binding.bottomAppBar.setVisibility(GONE);
            binding.exploreBtn.setVisibility(GONE);
        }
        if (tag.equalsIgnoreCase(COUNTRY_LIST_FRAGMENT)) {
            binding.bottomAppBar.setVisibility(View.VISIBLE);
            addFragment(countryListFragment, countryListFragment.getFragmentTag());
        }
        if (tag.equalsIgnoreCase(HOME_FRAGMENT)) {
            replaceFragment(homeFragment, homeFragment.getFragmentTag());
        }
        if (tag.equalsIgnoreCase(FAVORITE_LIST_FRAGMENT)) {
            addFragment(favoriteListFragment, favoriteListFragment.getFragmentTag());
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

            if (fragmentManager.getFragments().size() == 1) {
                replaceFragment(homeFragment, homeFragment.getFragmentTag());
                binding.bottomAppBar.setVisibility(View.VISIBLE);
                binding.exploreBtn.setVisibility(View.VISIBLE);

            } else {
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.your_placeholder, fragmentManager.getFragments().get(fragmentManager.getFragments().size() - 2));
                fragmentTransaction.commit();
                if (fragmentManager.getFragments().get(fragmentManager.getFragments().size() - 2) == homeFragment) {
                    if (getSupportFragmentManager().getFragments().contains(searchCountryFragment)) {
                        fragmentManager.beginTransaction().remove(searchCountryFragment);
                    }
                    binding.bottomAppBar.setVisibility(View.VISIBLE);
                    binding.exploreBtn.setVisibility(View.VISIBLE);
                } else {
                    binding.bottomAppBar.setVisibility(View.VISIBLE);
                }

            }

        }
    }

    public Fragment getCurrentFragment(){
       return fragmentManager.findFragmentById(R.id.your_placeholder);
    }

    /**
     * method to add and replace fragments
     */
    public void loadFragment(Fragment fragment, String tag,boolean addToBackStack){


    }

    public void replaceFragment(Fragment fragment, String tag) {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.your_placeholder, fragment, tag);
        fragmentTransaction.commit();
    }

    public void addFragment(Fragment fragment, String tag) {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.your_placeholder, fragment, tag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
