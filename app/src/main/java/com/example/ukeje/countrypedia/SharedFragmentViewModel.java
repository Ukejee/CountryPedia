package com.example.ukeje.countrypedia;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.example.ukeje.countrypedia.responses.CountryResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SharedFragmentViewModel extends ViewModel {

    private String searchedCountry;
    public CountryResponse countryDetails;
    private CountryRepository countryRepository = new CountryRepository();


    public String getSearchedCountry() {
        return searchedCountry;
    }

    public void setSearchedCountry(String searchedCountry) {
        this.searchedCountry = searchedCountry;
    }

    public CountryResponse loadCountryDetails(String countryName, CountryRepository.CountryApiResponseListener countryApiResponseListener){

        if(countryDetails == null){

           countryRepository.getCountry(countryName, countryApiResponseListener);
        }

        return  countryDetails;
    }
}
