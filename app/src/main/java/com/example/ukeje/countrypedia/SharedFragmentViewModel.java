package com.example.ukeje.countrypedia;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;

import com.example.ukeje.countrypedia.web.helper.ApiResponseListener;
import com.example.ukeje.countrypedia.web.responses.CountryResponse;
import com.example.ukeje.countrypedia.web.responses.ErrorResponse;

import java.util.ArrayList;
import java.util.List;

public class SharedFragmentViewModel extends ViewModel {

    private String searchedCountry;
    private String regionSelected;
    public CountryResponse countryDetails;
    public List<CountryResponse> countryList;
    private static Dialog mProgressDialog;
    private CountryRepository countryRepository = new CountryRepository();
    public ArrayList<String> favoriteCountries = new ArrayList<>();
    public ArrayList<String> funFacts;

    public ArrayList<String> getFavoriteCountries(){ return favoriteCountries; }


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
        countryRepository.getCountry(getSearchedCountry(), apiResponseListener);

    }

    public void loadCountryList(ApiResponseListener<List<CountryResponse>, ErrorResponse> apiResponseListener){
        countryRepository.getCountryList(getRegionSelected(), apiResponseListener);
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
    }

}
