package com.example.ukeje.countrypedia;

import android.util.Log;

import androidx.annotation.Nullable;

import com.example.ukeje.countrypedia.responses.CountryResponse;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CountryRepository {

    private static final String BASE_URL = "https://restcountries.eu/rest/";

    private Retrofit getRetrofitClient() {


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();

            return  new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

    }

    void getCountry(String countryName, final CountryApiResponseListener countryApiResponseListener) {

        getRetrofitClient().create(CountryApi.class).getCountryDetails(countryName)
                .enqueue(new Callback<CountryResponse>() {

                    @Override
                    public void onResponse(@Nullable Call<CountryResponse> call, @Nullable Response<CountryResponse> response) {

                        Log.d("COUNTRY DETAILS API", "Api received received");


                        CountryResponse cr;
                        if (response != null) {
                            if(response.isSuccessful()) {
                                cr = response.body();
                                countryApiResponseListener.onApiSuccessful(cr);
                            }else{
                                countryApiResponseListener.onApiFailed();
                            }

                        }

                    }

                    @Override
                    public void onFailure(@Nullable Call<CountryResponse> call, @Nullable Throwable t) {
                        Log.e("COUNTRY DETAILS API", "Api received failed");

                        countryApiResponseListener.onNetworkFailure();


                    }
                });

    }

    public interface CountryApiResponseListener {

        void onApiSuccessful(CountryResponse countryResponse);
        void onApiFailed();
        void onNetworkFailure();
    }
}
