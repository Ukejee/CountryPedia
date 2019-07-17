package com.example.ukeje.countrypedia;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.example.ukeje.countrypedia.database.Country;
import com.example.ukeje.countrypedia.database.CountryPediaDatabase;
import com.example.ukeje.countrypedia.web.CountryPediaApiService;
import com.example.ukeje.countrypedia.web.helper.ApiCallHelper;
import com.example.ukeje.countrypedia.web.helper.ApiResponse;
import com.example.ukeje.countrypedia.web.responses.CountryResponse;
import com.example.ukeje.countrypedia.web.responses.ErrorResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class CountryRepository {

    public static final String BASE_URL = "https://restcountries.eu/rest/v2/";
    private static CountryPediaDatabase countryDatabase;
    private CountryPediaDatabase countryPedeiaDatabase;
    private String DB_NAME = "db_country";

    public CountryRepository() {
    }

    public CountryRepository(CountryPediaDatabase countryPedeiaDatabase) {

        this.countryPedeiaDatabase = countryPedeiaDatabase;
    }

    //API REQUEST METHODS
    LiveData<ApiResponse<List<CountryResponse>, ErrorResponse>> getCountryDetails(final String countryName) {

        return new ApiCallHelper<List<CountryResponse>, ErrorResponse, CountryPediaApiService>
                (CountryRepository.BASE_URL, ErrorResponse.class, CountryPediaApiService.class) {

            @NonNull
            @Override
            public Call<List<CountryResponse>> createApiServiceCall(CountryPediaApiService apiService) {
                return apiService.getCountryDetails(countryName);
            }

        }.makeApiCall();
    }

    LiveData<ApiResponse<List<CountryResponse>, ErrorResponse>> getCountryList(final String regionName) {

        return  new ApiCallHelper<List<CountryResponse>, ErrorResponse, CountryPediaApiService>
                (CountryRepository.BASE_URL, ErrorResponse.class, CountryPediaApiService.class) {

            @NonNull
            @Override
            public Call<List<CountryResponse>> createApiServiceCall(CountryPediaApiService apiService) {
                return apiService.getCountryList(regionName);
            }
        }.makeApiCall();
    }






















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

//        country.setModifiedAt(AppUtils.getCurrentDateTime());

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {

                countryDatabase.countryDao().updateCountry(country);
                return null;
            }

        }.execute();
    }

    public static void deleteCountry(final int id) {

        final Country country = getCountryDetails(id);
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

//    public static int getCountryId(final String countryName) {
//        return countryDatabase.countryDao().fetchCountryIdByName(countryName);
//    }

    public static Country getCountryDetails(int id) {
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
//        country.setCreatedAt(AppUtils.getCurrentDateTime());
//        country.setModifiedAt(AppUtils.getCurrentDateTime());
        insertCountry(country);
    }

}