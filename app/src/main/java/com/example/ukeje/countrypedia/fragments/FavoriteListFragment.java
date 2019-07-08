package com.example.ukeje.countrypedia.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ukeje.countrypedia.CountryRepository;
import com.example.ukeje.countrypedia.SharedFragmentViewModel;
import com.example.ukeje.countrypedia.adapters.FavoriteListAdapter;
import com.example.ukeje.countrypedia.database.Country;
import com.example.ukeje.countrypedia.databinding.FragmentFavoriteBinding;
import com.example.ukeje.countrypedia.web.helper.ApiResponseListener;
import com.example.ukeje.countrypedia.web.responses.CountryResponse;
import com.example.ukeje.countrypedia.web.responses.ErrorResponse;

import java.util.List;


public class FavoriteListFragment extends BaseFragment {

    private LinearLayoutManager linearLayoutManager;
    public FavoriteListAdapter myAdapter;
    private FragmentFavoriteBinding binding;

    private SharedFragmentViewModel viewModel;

    private List<Country> favoriteCountriesList;
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
                callSearchApi(v);
        }
    };

    private CountryRepository countryRepository;

    private OnFragmentInteractionListener mListener;

    public FavoriteListFragment() {
        // Required empty public constructor
    }

    @Override
    public String getFragmentTag() {
        return FAVORITE_LIST_FRAGMENT;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteBinding.inflate(getLayoutInflater(), container, false);
        viewModel = ViewModelProviders.of(getActivity()).get(SharedFragmentViewModel.class);
        init();
        return binding.getRoot();
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

    public void init(){

        linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.favoriteList.setLayoutManager(linearLayoutManager);
        getFavoriteCountries();


    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String tag);
    }

    public void getFavoriteCountries(){

        countryRepository = new CountryRepository(getActivity());

        new AsyncTask<Void, Void, List<Country>>() {
            @Override
            protected List<Country> doInBackground(Void...voids) {

                return countryRepository.getFavoriteCountries(true);
            }

            @Override
            protected void onPostExecute(List<Country> favoriteCountryList) {
                favoriteCountriesList = favoriteCountryList;
                myAdapter = new FavoriteListAdapter(favoriteCountriesList, listener, getActivity());
                binding.favoriteList.setAdapter(myAdapter);

            }
        }.execute();

    }

    public void callSearchApi(View view){
        viewModel.setSearchedCountry(favoriteCountriesList.get(binding.favoriteList.getChildLayoutPosition(view)).getName());
        viewModel.showProgressDialog(getActivity());
        viewModel.loadCountryDetails(new ApiResponseListener<List<CountryResponse>, ErrorResponse>() {
            @Override
            public void onApiSuccessful(List<CountryResponse> successResponse) {
                viewModel.countryDetails = successResponse.get(0);
                viewModel.cancelProgressDialog();
                onButtonPressed(SEARCH_COUNTRY_FRAGMENT);
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
}
