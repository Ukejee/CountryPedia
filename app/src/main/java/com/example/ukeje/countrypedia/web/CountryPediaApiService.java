package com.example.ukeje.countrypedia.web;

import com.example.ukeje.countrypedia.web.responses.CountryResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CountryPediaApiService {

    @GET("v2/name/{name}")
    Call<CountryResponse> getCountryDetails(@Path("name") String countryName);

    @GET("v2/name/{continent}")
    Call<List<CountryResponse>> getCountryList(@Path("continent") String continentName);

}