package com.example.ukeje.countrypedia.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ukeje.countrypedia.SharedFragmentViewModel;
import com.example.ukeje.countrypedia.adapters.CountryListAdapter;
import com.example.ukeje.countrypedia.databinding.FragmentCountryListBinding;
import com.example.ukeje.countrypedia.utils.AppUtils;


public class CountryListFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCountryListBinding.inflate(getLayoutInflater(), container, false);
        v = binding.getRoot();
        viewModel = ViewModelProviders.of(this.getActivity()).get(SharedFragmentViewModel.class);
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

        binding.regionTitle.setText(viewModel.getRegionSelected());
        layoutManager = new LinearLayoutManager(getActivity());
        binding.countryList.setLayoutManager(layoutManager);

        mAdapter = new CountryListAdapter(viewModel.countryList, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onClickCountryList(v);
            }
        }, v.getContext());
        layoutManager = new LinearLayoutManager(this.getActivity());
        binding.countryList.setLayoutManager(layoutManager);
        binding.countryList.setAdapter(mAdapter);
    }

    public void onClickCountryList(View view){

        viewModel.countryDetails = viewModel.countryList.get(binding.countryList.getChildLayoutPosition(view));
        onButtonPressed("ET");
    }


}
