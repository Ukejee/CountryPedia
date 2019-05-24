package com.example.ukeje.countrypedia;

import com.example.ukeje.countrypedia.responses.CountryResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CountryApi {

    @GET("v2/name/{name}")
    Call<CountryResponse> getCountryDetails(@Path("name") String countryName);

}
