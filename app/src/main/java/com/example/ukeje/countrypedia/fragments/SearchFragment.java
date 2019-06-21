package com.example.ukeje.countrypedia.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.ukeje.countrypedia.CountryRepository;
import com.example.ukeje.countrypedia.MainActivity;
import com.example.ukeje.countrypedia.database.Country;
import com.example.ukeje.countrypedia.utils.AppUtils;
import com.example.ukeje.countrypedia.web.helper.ApiResponseListener;
import com.example.ukeje.countrypedia.web.responses.CountryResponse;
import com.example.ukeje.countrypedia.web.responses.ErrorResponse;

import androidx.fragment.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;

import com.example.ukeje.countrypedia.R;
import com.example.ukeje.countrypedia.SharedFragmentViewModel;
import com.example.ukeje.countrypedia.databinding.FragmentSearchBinding;
import com.google.android.material.navigation.NavigationView;

import java.util.List;
import java.util.Random;


public class SearchFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private  Country randomCountry;
    private List<Country> dbCountries;

    private Random random = new Random();
    private int ranNum = random.nextInt(246) + 1;
    private int dbSize;

    private BottomNavigationDrawerFragment navMenu;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public View v;
    FragmentSearchBinding binding;
    public CountryRepository countryRepository;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentSearchBinding.inflate(getLayoutInflater(), container, false);
        v = binding.getRoot();

        viewModel = ViewModelProviders.of(this.getActivity()).get(SharedFragmentViewModel.class);
        initView();
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

    private void initView(){

        countryRepository = new CountryRepository(getActivity().getApplicationContext());
        navMenu = new BottomNavigationDrawerFragment(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.region_menu_item:
                        onButtonPressed("FAB");
                        navMenu.dismiss();
                        return true;

                    case R.id.home_menu_item:
                        navMenu.dismiss();
                        return true;

                    case R.id.favorite_menu_item:
                        onButtonPressed("favorite");
                        navMenu.dismiss();
                        return true;
                }
                return true;
            }
        });

        binding.bottomAppBar.replaceMenu(R.menu.bottomappbar_menu);

        binding.exploreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed("FAB");

            }
        });

        binding.bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navMenu.show(getFragmentManager().beginTransaction(),"TAG");
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

        binding.knowMoreField.setClickable(true);
        binding.knowMoreField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callSearchApi(binding.countryTitle.getText().toString());
            }
        });

        populateDatabase();

        if(dbSize != 0){
            ranNum = random.nextInt(dbSize-1) + 1;
        }
        generateRandomCountry(ranNum);

        binding.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callSearchApi();
            }
        });

    }

    public void callSearchApi(){
        viewModel.setSearchedCountry(binding.countrySearchBox.getText().toString());
        viewModel.showProgressDialog(getActivity());
        viewModel.loadCountryDetails(new ApiResponseListener<List<CountryResponse>, ErrorResponse>() {
            @Override
            public void onApiSuccessful(List<CountryResponse> successResponse) {
                if(successResponse.size() > 1){
                    viewModel.countryDetails = successResponse.get(1);
                }
                else {
                    viewModel.countryDetails = successResponse.get(0);
                }
                viewModel.cancelProgressDialog();
                onButtonPressed("ET");
            }

            @Override
            public void onApiFailed(@Nullable ErrorResponse errorResponse) {
                if(errorResponse != null) {
                    viewModel.showAlert(errorResponse.getMessage(), getActivity());
                }
                else{
                    viewModel.showAlert("Not Found", getActivity());
                }
                viewModel.cancelProgressDialog();
            }

            @Override
            public void onNetworkFailure() {
                viewModel.showAlert("NETWORK FAILURE", getActivity());
                viewModel.cancelProgressDialog();

            }
        });
    }

    public void callSearchApi(String randomCountryName){
        viewModel.setSearchedCountry(randomCountryName);
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

    public void populateDatabase(){

        dbSize = 0;
        String test = "test";
        AsyncTask<String, Void, List<Country>> task = new AsyncTask<String, Void, List<Country>>(){

            @Override
            protected List<Country> doInBackground(String...params){
                return countryRepository.getCountries();
            }

            @Override
            protected void onPostExecute(List<Country> countryList){
                dbCountries = countryList;
                if(dbCountries.size() == 0){
                    String []countries = {"africa","asia","europe","americas","oceania"};
                    for(int i = 0; i < countries.length; i++){
                        AppUtils.showMessage(getActivity(),"PLEASE WAIT APP IS SETTING UP SOME THINGS");
                        viewModel.setRegionSelected(countries[i]);
                        viewModel.loadCountryList(new ApiResponseListener<List<CountryResponse>, ErrorResponse>() {
                            @Override
                            public void onApiSuccessful(List<CountryResponse> successResponse) {
                                for(int i = 0; i < successResponse.size(); i++){
                                    countryRepository.insertCountry(successResponse.get(i).getName(),successResponse.get(i).getCapital());
                                    dbSize = dbSize + successResponse.size();
                                }
                            }

                            @Override
                            public void onApiFailed(@Nullable ErrorResponse errorResponse) {

                                AppUtils.showMessage(getActivity().getApplicationContext(), "DATABASE" +
                                        "UPLOAD FALIED " + errorResponse.getMessage());
                            }

                            @Override
                            public void onNetworkFailure() {

                                AppUtils.showMessage(getActivity().getApplicationContext(), "DATABASE" +
                                        "UPLOAD FAILED" + "NETWORK ERROR");
                                AppUtils.showMessage(getActivity(), "Please check netork connectivity" +
                                        "and restart app");

                            }
                        });

                    }

                }
            }
        };

        task.execute(test);
    }

    public void generateRandomCountry(final int randomNo){

            AsyncTask<Integer, Void, Country> task = new AsyncTask<Integer, Void, Country>() {
                @Override
                protected Country doInBackground(Integer... ints) {

                    Country country = countryRepository.getCountry(randomNo);
                    return country;
                }

                @Override
                protected void onPostExecute(Country country) {
                    randomCountry = country;
                    if(randomCountry != null){
                        binding.countryTitle.setText(randomCountry.getName());
                        binding.capitalName.setText(randomCountry.getCapital());
                        binding.knowMoreField.setText("Know more about " + randomCountry.getName());
                    }


                }
            };

            task.execute(1);
    }

}
