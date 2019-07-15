package com.example.ukeje.countrypedia.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.example.ukeje.countrypedia.CountryRepository;
import com.example.ukeje.countrypedia.R;
import com.example.ukeje.countrypedia.SharedFragmentViewModel;
import com.example.ukeje.countrypedia.database.Country;
import com.example.ukeje.countrypedia.databinding.FragmentHomeBinding;
import com.example.ukeje.countrypedia.web.helper.ApiResponseListener;
import com.example.ukeje.countrypedia.web.responses.CountryResponse;
import com.example.ukeje.countrypedia.web.responses.ErrorResponse;

import java.util.List;
import java.util.Random;


public class HomeFragment extends BaseFragment {

    public CountryRepository countryRepository;
    public SearchCountryFragment searchCountryFragment;
    View.OnClickListener listener;
    FragmentHomeBinding binding;
    private Country randomCountry;
    private List<Country> dbCountries;
    private Random random = new Random();
    private int ranNum = random.nextInt(246) + 1;
    private int dbSize;
    private BottomNavDrawerDialogFragment navMenu;
    private OnFragmentInteractionListener mListener;
    //ViewModel for fragment
    private SharedFragmentViewModel viewModel;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public String getFragmentTag() {
        return Companion.getHOME_FRAGMENT();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentHomeBinding.inflate(getLayoutInflater(), container, false);

        viewModel = ViewModelProviders.of(getActivity()).get(SharedFragmentViewModel.class);
        initView();
        return binding.getRoot();
    }

    private void initView() {


        countryRepository = new CountryRepository(getActivity());

        binding.countrySearchBox.getText().clear();
        binding.countrySearchBox.setOnClickListener(v ->
                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_homeFragment_to_searchCountryFragment));

        binding.knowMoreField.setOnClickListener(v -> callSearchApi(binding.countryTitle.getText().toString()));

        populateDatabase();

        if (dbSize != 0) {
            ranNum = random.nextInt(dbSize - 1) + 1;
        }
        generateRandomCountry(ranNum);

    }

    public void callSearchApi() {
        viewModel.setSearchedCountry(binding.countrySearchBox.getText().toString());
        viewModel.showProgressDialog(getActivity());
        viewModel.loadCountryDetails(new ApiResponseListener<List<CountryResponse>, ErrorResponse>() {
            @Override
            public void onApiSuccessful(List<CountryResponse> successResponse) {
                if (successResponse.size() > 1) {
                    viewModel.countryDetails = successResponse.get(0);
                } else {
                    viewModel.countryDetails = successResponse.get(0);
                }
                viewModel.cancelProgressDialog();
            }

            @Override
            public void onApiFailed(@Nullable ErrorResponse errorResponse) {
                if (errorResponse != null) {
                    viewModel.showAlert(errorResponse.getMessage(), getActivity());
                } else {
                    viewModel.showAlert("Not Found", getActivity());
                }
                viewModel.cancelProgressDialog();
            }

            @Override
            public void onNetworkFailure() {
                viewModel.showAlert("NETWORK FAILURE", getActivity());
                viewModel.cancelProgressDialog();

            }
        });
    }

    public void callSearchApi(String randomCountryName) {
        viewModel.setSearchedCountry(randomCountryName);
        viewModel.showProgressDialog(getActivity());
        viewModel.loadCountryDetails(new ApiResponseListener<List<CountryResponse>, ErrorResponse>() {
            @Override
            public void onApiSuccessful(List<CountryResponse> successResponse) {
                viewModel.countryDetails = successResponse.get(0);
                viewModel.cancelProgressDialog();

                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_homeFragment_to_countryDetailsFragment);

            }

            @Override
            public void onApiFailed(@Nullable ErrorResponse errorResponse) {
                viewModel.showAlert(errorResponse.getMessage(), getActivity());
                viewModel.cancelProgressDialog();
            }

            @Override
            public void onNetworkFailure() {
                viewModel.showAlert("NETWORK FAILURE", getActivity());
                viewModel.cancelProgressDialog();

            }
        });
    }

    public void populateDatabase() {

        dbSize = 0;
        String test = "test";
        AsyncTask<String, Void, List<Country>> task = new AsyncTask<String, Void, List<Country>>() {

            @Override
            protected List<Country> doInBackground(String... params) {
                return countryRepository.getCountries();
            }

            @Override
            protected void onPostExecute(List<Country> countryList) {
                dbCountries = countryList;
                if (dbCountries.isEmpty()) {
                    String[] countries = {"africa", "asia", "europe", "americas", "oceania"};
                    for (int i = 0; i < countries.length; i++) {
                        getAppUtils().showMessage("PLEASE WAIT APP IS SETTING UP SOME THINGS");
                        viewModel.setRegionSelected(countries[i]);
                        viewModel.loadCountryList(new ApiResponseListener<List<CountryResponse>, ErrorResponse>() {
                            @Override
                            public void onApiSuccessful(List<CountryResponse> successResponse) {
                                for (int i = 0; i < successResponse.size(); i++) {
                                    countryRepository.insertCountry(successResponse.get(i).getName(), successResponse.get(i).getCapital());
                                    dbSize = dbSize + successResponse.size();
                                }
                            }

                            @Override
                            public void onApiFailed(@Nullable ErrorResponse errorResponse) {

                                getAppUtils().showMessage("DATABASE" +
                                        "UPLOAD FALIED " + errorResponse.getMessage());
                            }

                            @Override
                            public void onNetworkFailure() {

                                getAppUtils().showMessage("DATABASE" +
                                        "UPLOAD FAILED" + "NETWORK ERROR");
                                getAppUtils().showMessage("Please check netork connectivity" +
                                        "and restart app");

                            }
                        });

                    }

                }
            }

        };

        task.execute(test);
    }

    public void generateRandomCountry(final int randomNo) {

        AsyncTask<Integer, Void, Country> task = new AsyncTask<Integer, Void, Country>() {
            @Override
            protected Country doInBackground(Integer... ints) {

                Country country = countryRepository.getCountry(randomNo);
                return country;
            }

            @Override
            protected void onPostExecute(Country country) {
                randomCountry = country;
                if (randomCountry != null) {
                    binding.countryTitle.setText(randomCountry.getName());
                    binding.capitalName.setText(randomCountry.getCapital());
                    binding.knowMoreField.setText("Know more about " + randomCountry.getName());
                }
            }
        };

        task.execute(1);
    }

    //Methods to be implemented in the UI are declared here
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String tag);
    }
}