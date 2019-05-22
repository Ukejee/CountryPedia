package com.example.ukeje.countrypedia;

import android.util.Log;
import android.widget.Toast;

import com.example.ukeje.countrypedia.responses.CountryResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CountryRepository {

    public static final String BASE_URL = "https://restcountries.eu/rest/";
    public static Retrofit retrofit = null;

    public static Retrofit getRetrofitClient(){

        if(retrofit == null){

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public CountryResponse getCountry(String countryName){

        final CountryResponse country = new CountryResponse();
        getRetrofitClient().create(CountryApi.class).getCountryDetails(countryName)
                .enqueue(new Callback<CountryResponse>() {
                    @Override
                    public void onResponse(Call<CountryResponse> call, Response<CountryResponse> response) {

                        CountryResponse cr =response.body();
                        country.setName(cr.getName());
                        country.setCapital(cr.getCapital());
                        country.setRegion(cr.getRegion());
                        country.setSubregion(cr.getSubregion());
                        country.setCallingCodes(cr.getCallingCodes());
                        country.setCurrencies(cr.getCurrencies());
                        country.setTimezones(cr.getTimezones());
                        country.setPopulation(cr.getPopulation());
                        country.setLanguages(cr.getLanguages());
                        country.setAltSpellings(cr.getAltSpellings());
                        country.setLatlng(cr.getLatlng());

                    }

                    @Override
                    public void onFailure(Call<CountryResponse> call, Throwable t) {

                        Log.e("NETWORK  ERROR:", "Failure on getting country data");

                    }
                });

        return country;
    }
}
