package com.example.ukeje.countrypedia.fragments;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.ukeje.countrypedia.CountryRepository;
import com.example.ukeje.countrypedia.R;
import com.example.ukeje.countrypedia.SharedFragmentViewModel;
import com.example.ukeje.countrypedia.database.Country;
import com.example.ukeje.countrypedia.databinding.FragmentResultBinding;
import com.example.ukeje.countrypedia.utils.AppUtils;
import com.example.ukeje.countrypedia.web.helper.ApiResponseListener;
import com.example.ukeje.countrypedia.web.responses.CountryResponse;
import com.example.ukeje.countrypedia.web.responses.ErrorResponse;

import java.util.List;
import java.util.Objects;


public class ResultFragment extends Fragment {
    private FragmentResultBinding binding;
    private SharedFragmentViewModel sharedFragmentViewModel;
    private OnFragmentInteractionListener mListener;
    private CountryRepository countryRepository;
    public int countryDbId;
    private Country favoriteCountry;
    public boolean isFavorite = false;

    public ResultFragment() {
        // Required empty public constructor
    }

    public void onButtonPressed(String tag) {
        if (mListener != null) {
            mListener.onFragmentInteraction(tag);
        }
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

        countryRepository = new CountryRepository(getActivity());

        binding.backBtn.setClickable(true);
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    onButtonPressed("back");
            }
        });

        binding.favoriteBtn.setClickable(true);
        if(!sharedFragmentViewModel.getFavoriteCountries().contains(binding.countryName.getText())){
            binding.favoriteBtn.setTag(R.drawable.favorite_border);
            binding.favoriteBtn.setImageResource(R.drawable.favorite_border);
        }
        else{
            binding.favoriteBtn.setImageResource(R.drawable.favorite_two);
            binding.favoriteBtn.setTag(R.drawable.favorite_two);
        }

        binding.favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((int)binding.favoriteBtn.getTag() == R.drawable.favorite_border){
                    binding.favoriteBtn.setImageResource(R.drawable.favorite_two);
                    binding.favoriteBtn.setTag(R.drawable.favorite_two);
                    sharedFragmentViewModel.favoriteCountries.add(binding.countryName.getText().toString());
                    setCountryId();
                }
                else {
                    binding.favoriteBtn.setImageResource(R.drawable.favorite_border);
                    binding.favoriteBtn.setTag(R.drawable.favorite_border);
                    sharedFragmentViewModel.favoriteCountries.remove(binding.countryName.getText().toString());
                    setCountryId();
                }

            }
        });

    }

    //DISPLAY THE RESULTS FROM THE API TO THE UI
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

        if(cr.getLatlng().size() >= 2) {
            binding.countryCoordinates.setText(cr.getLatlng().get(0).toString() + "(Lat) " + cr.getLatlng().get(1).toString()
                    + "(Long)");
        }

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

    //THIS METHOD UPDATES THE VALUE OF THE FAVORITE COLUMN IN THE DATABASE
    public void setFavoriteCountry(){
        new AsyncTask<Void, Void, Country>() {
            @Override
            protected Country doInBackground(Void...voids) {
                return countryRepository.getCountry(countryDbId);
            }

            @Override
            protected void onPostExecute(Country country) {
                favoriteCountry = country;

                if((int)binding.favoriteBtn.getTag() == R.drawable.favorite_two){
                    favoriteCountry.setFavorite(true);
                    countryRepository.updateCountry(favoriteCountry);
                    AppUtils.showMessage(getActivity(),"Country Added to favorite");
                }
                if((int)binding.favoriteBtn.getTag() == R.drawable.favorite_border){
                    favoriteCountry.setFavorite(false);
                    countryRepository.updateCountry(favoriteCountry);
                    AppUtils.showMessage(getActivity(),"Country Has Been Removed From Favorites");

                }

            }
        }.execute();


    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String tag);
    }

    public  void setCountryId(){

        AsyncTask<Void, Void, Integer> task = new AsyncTask<Void, Void, Integer>() {

            @Override
            protected Integer doInBackground(Void...voids) {
                return countryRepository.getCountryId(binding.countryName.getText().toString());
            }

            @Override
            protected void onPostExecute(Integer countryId) {
                countryDbId = countryId.intValue();
                setFavoriteCountry();
            }

        };

        task.execute();
    }
}
