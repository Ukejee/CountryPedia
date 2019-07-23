package com.example.ukeje.countrypedia.web

import com.example.ukeje.countrypedia.web.responses.CountryResponse

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CountryPediaApiService {

    @GET("rest/v2/all")
    fun getAllCountries(): Call<List<CountryResponse>>

    @GET("rest/v2/name/{name}")
    fun getCountryDetails(@Path("name") countryName: String): Call<List<CountryResponse>>

    @GET("rest/v2/region/{region}")
    fun getCountryList(@Path("region") regionName: String): Call<List<CountryResponse>>

}