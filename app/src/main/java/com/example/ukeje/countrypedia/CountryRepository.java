package com.example.ukeje.countrypedia;

import com.example.ukeje.countrypedia.web.CountryPediaApiService;
import com.example.ukeje.countrypedia.web.helper.ApiCallHelper;
import com.example.ukeje.countrypedia.web.helper.ApiResponseListener;
import com.example.ukeje.countrypedia.web.responses.CountryResponse;
import com.example.ukeje.countrypedia.web.responses.ErrorResponse;

import java.util.List;

import retrofit2.Call;

public class CountryRepository {

    public static final String BASE_URL = "https://restcountries.eu/rest/";

    void getCountry(final String countryName, final ApiResponseListener<CountryResponse, ErrorResponse> apiResponseListener) {

        new ApiCallHelper<CountryResponse, ErrorResponse, CountryPediaApiService>
                (CountryRepository.BASE_URL, ErrorResponse.class, CountryPediaApiService.class) {

            @Override
            public Call<CountryResponse> createApiServiceCall(CountryPediaApiService apiService) {
                return apiService.getCountryDetails(countryName);
            }

        }.makeApiCall(apiResponseListener);
    }

    void getCountryList(final String continent, ApiResponseListener<List<CountryResponse>, ErrorResponse> apiResponseListener){

        new ApiCallHelper<List<CountryResponse>, ErrorResponse, CountryPediaApiService>
                (BASE_URL, ErrorResponse.class, CountryPediaApiService.class){

            @Override
            public Call<List<CountryResponse>> createApiServiceCall(CountryPediaApiService apiService){
                return apiService.getCountryList(continent);
            }
        }.makeApiCall(apiResponseListener);
    }
}