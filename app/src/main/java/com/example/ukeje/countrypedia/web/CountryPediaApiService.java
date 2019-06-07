package com.example.ukeje.countrypedia.web;

import com.example.ukeje.countrypedia.web.responses.CountryResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CountryPediaApiService {

    @GET("name/{name}")
    Call<List<CountryResponse>> getCountryDetails(@Path("name") String countryName);

    @GET("region/{region}")
    Call<List<CountryResponse>> getCountryList(@Path("region") String regionName);

}