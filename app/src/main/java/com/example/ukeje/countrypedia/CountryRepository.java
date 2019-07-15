package com.example.ukeje.countrypedia;

import android.content.Context;
import android.os.AsyncTask;

import androidx.room.Room;

import com.example.ukeje.countrypedia.database.Country;
import com.example.ukeje.countrypedia.database.CountryPediaDatabase;
import com.example.ukeje.countrypedia.utils.AppUtils;
import com.example.ukeje.countrypedia.web.CountryPediaApiService;
import com.example.ukeje.countrypedia.web.helper.ApiCallHelper;
import com.example.ukeje.countrypedia.web.helper.ApiResponseListener;
import com.example.ukeje.countrypedia.web.responses.CountryResponse;
import com.example.ukeje.countrypedia.web.responses.ErrorResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class CountryRepository {

    public static final String BASE_URL = "https://restcountries.eu/rest/v2/";
    private static CountryPediaDatabase countryDatabase;
    private String DB_NAME = "db_country";

    public CountryRepository() {
    }

    ;

    public CountryRepository(Context context) {

        countryDatabase = Room.databaseBuilder(context, CountryPediaDatabase.class, DB_NAME).build();
    }

    public static void insertCountry(final Country country) {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {

                countryDatabase.countryDao().insertCountry(country);
                return null;
            }
        }.execute();
    }

    public static void updateCountry(final Country country) {

        country.setModifiedAt(AppUtils.getCurrentDateTime());

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {

                countryDatabase.countryDao().updateCountry(country);
                return null;
            }

        }.execute();
    }

    public static void deleteCountry(final int id) {

        final Country country = getCountry(id);
        if (country != null) {

            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... voids) {

                    countryDatabase.countryDao().deleteCountry(country);
                    return null;
                }
            }.execute();
        }
    }

    public static void deleteCountry(final Country country) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                countryDatabase.countryDao().deleteCountry(country);
                return null;
            }
        }.execute();
    }

    public static int getCountryId(final String countryName) {
        return countryDatabase.countryDao().fetchCountryIdByName(countryName);
    }

    public static Country getCountry(int id) {
        return new Country();
    }

    public static List<Country> getCountries() {

        return new ArrayList<>();
    }

    public static List<Country> getFavoriteCountries(boolean isFavorite) {

        return new ArrayList<>();
    }

    //DATABASE ACCESS METHODS
    public void insertCountry(String name, String capital) {

        insertCountry(name, capital, false);
    }

    public void insertCountry(String name, String capital, boolean favorite) {

        Country country = new Country();
        country.setName(name);
        country.setCapital(capital);
//        country.setFavorite(favorite);
        country.setCreatedAt(AppUtils.getCurrentDateTime());
        country.setModifiedAt(AppUtils.getCurrentDateTime());
        insertCountry(country);
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

    void getCountryList(final String regionName, final ApiResponseListener<List<CountryResponse>, ErrorResponse> apiResponseListener) {

        new ApiCallHelper<List<CountryResponse>, ErrorResponse, CountryPediaApiService>
                (CountryRepository.BASE_URL, ErrorResponse.class, CountryPediaApiService.class) {

            @Override
            public Call<List<CountryResponse>> createApiServiceCall(CountryPediaApiService apiService) {
                return apiService.getCountryList(regionName);
            }
        }.makeApiCall(apiResponseListener);
    }

}