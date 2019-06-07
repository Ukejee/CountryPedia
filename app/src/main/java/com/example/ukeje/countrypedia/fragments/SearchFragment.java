package com.example.ukeje.countrypedia.fragments;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import com.example.ukeje.countrypedia.MainActivity;
import com.example.ukeje.countrypedia.web.helper.ApiResponseListener;
import com.example.ukeje.countrypedia.web.responses.CountryResponse;
import com.example.ukeje.countrypedia.web.responses.ErrorResponse;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.example.ukeje.countrypedia.R;
import com.example.ukeje.countrypedia.SharedFragmentViewModel;
import com.example.ukeje.countrypedia.databinding.FragmentSearchBinding;

import java.util.List;



public class SearchFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public BottomNavigationDrawerFragment navMenu;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public View v;
    FragmentSearchBinding binding;

    //ViewModel for fragment
    private SharedFragmentViewModel viewModel;

    public SearchFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentSearchBinding.inflate(getLayoutInflater(), container, false);
        v = binding.getRoot();

        viewModel = ViewModelProviders.of(this.getActivity()).get(SharedFragmentViewModel.class);
        initView();
        //setUpBottomAppBar();
        return  v;
    }




    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String tag) {
        if (mListener != null) {
            mListener.onFragmentInteraction(tag);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    //Methods to be implemented in the UI are declared here
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String tag);
    }

    public void initView(){

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed("FAB");
            }
        };
        navMenu = new BottomNavigationDrawerFragment();

        binding.bottomAppBar.replaceMenu(R.menu.bottomappbar_menu);

        binding.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                callSearchApi();
                onButtonPressed("FAB");
            }
        });

        binding.bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navMenu.show(getFragmentManager().beginTransaction(),"TAG");
                //navMenu.onOptionsItemSelected((MenuItem) v.findViewById(R.id.nav1));
            }
        });

        binding.countrySearchBox.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(keyCode == EditorInfo.IME_ACTION_DONE || keyCode == EditorInfo.IME_ACTION_SEARCH
                        || event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER){

                    if(!event.isShiftPressed()){
                       callSearchApi();
                    }
                }
                return false;
            }
        });
    }

    public void callSearchApi(){
        viewModel.setSearchedCountry(binding.countrySearchBox.getText().toString());
        viewModel.showProgressDialog(getActivity());
        viewModel.loadCountryDetails(new ApiResponseListener<List<CountryResponse>, ErrorResponse>() {
            @Override
            public void onApiSuccessful(List<CountryResponse> successResponse) {
                viewModel.countryDetails = successResponse.get(0);
                viewModel.cancelProgressDialog();
                onButtonPressed("ET");
            }

            @Override
            public void onApiFailed(@Nullable ErrorResponse errorResponse) {
                viewModel.showAlert(errorResponse.getMessage(),getActivity());
                viewModel.cancelProgressDialog();
            }

            @Override
            public void onNetworkFailure() {
                viewModel.showAlert("NETWORK FAILURE", getActivity());
                viewModel.cancelProgressDialog();

            }
        });
    }

    public void setUpBottomAppBar(){

        ((MainActivity)getActivity()).setSupportActionBar(binding.bottomAppBar);
        binding.bottomAppBar.inflateMenu(R.menu.bottomappbar_menu);
    }

}
