package com.example.ukeje.countrypedia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ukeje.countrypedia.database.Country
import com.example.ukeje.countrypedia.database.Favourite
import com.example.ukeje.countrypedia.repository.CountryPediaRepository
import com.example.ukeje.countrypedia.utils.AppUtils
import com.example.ukeje.countrypedia.web.helper.ApiResponse
import com.example.ukeje.countrypedia.web.responses.CountryResponse
import com.example.ukeje.countrypedia.web.responses.ErrorResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.math.ceil
import kotlin.math.roundToInt

class SharedFragmentViewModel : ViewModel() {

    private val regionSelected: String? = null
    var countryDetails: CountryResponse? = null
    var countryList: List<CountryResponse> = ArrayList()
    var countryPediaRepository: CountryPediaRepository? = null
    var favoriteCountries = ArrayList<String>()
    var funFacts: ArrayList<String>? = null

    var nigeriaCountry = Country("Nigeria", "Abuja", "566", "Africa", "https://restcountries.eu/data/nga.svg")

    var splitTimeZones = MutableLiveData<Boolean>()
        get() {
            field.value = countryDetails?.timezones?.size!! > 1
            return field
        }

    var timeZonesFirstHalf = MutableLiveData<String>()
        get() {
            val halfIndex = ceil((countryDetails?.timezones!!.size.toDouble() / 2))
            field.value = formatStringListIntoString(countryDetails?.timezones!!.subList(0, halfIndex.roundToInt()), "\n")
            return field
        }

    var timeZonesSecondHalf = MutableLiveData<String>()
        get() {
            val halfIndex = ceil((countryDetails?.timezones!!.size.toDouble() / 2))
            field.value = formatStringListIntoString(countryDetails?.timezones!!.subList(halfIndex.roundToInt() - 1, countryDetails?.timezones!!.lastIndex), "\n")
            return field
        }

    var formattedLanguages = " - "
        get() {
            field = formatStringListIntoString(countryDetails?.languages!!.map { it.name }, ", ")
            return field
        }

    var formattedAltSpelling = " - "
        get() {
            field = formatStringListIntoString(countryDetails?.altSpellings as List<String>, ", ")
            return field
        }

    var countryCallingCode = " - "
        get() {
            val callingCode = countryDetails!!.callingCodes[0]
            field = if (callingCode.isBlank()) " - " else "+$callingCode"
            return field
        }

    var randomCountryLiveData = MutableLiveData<Country>()

    fun callGetCountryDetailsApi(countryName: String): LiveData<ApiResponse<List<CountryResponse>, ErrorResponse>> {
        return countryPediaRepository!!.getCountryDetailsFromApi(countryName)
    }

    fun callGetCountryListByRegionApi(regionName: String): LiveData<ApiResponse<List<CountryResponse>, ErrorResponse>> {
        return countryPediaRepository!!.getCountryListFromApi(regionName)
    }

    fun initializingCountryListIntoDb(): LiveData<ApiResponse<List<CountryResponse>, ErrorResponse>> {
        return countryPediaRepository!!.initializingCountryListIntoDb()
    }

    fun setRandomCountry() {
        //get country list from db
        countryPediaRepository!!.compositeDisposable.add(countryPediaRepository!!.getAllCountriesFromDb()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    //generate random number
                    val ranNum = Random().nextInt(it.size - 1) + 1
                    randomCountryLiveData.value = it[ranNum]
                }, { throwable ->
                    AppUtils.error("unable to get random country. error: ${throwable.message}.\n showing default country: $nigeriaCountry")

                    randomCountryLiveData.value = nigeriaCountry
                }))

    }

    /**
     * @param: a list of LanguageResponse
     * This method formats the elements of the list and
     * returns a String to be displayed on the UI
     */
    private fun formatStringListIntoString(list: List<String>?, separator: String): String {

        val formattedString = StringBuilder()

        list?.forEachIndexed { index, string ->

            formattedString.append(string)

            if (list.size > 1 && index < list.lastIndex)
                formattedString.append(separator)

        }

        return formattedString.toString()
    }

    fun fetchFavouriteByNumericCode() = countryPediaRepository!!.fetchFavouriteByNumericCode(countryDetails!!.numericCode)

    fun deleteFavouriteByNumericCode() = countryPediaRepository!!.deleteFavouriteByNumericCode(countryDetails!!.numericCode)

    fun insertFavourite() = countryPediaRepository!!.insertFavourite(Favourite(countryDetails!!.numericCode))

    fun getFavouriteCountries() = countryPediaRepository!!.getFavouriteCountries()


    /*public ArrayList<String> getFavoriteCountries(){ return favoriteCountries; }


    public List<CountryResponse> getCountryList() {
        return countryList;
    }

    public void setCountryList(List<CountryResponse> countryList) {
        this.countryList = countryList;
    }

    public String getSearchedCountry() {
        return searchedCountry;
    }

    public void setSearchedCountry(String searchedCountry) {
        this.searchedCountry = searchedCountry;
    }

    public String getRegionSelected() {
        return regionSelected;
    }

    public void setRegionSelected(String regionSelected) {
        this.regionSelected = regionSelected;
    }

    public void loadCountryDetails(ApiResponseListener<List<CountryResponse>, ErrorResponse> apiResponseListener){
        countryPediaRepository.getCountryDetailsFromApi(getSearchedCountry());

    }

    public void loadCountryList(ApiResponseListener<List<CountryResponse>, ErrorResponse> apiResponseListener){
        countryPediaRepository.getCountryListFromApi(getRegionSelected());
    }

    public static void showProgressDialog(final Activity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mProgressDialog != null) {
                   if (mProgressDialog.isShowing() == true) cancelProgressDialog();
                }
                mProgressDialog = new Dialog(activity);
                mProgressDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                mProgressDialog.setContentView(R.layout.progress_indicator);
                mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mProgressDialog.show();
                mProgressDialog.setCancelable(false);
            }
        });

    }

    public static void showMessage(String title,String message, final FragmentActivity context){
            try {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(title);
                builder.setMessage(message);
                builder.setPositiveButton("OK", null);
                builder.show();
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
    }

    public static void showAlert(String message, final FragmentActivity context) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Error!");
            builder.setMessage(message);
            builder.setPositiveButton("OK", null);

            builder.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void cancelProgressDialog() {
        if (mProgressDialog != null){
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }
    }

    public void createFunFactDb(){

        funFacts = new ArrayList<>();
        funFacts.add("Did you know that there are 247 countries in the world");
        funFacts.add("Did you know that China is the most populated country in the world");
        funFacts.add("Did you know that India is the second most populous in the world");
        funFacts.add("Did you know France has the most timezones in the world");
        funFacts.add("Did you know that there are five regions in the world");
        funFacts.add("Did you know that Nigeria is the most populated country in Africa");
        funFacts.add("Africa has 54 countries!");
        funFacts.add("A third of world’s languages are spoken in Africa!");
        funFacts.add("Over 200 languages are spoken in Europe!");
        funFacts.add("The largest country in Europe is Russia!");
        funFacts.add("The smallest country in Europe is Vatican!");
        funFacts.add("The largest island in the world is Green Land!");
        //funFacts.add("")
    }*/

}
