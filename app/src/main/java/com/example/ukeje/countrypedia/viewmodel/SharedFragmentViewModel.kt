package com.example.ukeje.countrypedia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ukeje.countrypedia.database.Country
import com.example.ukeje.countrypedia.repository.CountryPediaRepository
import com.example.ukeje.countrypedia.utils.AppUtils
import com.example.ukeje.countrypedia.web.helper.ApiResponse
import com.example.ukeje.countrypedia.web.responses.CountryResponse
import com.example.ukeje.countrypedia.web.responses.ErrorResponse
import com.example.ukeje.countrypedia.web.responses.LanguageResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class SharedFragmentViewModel : ViewModel() {

    private val searchedCountry: String? = null
    private val regionSelected: String? = null
    var countryDetails: CountryResponse? = null
    var countryList: List<CountryResponse> = ArrayList()
    var countryPediaRepository: CountryPediaRepository? = null
    var favoriteCountries = ArrayList<String>()
    var funFacts: ArrayList<String>? = null

    /**
     * The list of timeZones of the splitTimeZones
     */
    var splitListFirstHalve: String? = null
    var splitListSecondHalve: String? = null

    var formattedLanguages = " - "
        get() {
            field = formatLanguageResponse(countryDetails?.languages)
            return field
        }


    var formattedAltSpelling = " - "
        get() {
            field = formatList(countryDetails?.altSpellings as List<String>, ", ")
            return field
        }

    var countryCallingCode = " - "
        get() {
            val callingCode = countryDetails!!.callingCodes[0]
            field = if (callingCode.isNullOrBlank()) " - " else "+$callingCode"
            return field
        }

    var randomCountryLiveData = MutableLiveData<Country>()

    var timeZoneSplitSeparatorVisibility = false
        /*get() {
            field = countryDetails?.timezones?.size!! > 1
            return field
        }*/

    fun setUpUITimeZones() {
        splitList(countryDetails?.timezones?.filter {
            !it.isNullOrBlank()
        } as List<String>)
    }

    fun callGetCountryDetailsApi(countryName: String): LiveData<ApiResponse<List<CountryResponse>, ErrorResponse>> {
        return countryPediaRepository!!.getCountryDetailsFromApi("french polynesia")
    }

    fun callGetCountryListApi(regionName: String): LiveData<ApiResponse<List<CountryResponse>, ErrorResponse>> {
        return countryPediaRepository!!.getCountryDetailsFromApi(regionName)
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
                    val country = Country("Nigeria", "Abuja", "566")
                    AppUtils.error("unable to get random country. error: ${throwable.message}.\n showing default country: ${country}")

                    randomCountryLiveData.value = country
                }))

    }

    /**
     * @param: A list of strings
     * This method is used to split a list of strings into two equal halves
     * and pass them into two variables
     */
    fun splitList(list: List<String>) {

        val midPoint = Math.floor((list.size / 2).toDouble())
        val firstList = mutableListOf<String>()
        val secondList = mutableListOf<String>()
        val splitList = mutableListOf<String>()

        if (list.size > 1) {
            if (list.size % 2 == 0) {
                for (i in 0 until (midPoint.toInt())) {
                    firstList.add(list[i])
                }

                for (i in (midPoint.toInt()) until list.size) {
                    secondList.add(list[i])
                }
            } else {
                for (i in 0 until (midPoint.toInt() + 1)) {
                    firstList.add(list[i])
                }

                for (i in (midPoint.toInt() + 1) until list.size) {
                    secondList.add(list[i])
                }
            }
        } else {
            firstList.add(list[0])
        }

        splitListFirstHalve = formatList(firstList, "\n")
        splitListSecondHalve = formatList(secondList, "\n")

    }


    /**
     * @param: a list of Strings, a seperator format
     * This method takes a list of strings and formats
     * for each value to appear on a new line in form of a String
     */
    fun formatList(list: List<String>?, separator: String): String {

        val formattedString = StringBuilder()

        if (list?.size == 1) {
            formattedString.append(list[0])
        } else {
            list?.forEach {
                formattedString.append(it)
                formattedString.append(separator)
            }
            if (formattedString.isNotEmpty()) {
                formattedString.delete(formattedString.lastIndex - 1, formattedString.lastIndex)
            }

        }

        return formattedString.toString()
    }


    /**
     * @param: a list of LanguageResponse
     * This method formats the elements of the list and
     * returns a String to be displayed on the UI
     */
    fun formatLanguageResponse(list: List<LanguageResponse>?): String {

        val formattedString = StringBuilder()

        if (list?.size == 1) {
            formattedString.append(list[0].name)
        } else {
            list?.forEach {
                formattedString.append(it.name)
                formattedString.append(", ")
            }
            formattedString.delete(formattedString.lastIndex - 1, formattedString.lastIndex)
        }

        return formattedString.toString()
    }


    /**
     * @param: a list of String and two View Visibilty Constants
     * checks the size of a list and decides if it would need an extra view to display content
     */
    fun checkViewVisibility(list: List<String>) = list.size > 1


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
