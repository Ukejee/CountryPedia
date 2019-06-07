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

import java.util.List;

public class SharedFragmentViewModel extends ViewModel {

    private String searchedCountry;
    private String regionSelected;
    public CountryResponse countryDetails;
    public List<CountryResponse> countryList;
    private static Dialog mProgressDialog;
    private CountryRepository countryRepository = new CountryRepository();


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

}
