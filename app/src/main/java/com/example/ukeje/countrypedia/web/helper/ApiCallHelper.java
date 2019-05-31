package com.example.ukeje.countrypedia.web.helper;

import androidx.annotation.Nullable;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author .: Oriaghan Uyi
 * @email ..: uyioriaghan@gmail.com, uyi.oriaghan@cwg-plc.com
 * @created : 2019-05-24 14:11
 */
public abstract class ApiCallHelper<S , E, T> {
    private Retrofit retrofit;
    private Class<T> retrofitApiService;
    private Class<E> errorResponse;

    public ApiCallHelper(String baseUrl, Class<E> errorResponse, Class<T> retrofitApiService) {
        retrofit = buildRetrofitClient(baseUrl);
        this.retrofitApiService = retrofitApiService;
        this.errorResponse = errorResponse;
    }

    private Retrofit buildRetrofitClient(String baseUrl) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    public abstract Call<S> createApiServiceCall(T apiService);

    public void makeApiCall(final ApiResponseListener<S , E> apiResponseListener) {

        T apiService = retrofit.create(retrofitApiService);

        Call<S> apiServiceCall = createApiServiceCall(apiService);

        apiServiceCall.enqueue(new Callback<S>() {

            @Override
            public void onResponse(@Nullable Call<S> call, @Nullable Response<S> response) {

                S cr;
                if (response != null) {
                    if (response.isSuccessful()) {
                        //if code is 200 - 300

                        cr = response.body();
                        apiResponseListener.onApiSuccessful(cr);
                    } else {
                        if (response.errorBody() != null)
                            try {
                                E errorBody = new Gson().fromJson(new JSONObject(response.errorBody().string()).toString(), errorResponse);

                                apiResponseListener.onApiFailed(errorBody);
                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                                apiResponseListener.onApiFailed(null);
                            }
                        else
                            apiResponseListener.onApiFailed(null);

                    }
                }
            }

            @Override
            public void onFailure(@Nullable Call<S> call, @Nullable Throwable t) {
                apiResponseListener.onNetworkFailure();

            }
        });

    }
}
