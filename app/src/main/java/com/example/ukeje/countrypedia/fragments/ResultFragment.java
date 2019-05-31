package com.example.ukeje.countrypedia.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.ukeje.countrypedia.SharedFragmentViewModel;
import com.example.ukeje.countrypedia.databinding.FragmentResultBinding;
import com.example.ukeje.countrypedia.web.helper.ApiResponseListener;
import com.example.ukeje.countrypedia.web.responses.CountryResponse;
import com.example.ukeje.countrypedia.web.responses.ErrorResponse;

import java.util.List;
import java.util.Objects;


public class ResultFragment extends Fragment {
    private FragmentResultBinding binding;
    private SharedFragmentViewModel sharedFragmentViewModel;
    private OnFragmentInteractionListener mListener;

    public ResultFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sharedFragmentViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(SharedFragmentViewModel.class);
        init();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentResultBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
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

        sharedFragmentViewModel.loadCountryDetails(new ApiResponseListener<List<CountryResponse>, ErrorResponse>() {
            @Override
            public void onApiSuccessful(List<CountryResponse> successResponse) {
                CountryResponse cr = successResponse.get(0);
                binding.countryName.setText(cr.getName());
                binding.capitalName.setText("It's capital is " + cr.getCapital());
                binding.subregionName.setText(cr.getSubregion());
                binding.regionName.setText(cr.getRegion());
                binding.countryCode.setText(cr.getCallingCodes().get(0));
                Toast.makeText(getContext(), successResponse.get(0).getName(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onApiFailed(@Nullable ErrorResponse errorResponse) {
                if (errorResponse == null) {
                    Log.e("COUNTRY PEDIA", "error is null");
                    return;
                }
                Toast.makeText(getContext(), errorResponse.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNetworkFailure() {
                Toast.makeText(getContext(), "Network failure", Toast.LENGTH_LONG).show();

            }
        });

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String tag);
    }
}
