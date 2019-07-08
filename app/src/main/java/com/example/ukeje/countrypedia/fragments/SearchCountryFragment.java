package com.example.ukeje.countrypedia.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ukeje.countrypedia.SharedFragmentViewModel;
import com.example.ukeje.countrypedia.adapters.SearchResultListAdapter;
import com.example.ukeje.countrypedia.databinding.FragmentSearchResultBinding;
import com.example.ukeje.countrypedia.web.helper.ApiResponseListener;
import com.example.ukeje.countrypedia.web.responses.CountryResponse;
import com.example.ukeje.countrypedia.web.responses.ErrorResponse;

import java.util.List;


public class SearchCountryFragment extends BaseFragment {

    private View v;
    private RecyclerView.LayoutManager linearLayoutManager;
    private SearchResultListAdapter searchResultListAdapter;

    private SharedFragmentViewModel viewModel;

    private List<CountryResponse> searchResultList;
    private FragmentSearchResultBinding binding;

    private OnFragmentInteractionListener mListener;

    public SearchCountryFragment() {
        // Required empty public constructor
    }

    @Override
    public String getFragmentTag() {
        return SEARCH_COUNTRY_FRAGMENT;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSearchResultBinding.inflate(getLayoutInflater(), container, false);
        v = binding.getRoot();
        viewModel = ViewModelProviders.of(getActivity()).get(SharedFragmentViewModel.class);

        init();
        binding.editText.requestFocus();
        appUtils.openKeyboard();
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
        mListener = null;;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String tag);
    }

    private void init(){

        linearLayoutManager =  new LinearLayoutManager(getActivity());
        binding.listView.setLayoutManager(linearLayoutManager);

        binding.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                //callSearchApi(s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                callSearchApi(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

               callSearchApi(s.toString());
            }
        });
    }

    public void callSearchApi(final String countryName){

        viewModel.setSearchedCountry(countryName);
        viewModel.loadCountryDetails(new ApiResponseListener<List<CountryResponse>, ErrorResponse>() {
            @Override
            public void onApiSuccessful(final List<CountryResponse> successResponse) {

                if(binding.errorMessage != null){
                    binding.errorMessage.setVisibility(View.GONE);
                }
                binding.listView.setVisibility(View.VISIBLE);
                searchResultList = successResponse;
                searchResultListAdapter = new SearchResultListAdapter(getActivity(), searchResultList,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                viewModel.countryDetails = successResponse.get(binding.listView.getChildLayoutPosition(v));
                                onButtonPressed("et");
                                appUtils.hideKeyboard();
                            }
                        });
                binding.listView.setAdapter(searchResultListAdapter);
            }

            @Override
            public void onApiFailed(@Nullable ErrorResponse errorResponse) {
                binding.listView.setVisibility(View.GONE);
                binding.errorMessage.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNetworkFailure() {
                binding.listView.setVisibility(View.GONE);
                binding.errorMessage.setVisibility(View.VISIBLE);
                binding.errorMessage.setText("No Internet Connection");
            }
        });

    }




}
