package com.example.ukeje.countrypedia;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Room;

import com.example.ukeje.countrypedia.database.Country;
import com.example.ukeje.countrypedia.database.CountryDatabase;
import com.example.ukeje.countrypedia.utils.AppUtils;
import com.example.ukeje.countrypedia.web.CountryPediaApiService;
import com.example.ukeje.countrypedia.web.helper.ApiCallHelper;
import com.example.ukeje.countrypedia.web.helper.ApiResponseListener;
import com.example.ukeje.countrypedia.web.responses.CountryResponse;
import com.example.ukeje.countrypedia.web.responses.ErrorResponse;

import java.util.List;

import retrofit2.Call;

public class CountryRepository {

    public static final String BASE_URL = "https://restcountries.eu/rest/v2/";
    private String DB_NAME = "db_country";

    private static CountryDatabase countryDatabase;

    public CountryRepository(){};

    public CountryRepository(Context context){

        countryDatabase = Room.databaseBuilder(context, CountryDatabase.class, DB_NAME).build();
    }

    //DATABASE ACCESS METHODS
    public void insertCountry(String name, String capital){

        insertCountry(name, capital, false);
    }

    public void insertCountry(String name,  String capital, boolean favorite){

        Country country = new Country();
        country.setName(name);
        country.setCapital(capital);
        country.setFavorite(favorite);
        country.setCreatedAt(AppUtils.getCurrentDateTime());
        country.setModifiedAt(AppUtils.getCurrentDateTime());
        insertCountry(country);
    }

    public static   void insertCountry(final Country country){

        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void...voids){

                countryDatabase.daoAccess().insertCountry(country);
                return null;
            }
        }.execute();
    }

    public static void updateCountry(final Country country){

        country.setModifiedAt(AppUtils.getCurrentDateTime());

        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void...voids){

                countryDatabase.daoAccess().updateCountry(country);
                return null;
            }
        }.execute();
    }

    public static void deleteCountry(final int id){

        final Country country = getCountry(id);
        if(country != null){

            new AsyncTask<Void, Void, Void>(){

                @Override
                protected Void doInBackground(Void...voids){

                    countryDatabase.daoAccess().deleteCountry(country);
                    return null;
                }
            }.execute();
        }
    }

    public static void deleteCountry(final Country country) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                countryDatabase.daoAccess().deleteCountry(country);
                return null;
            }
        }.execute();
    }

    public static Country getCountry(int id) {
        return countryDatabase.daoAccess().getCountry(id);
    }

    public static List<Country> getCountrys() {

        return countryDatabase.daoAccess().fectchAllCountries();
    }





    //API REQUEST METHODS
    void getCountry(final String countryName, final ApiResponseListener<List<CountryResponse>, ErrorResponse> apiResponseListener) {

        new ApiCallHelper<List<CountryResponse>, ErrorResponse, CountryPediaApiService>
                (CountryRepository.BASE_URL, ErrorResponse.class, CountryPediaApiService.class) {

            @Override
            public Call<List<CountryResponse>> createApiServiceCall(CountryPediaApiService apiService) {
                return apiService.getCountryDetails(countryName);
            }

        }.makeApiCall(apiResponseListener);
    }

    void getCountryList(final String regionName, final ApiResponseListener<List<CountryResponse>, ErrorResponse> apiResponseListener){

        new ApiCallHelper<List<CountryResponse>, ErrorResponse, CountryPediaApiService>
                (CountryRepository.BASE_URL, ErrorResponse.class, CountryPediaApiService.class){

            @Override
            public Call<List<CountryResponse>> createApiServiceCall(CountryPediaApiService apiService){
                return apiService.getCountryList(regionName);
            }
        }.makeApiCall(apiResponseListener);
    }

}