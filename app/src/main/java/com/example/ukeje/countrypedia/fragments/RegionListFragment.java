package com.example.ukeje.countrypedia.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ukeje.countrypedia.SharedFragmentViewModel;
import com.example.ukeje.countrypedia.adapters.RegionListAdapter;
import com.example.ukeje.countrypedia.databinding.FragmentRegionBinding;
import com.example.ukeje.countrypedia.web.helper.ApiResponseListener;
import com.example.ukeje.countrypedia.web.responses.CountryResponse;
import com.example.ukeje.countrypedia.web.responses.ErrorResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class RegionListFragment extends BaseFragment {

    View v;
    SharedFragmentViewModel viewModel;
    FragmentRegionBinding binding;
    BottomNavigationDrawerFragment navMenu;
    LinearLayoutManager linearLayoutManager;
    RegionListAdapter regionListAdapter;
    ArrayList<String> regionList = new ArrayList<>(Arrays.asList("Africa", "Americas", "Asia", "Europe", "Oceania"));
    private OnFragmentInteractionListener mListener;
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onClickRegion(v);
        }
    };

    public RegionListFragment() {
        // Required empty public constructor
    }

    @Override
    public String getFragmentTag() {
        return REGION_LIST_FRAGMENT;
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

    public void init() {

        linearLayoutManager = new LinearLayoutManager(getActivity());
        regionListAdapter = new RegionListAdapter(getActivity(), regionList, listener);
        binding.regionList.setLayoutManager(linearLayoutManager);
        binding.regionList.setAdapter(new RegionListAdapter(getActivity(), regionList, listener));

    }

    public void onClickRegion(View view) {
        callSearchApi(regionList.get(binding.regionList.getChildLayoutPosition(view)).toLowerCase());
        viewModel.setRegionSelected(regionList.get(binding.regionList.getChildLayoutPosition(view)));
    }

    public void callSearchApi(String region) {
        viewModel.setRegionSelected(region);
        viewModel.showProgressDialog(getActivity());
        viewModel.loadCountryList(new ApiResponseListener<List<CountryResponse>, ErrorResponse>() {
            @Override
            public void onApiSuccessful(List<CountryResponse> successResponse) {
                viewModel.countryList = successResponse;
                viewModel.cancelProgressDialog();
                onButtonPressed(COUNTRY_LIST_FRAGMENT);
            }

            @Override
            public void onApiFailed(@Nullable ErrorResponse errorResponse) {
                viewModel.showAlert(errorResponse.getMessage(), getActivity());
                viewModel.cancelProgressDialog();
            }

            @Override
            public void onNetworkFailure() {
                viewModel.showAlert("NETWORK FAILURE", getActivity());
                viewModel.cancelProgressDialog();

            }
        });
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String tag);
    }
}
