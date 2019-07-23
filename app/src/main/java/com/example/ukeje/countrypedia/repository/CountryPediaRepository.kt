package com.example.ukeje.countrypedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.ukeje.countrypedia.database.Country
import com.example.ukeje.countrypedia.database.CountryPediaDatabase
import com.example.ukeje.countrypedia.utils.AppUtils
import com.example.ukeje.countrypedia.web.CountryPediaApiService
import com.example.ukeje.countrypedia.web.helper.ApiCallHelper
import com.example.ukeje.countrypedia.web.helper.ApiResponse
import com.example.ukeje.countrypedia.web.helper.ResponseType
import com.example.ukeje.countrypedia.web.responses.CountryResponse
import com.example.ukeje.countrypedia.web.responses.ErrorResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call

class CountryPediaRepository(val countryPedeiaDatabase: CountryPediaDatabase) {

    companion object {
        private val DB_NAME = "db_country"
        val BASE_URL = "https://restcountries.eu/"
        private val countryDatabase: CountryPediaDatabase? = null
    }

    val compositeDisposable = CompositeDisposable()

    fun getAllCountriesFromApi(): LiveData<ApiResponse<List<CountryResponse>, ErrorResponse>> {

        return object : ApiCallHelper<List<CountryResponse>, ErrorResponse, CountryPediaApiService>(BASE_URL, ErrorResponse::class.java, CountryPediaApiService::class.java) {

            override fun createApiServiceCall(apiService: CountryPediaApiService): Call<List<CountryResponse>> {
                return apiService.getAllCountries()
            }

        }.makeApiCall()
    }

    fun getCountryDetailsFromApi(countryName: String): LiveData<ApiResponse<List<CountryResponse>, ErrorResponse>> {

        return object : ApiCallHelper<List<CountryResponse>, ErrorResponse, CountryPediaApiService>(BASE_URL, ErrorResponse::class.java, CountryPediaApiService::class.java) {

            override fun createApiServiceCall(apiService: CountryPediaApiService): Call<List<CountryResponse>> {
                return apiService.getCountryDetails(countryName)
            }

        }.makeApiCall()
    }

    fun getCountryListFromApi(regionName: String): LiveData<ApiResponse<List<CountryResponse>, ErrorResponse>> {

        return object : ApiCallHelper<List<CountryResponse>, ErrorResponse, CountryPediaApiService>(BASE_URL, ErrorResponse::class.java, CountryPediaApiService::class.java) {

            override fun createApiServiceCall(apiService: CountryPediaApiService): Call<List<CountryResponse>> {
                return apiService.getCountryList(regionName)
            }
        }.makeApiCall()
    }

    /**
     * This method calls the countryList api and persist the result in the db
     * @return liveData of apiResponse inorder to listen on the action to know when its done
     */
    fun initializingCountryListIntoDb(): LiveData<ApiResponse<List<CountryResponse>, ErrorResponse>> {
        //initialize live data
        val responseMediatorLiveData = MediatorLiveData<ApiResponse<List<CountryResponse>, ErrorResponse>>()
        //sets an initial response to loading
        responseMediatorLiveData.value = ApiResponse()

        //call api to get all the countries
        val allCountriesLiveData = getAllCountriesFromApi()

        //add allCountriesLiveData to mediatorLive data and observe on the result of the allCountriesLiveData
        responseMediatorLiveData.addSource(allCountriesLiveData) {
            when {
                it.responseType == ResponseType.LOADING -> {
                    responseMediatorLiveData.value = ApiResponse()
                }
                it.responseType == ResponseType.SUCCESS -> {
                    //clear db
                    clearCountryTable(onSuccess = {
                        //insert into db
                        insertCountries(it.successObject!!.map { item -> Country(item.name, item.capital, item.numericCode) },
                                onSuccess = {
                                    responseMediatorLiveData.value = it
                                },
                                onError = {
                                    AppUtils.error("An error occurred while inserting country list table")
                                    responseMediatorLiveData.value = ApiResponse(ResponseType.ERROR, null, ErrorResponse())
                                })
                    }, onError = {
                        AppUtils.error("An error occurred while deleting country table")
                        responseMediatorLiveData.value = ApiResponse(ResponseType.ERROR, null, ErrorResponse())
                    })
                }
                it.responseType == ResponseType.ERROR -> {
                    responseMediatorLiveData.value = it
                }
                it.responseType == ResponseType.NETWORK_FAILURE -> {
                    responseMediatorLiveData.value = it
                }
            }
        }

        return responseMediatorLiveData
    }

    private fun clearCountryTable(onSuccess: () -> Unit, onError: (Throwable) -> Unit) {
        compositeDisposable.add(countryPedeiaDatabase.countryDao().deleteAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    //inserted successfully
                    onSuccess()
                }, { throwable ->
                    onError(throwable)
                }))
    }

    private fun insertCountries(countryList: List<Country>, onSuccess: () -> Unit, onError: (Throwable) -> Unit) {
        compositeDisposable.add(insertCountries(countryList.map { item -> Country(item.name, item.capital, item.numericCode) })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    //inserted successfully
                    onSuccess()
                }, { throwable ->
                    onError(throwable)
                }))
    }

    private fun insertCountries(countryList: List<Country>) = countryPedeiaDatabase.countryDao().insertCountries(countryList)

    fun clearDisposable() = compositeDisposable.clear()

     fun getAllCountriesFromDb() =  countryPedeiaDatabase.countryDao().fetchAllCountries()






    /**
     * public CountryPediaRepository(Context context) {

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

    //                countryDatabase.countryDao().updateCountry(country);
    return null;
    }

    }.execute();
    }

    public static void deleteCountry(final int id) {

    final Country country = getCountryDetailsFromApi(id);
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

    public static Country getCountryDetailsFromApi(int id) {
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
     */
}