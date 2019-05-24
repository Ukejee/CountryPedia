package com.example.ukeje.countrypedia;

import androidx.lifecycle.ViewModel;

import com.example.ukeje.countrypedia.web.helper.ApiResponseListener;
import com.example.ukeje.countrypedia.web.responses.CountryResponse;
import com.example.ukeje.countrypedia.web.responses.ErrorResponse;

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

    public void loadCountryDetails(ApiResponseListener<CountryResponse, ErrorResponse> apiResponseListener){
        countryRepository.getCountry(getSearchedCountry(), apiResponseListener);

    }
}
