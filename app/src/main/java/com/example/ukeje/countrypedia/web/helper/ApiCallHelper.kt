package com.example.ukeje.countrypedia.web.helper

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * @author .: Oriaghan Uyi
 * @email ..: uyioriaghan@gmail.com, uyi.oriaghan@cwg-plc.com
 * @created : 2019-05-24 14:11
 */
abstract class ApiCallHelper<S, E, T>(baseUrl: String, private val errorResponse: Class<E>, private val retrofitApiService: Class<T>) {
    private val retrofit: Retrofit

    init {
        retrofit = buildRetrofitClient(baseUrl)
    }

    private fun buildRetrofitClient(baseUrl: String): Retrofit {

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build()

        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())

                .build()

    }

    abstract fun createApiServiceCall(apiService: T): Call<S>

    fun makeApiCall(): LiveData<ApiResponse<S, E>> {

        val apiResponseLiveData = MutableLiveData<ApiResponse<S, E>>()
        //push initial api response with loading
        apiResponseLiveData.value = ApiResponse()

        val apiService = retrofit.create(retrofitApiService)

        val apiServiceCall = createApiServiceCall(apiService)

        apiServiceCall.enqueue(object : Callback<S> {

            override fun onResponse(call: Call<S>?, response: Response<S>?) {

                apiResponseLiveData.value = if (response != null) {
                    if (response.isSuccessful) {
                        //if code is 200 - 300
                        ApiResponse(ResponseType.SUCCESS, response.body())
                    } else {
                        buildErrorBody(response)
                    }
                } else
                    ApiResponse(ResponseType.ERROR, null, errorResponse.newInstance())
            }

            override fun onFailure(call: Call<S>?, t: Throwable?) {
                apiResponseLiveData.value = ApiResponse(ResponseType.NETWORK_FAILURE, null, errorResponse.newInstance())
            }
        })

        return apiResponseLiveData
    }

    private fun buildErrorBody(retrofitResponse: Response<S>): ApiResponse<S, E> {
        val apiResponse: ApiResponse<S, E>?

        if (retrofitResponse.errorBody() != null) {
            apiResponse = try {
                //converts retrofit error body to our passed in error Type
                val errorBody = Gson().fromJson(JSONObject(retrofitResponse.errorBody()!!.string()).toString(), errorResponse)
                ApiResponse(ResponseType.ERROR, null, errorBody)
            } catch (e: JSONException) {
                e.printStackTrace()
                ApiResponse(ResponseType.ERROR, null, errorResponse.newInstance())
            } catch (e: IOException) {
                e.printStackTrace()
                ApiResponse(ResponseType.ERROR, null, errorResponse.newInstance())
            }
        } else {
            apiResponse = ApiResponse(ResponseType.ERROR, null, errorResponse.newInstance())
        }

        return apiResponse
    }
}

data class ApiResponse<S, E>(
        val responseType: ResponseType = ResponseType.LOADING,
        val successObject: S? = null,
        val errorObject: E? = null)

enum class ResponseType {
    LOADING, SUCCESS, ERROR, NETWORK_FAILURE
}
