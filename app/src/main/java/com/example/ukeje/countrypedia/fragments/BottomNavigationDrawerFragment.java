package com.example.ukeje.countrypedia.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.ukeje.countrypedia.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;

/**
 * @author .: Ukeje Emeka
 * @email ..: ukejee3@gmail.com
 * @created : 6/6/19
 */
public class BottomNavigationDrawerFragment extends BottomSheetDialogFragment {

    private final NavigationView.OnNavigationItemSelectedListener listener;

    public BottomNavigationDrawerFragment(NavigationView.OnNavigationItemSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_bottomsheet, container, false);

        NavigationView nav = view.findViewById(R.id.navigation_view);

        nav.setNavigationItemSelectedListener(listener);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

    }


}
