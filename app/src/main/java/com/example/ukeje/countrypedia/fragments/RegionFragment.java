package com.example.ukeje.countrypedia.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ukeje.countrypedia.R;
import com.example.ukeje.countrypedia.SharedFragmentViewModel;
import com.example.ukeje.countrypedia.databinding.FragmentRegionBinding;
import com.example.ukeje.countrypedia.web.helper.ApiResponseListener;
import com.example.ukeje.countrypedia.web.responses.CountryResponse;
import com.example.ukeje.countrypedia.web.responses.ErrorResponse;

import java.util.List;
import java.util.Objects;


public class RegionFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    View v;
    SharedFragmentViewModel viewModel;
    FragmentRegionBinding binding;

    public RegionFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static RegionFragment newInstance(String param1, String param2) {
        RegionFragment fragment = new RegionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRegionBinding.inflate(getLayoutInflater(), container, false);
        viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(SharedFragmentViewModel.class);
        init();
        v = binding.getRoot();
        init();

        return v;
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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String tag);
    }

    public void init(){

        binding.continentOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRegion(binding.continentOne);
            }
        });

        binding.continentTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRegion(binding.continentTwo);
            }
        });

        binding.continentThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRegion(binding.continentThree);
            }
        });

        binding.continentFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRegion(binding.continentFour);
            }
        });

        binding.continentFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRegion(binding.continentFive);
            }
        });
    }

    public void onClickRegion(View view){
        if(view.getId() == R.id.africa_title || view.getId() ==R.id.continent_one){
            callSearchApi("africa");
        }
        else if(view.getId() == R.id.americas_title || view.getId() == R.id.continent_two){
            callSearchApi("americas");
        }
        else if(view.getId() == R.id.asia_title || view.getId() == R.id.continent_three){
            callSearchApi("asia");
        }
        else if(view.getId() == R.id.europe_title || view.getId() == R.id.continent_four){
            callSearchApi("europe");
        }
        else if(view.getId() == R.id.oceania_title || view.getId() == R.id.continent_five){
            callSearchApi("oceania");
        }
    }

    public void callSearchApi(String region){
        viewModel.setRegionSelected(region);
        viewModel.showProgressDialog(getActivity());
        viewModel.loadCountryList(new ApiResponseListener<List<CountryResponse>, ErrorResponse>() {
            @Override
            public void onApiSuccessful(List<CountryResponse> successResponse) {
                viewModel.countryList = successResponse;
                viewModel.cancelProgressDialog();
                onButtonPressed("CLF");
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
}
