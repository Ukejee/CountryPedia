package com.example.ukeje.countrypedia;

import com.example.ukeje.countrypedia.fragments.CountryListFragment;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.ukeje.countrypedia.fragments.RegionFragment;
import com.example.ukeje.countrypedia.fragments.ResultFragment;
import com.example.ukeje.countrypedia.fragments.SearchFragment;

public class MainActivity extends AppCompatActivity implements RegionFragment.OnFragmentInteractionListener,
                                                                SearchFragment.OnFragmentInteractionListener,
                                                                ResultFragment.OnFragmentInteractionListener{

    BottomAppBar bottomAppBar;
    FloatingActionButton continentBtn;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    RegionFragment regionFragment;
    SearchFragment searchFragment;
    ResultFragment resultFragment;
    CountryListFragment countryListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        regionFragment = new RegionFragment();
        searchFragment = new SearchFragment();
        resultFragment = new ResultFragment();
        countryListFragment = new CountryListFragment();

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.your_placeholder, searchFragment);
        fragmentTransaction.commit();


    }

    public void onFragmentInteraction(String tag){

        if(tag.equalsIgnoreCase("FAB")){
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.your_placeholder, regionFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        if(tag.equalsIgnoreCase("ET")){
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.your_placeholder, resultFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }
        if(tag.equalsIgnoreCase("CLF")){
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.your_placeholder, countryListFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
}
