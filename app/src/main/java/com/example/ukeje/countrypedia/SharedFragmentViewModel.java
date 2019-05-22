package com.example.ukeje.countrypedia;

import android.arch.lifecycle.ViewModel;

public class SharedFragmentViewModel extends ViewModel {

    private String searchedCountry;


    public String getSearchedCountry() {
        return searchedCountry;
    }

    public void setSearchedCountry(String searchedCountry) {
        this.searchedCountry = searchedCountry;
    }
}
