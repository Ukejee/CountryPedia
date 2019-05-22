package com.example.ukeje.countrypedia;

import android.net.Uri;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.ukeje.countrypedia.fragments.ContinentFragment;
import com.example.ukeje.countrypedia.fragments.SearchFragment;

public class MainActivity extends AppCompatActivity implements ContinentFragment.OnFragmentInteractionListener,
                                                                SearchFragment.OnFragmentInteractionListener{

    BottomAppBar bottomAppBar;
    FloatingActionButton continentBtn;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    ContinentFragment continentFragment;
    SearchFragment searchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        continentFragment = new ContinentFragment();
        searchFragment = new SearchFragment();

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.your_placeholder, searchFragment);
        fragmentTransaction.commit();


    }

    public void onFragmentInteraction(){

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.your_placeholder, continentFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
