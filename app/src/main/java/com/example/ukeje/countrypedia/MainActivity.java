package com.example.ukeje.countrypedia;

import com.example.ukeje.countrypedia.databinding.ActivityMainBinding;
import com.example.ukeje.countrypedia.fragments.BottomNavigationDrawerFragment;
import com.example.ukeje.countrypedia.fragments.CountryListFragment;
import com.example.ukeje.countrypedia.fragments.FavoriteFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.VideoView;

import com.example.ukeje.countrypedia.fragments.RegionFragment;
import com.example.ukeje.countrypedia.fragments.ResultFragment;
import com.example.ukeje.countrypedia.fragments.SearchFragment;
import com.example.ukeje.countrypedia.fragments.SearchResultFragment;
import com.example.ukeje.countrypedia.utils.AppUtils;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity implements RegionFragment.OnFragmentInteractionListener,
                                                                SearchFragment.OnFragmentInteractionListener,
                                                                ResultFragment.OnFragmentInteractionListener,
                                                                CountryListFragment.OnFragmentInteractionListener,
                                                                FavoriteFragment.OnFragmentInteractionListener,
                                                                SearchResultFragment.OnFragmentInteractionListener{


    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    RegionFragment regionFragment;
    SearchFragment searchFragment;
    ResultFragment resultFragment;
    CountryListFragment countryListFragment;
    FavoriteFragment favoriteFragment;
    BottomNavigationDrawerFragment navMenu;
    SearchResultFragment searchResultFragment;

    ActivityMainBinding binding;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        regionFragment = new RegionFragment();
        searchFragment = new SearchFragment();
        resultFragment = new ResultFragment();
        countryListFragment = new CountryListFragment();
        favoriteFragment = new FavoriteFragment();
        searchResultFragment = new SearchResultFragment();

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.your_placeholder, searchFragment, "Home");
        fragmentTransaction.commit();

        navMenu = new BottomNavigationDrawerFragment(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.region_menu_item:
                        if(fragmentManager.getFragments().contains(regionFragment)){
                            replaceFragment(regionFragment,"Region");
                        }else{
                            onFragmentInteraction("FAB");
                        }
                        //Suppressed something here not really sure what incase of error
                        binding.exploreBtn.setVisibility(GONE);
                        navMenu.dismiss();
                        return true;

                    case R.id.home_menu_item:
                        if(fragmentManager.getFragments().contains(searchFragment)){
                            replaceFragment(searchFragment, "Home");
                        }
                        else{
                            addFragment(searchFragment, "Home");
                        }
                        binding.exploreBtn.setVisibility(View.VISIBLE);
                        navMenu.dismiss();
                        return true;

                    case R.id.favorite_menu_item:
                        if(fragmentManager.getFragments().contains(favoriteFragment)){
                            replaceFragment(favoriteFragment, "Favorite");
                        }
                        else{
                            addFragment(favoriteFragment, "Favorite");
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
                navMenu.show(fragmentManager.beginTransaction(), "Navigation Drawer");
            }
        });

        binding.exploreBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                if(fragmentManager.getFragments().contains(regionFragment)){
                    replaceFragment(regionFragment,"Region");
                }else{
                    onFragmentInteraction("FAB");
                }
                //Suppressed something here not really sure what incase of error
                binding.exploreBtn.setVisibility(GONE);
            }
        });

        toggleExploreButton(fragmentManager.findFragmentByTag("Home"));


    }


    @SuppressLint("RestrictedApi")
    @Override
    public void onBackPressed() {
        super.onBackPressed();
            if (fragmentManager.findFragmentById(R.id.your_placeholder) == searchFragment) {
                if(getSupportFragmentManager().getFragments().contains(searchResultFragment)){
                    fragmentManager.beginTransaction().remove(searchResultFragment).commit();
                }
                binding.bottomAppBar.setVisibility(View.VISIBLE);
                binding.exploreBtn.setVisibility(View.VISIBLE);
            }
            else {
                binding.bottomAppBar.setVisibility(View.VISIBLE);
            }
    }

    @SuppressLint("RestrictedApi")
    public void toggleExploreButton(Fragment fragment){

        if(fragment != null){
            if(fragmentManager.getPrimaryNavigationFragment().equals(fragment)){
                binding.exploreBtn.setVisibility(GONE);
            }

        }

    }

    //METHOD THAT CONTROLS FRAGMENT NAVIGATION IN THE APP
    @SuppressLint("RestrictedApi")
    public void onFragmentInteraction(String tag){

        if(tag.equalsIgnoreCase("FAB")){
           addFragment(regionFragment, "Region");
           binding.bottomAppBar.setVisibility(View.VISIBLE);
        }
        if(tag.equalsIgnoreCase("ET")){

            if(fragmentManager.findFragmentById(R.id.your_placeholder ) == searchResultFragment){
               fragmentTransaction = fragmentManager.beginTransaction();
               fragmentTransaction.replace(R.id.your_placeholder, resultFragment);
               fragmentTransaction.commit();
            }
            else{
                addFragment(resultFragment, "Result");
            }
            binding.bottomAppBar.setVisibility(GONE);
            binding.exploreBtn.setVisibility(GONE);
        }
        if(tag.equalsIgnoreCase("CLF")){
            binding.bottomAppBar.setVisibility(View.VISIBLE);
            addFragment(countryListFragment, "CountryListFragment");
        }
        if(tag.equalsIgnoreCase("home")){
            replaceFragment(searchFragment, "Home");
        }
        if(tag.equalsIgnoreCase("favorite")){
            addFragment(favoriteFragment, "Favorite");
            binding.bottomAppBar.setVisibility(View.VISIBLE);
        }
        if(tag.equalsIgnoreCase("SearchResult")){
            //addFragment(searchResultFragment, "SearchResult");
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.your_placeholder, searchResultFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            binding.exploreBtn.setVisibility(GONE);
        }
       if(tag.equalsIgnoreCase("back")){

           if(fragmentManager.getFragments().size() == 1){
               fragmentTransaction = fragmentManager.beginTransaction();
               fragmentTransaction.replace(R.id.your_placeholder, searchFragment);
               fragmentTransaction.commit();
               binding.bottomAppBar.setVisibility(View.VISIBLE);
               binding.exploreBtn.setVisibility(View.VISIBLE);

           }
           else{
               fragmentTransaction = fragmentManager.beginTransaction();
               fragmentTransaction.replace(R.id.your_placeholder, fragmentManager.getFragments().get(fragmentManager.getFragments().size() - 2));
               fragmentTransaction.commit();
               if (fragmentManager.getFragments().get(fragmentManager.getFragments().size() - 2) == searchFragment) {
                   if(getSupportFragmentManager().getFragments().contains(searchResultFragment)){
                       fragmentManager.beginTransaction().remove(searchResultFragment);
                   }
                   binding.bottomAppBar.setVisibility(View.VISIBLE);
                   binding.exploreBtn.setVisibility(View.VISIBLE);
               }
               else {
                   binding.bottomAppBar.setVisibility(View.VISIBLE);
               }

           }

       }


    }


    public void replaceFragment(Fragment  fragment, String tag){
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.your_placeholder, fragment, tag);
        fragmentTransaction.commit();
    }

    public void addFragment(Fragment fragment, String tag){
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.your_placeholder, fragment, tag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
