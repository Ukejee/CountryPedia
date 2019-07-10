package com.example.ukeje.countrypedia.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ukeje.countrypedia.SharedFragmentViewModel;
import com.example.ukeje.countrypedia.adapters.CountryListAdapter;
import com.example.ukeje.countrypedia.databinding.FragmentCountryListBinding;


public class CountryListFragment extends BaseFragment {


    public CountryListAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    View v;
    FragmentCountryListBinding binding;
    SharedFragmentViewModel viewModel;

    public CountryListFragment() {
        // Required empty public constructor
    }


    public static CountryListFragment newInstance(String param1, String param2) {
        CountryListFragment fragment = new CountryListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public String getFragmentTag() {
        return COUNTRY_LIST_FRAGMENT;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCountryListBinding.inflate(getLayoutInflater(), container, false);
        v = binding.getRoot();
        viewModel = ViewModelProviders.of(this.getActivity()).get(SharedFragmentViewModel.class);
        init();
        return v;
    }

    public void init(){

        binding.regionTitle.setText(viewModel.getRegionSelected());
        layoutManager = new LinearLayoutManager(getActivity());
        binding.countryList.setLayoutManager(layoutManager);

        mAdapter = new CountryListAdapter(viewModel.countryList, v -> onClickCountryList(v), v.getContext());

        layoutManager = new LinearLayoutManager(this.getActivity());
        binding.countryList.setLayoutManager(layoutManager);
        binding.countryList.setAdapter(mAdapter);

    }

    public void onClickCountryList(View view){

        viewModel.countryDetails = viewModel.countryList.get(binding.countryList.getChildLayoutPosition(view));
//        onButtonPressed(COUNTRY_DETAILS_FRAGMENT);
    }


}
