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
        updateUI(sharedFragmentViewModel.countryDetails);

    }

    public void updateUI(CountryResponse cr){

        binding.countryName.setText(cr.getName());
        binding.capitalName.setText("It's capital is " + cr.getCapital());
        binding.subregionName.setText(cr.getSubregion());
        binding.regionName.setText(cr.getRegion());
        binding.countryCode.setText("+" + cr.getCallingCodes().get(0));
        binding.currencyCode.setText(cr.getCurrencies().get(0).getCode());
        binding.currencyName.setText(cr.getCurrencies().get(0).getName());
        binding.countryPopulation.setText(cr.getPopulation().toString());
        binding.countryLanguage.setText(cr.getLanguages().get(0).getName());

        if(cr.getLanguages().size() >= 1){

            for(int i = 1; i < cr.getLanguages().size(); i++){
                binding.countryLanguage.append(", ");
                binding.countryLanguage.append(cr.getLanguages().get(i).getName());
            }
        }


        binding.countryAltSpelling.setText(cr.getAltSpellings().get(0));
        for(int i = 1; i < cr.getAltSpellings().size(); i++){
            binding.countryAltSpelling.append(", ");
            binding.countryAltSpelling.append(cr.getAltSpellings().get(i));
            if( i > cr.getAltSpellings().size()){
                binding.countryLanguage.append(", ");
            }
        }

        binding.countryCoordinates.setText(cr.getLatlng().get(0).toString()+ "(Lat) "+cr.getLatlng().get(1).toString()
                +"(Long)");

        //THERE'S A BUG HERE; USE SWITCH STATEMENTS TO FIX
        binding.countryTimeZones.setText(cr.getTimezones().get(0));

        if(cr.getTimezones().size() <= 6){
            for(int i = 1; i < cr.getTimezones().size(); i++){
                binding.countryTimeZones.append("\n");
                binding.countryTimeZones.append(cr.getTimezones().get(i));
            }
        }

        if(cr.getTimezones().size() > 6){
            for(int i = 1; i < 6; i++){
                binding.countryTimeZones.append("\n");
                binding.countryTimeZones.append(cr.getTimezones().get(i));
            }
            for(int i = 6; i < cr.getTimezones().size(); i++){
                binding.countryTimeZoneTwo.append(cr.getTimezones().get(i));
                binding.countryTimeZoneTwo.append("\n");
            }
        }



    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String tag);
    }
}
