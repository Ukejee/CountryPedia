package com.example.ukeje.countrypedia.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.ukeje.countrypedia.R
import com.example.ukeje.countrypedia.databinding.FragmentHomeBinding
import com.example.ukeje.countrypedia.viewmodel.SharedFragmentViewModel
import com.example.ukeje.countrypedia.web.helper.ResponseType
import java.util.*


class HomeFragment : BaseFragment() {

    internal var listener: View.OnClickListener? = null
    lateinit var binding: FragmentHomeBinding
    //ViewModel for fragment
    private lateinit var viewModel: SharedFragmentViewModel

    override val fragmentTag: String
        get() = HOME_FRAGMENT

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //initialize view model
        viewModel = ViewModelProviders.of(mainActivity).get(SharedFragmentViewModel::class.java)
        viewModel.countryPediaRepository = Objects.requireNonNull(mainActivity).countryPediaRepository
        binding.viewModel = viewModel

        initView()
    }

    private fun initView() {

        viewModel.setRandomCountry()

        binding.knowMoreField.setOnClickListener {
            val countryDetailsLiveData = viewModel.callGetCountryDetailsApi(viewModel.randomCountryLiveData.value!!.name!!)
            countryDetailsLiveData.observe(viewLifecycleOwner, Observer {
                when {
                    it.responseType == ResponseType.LOADING -> {
                        showProgressDialog()
                    }
                    it.responseType == ResponseType.SUCCESS -> {
                        viewModel.countryDetails = it.successObject!![0]
                        cancelProgressDialog()
                        findNavController().navigate(R.id.action_homeFragment_to_countryDetailsFragment)
                    }
                    it.responseType == ResponseType.ERROR -> {
                        showAlert(it.errorObject?.message ?: "An Error occurred try again")
                    }
                    it.responseType == ResponseType.NETWORK_FAILURE -> {
                        showAlert("Network Unavailable")
                    }
                }
            })
        }

    }
}


/* private void initView() {


     countryRepository = new CountryRepository(getActivity());


     //set default random country
     randomCountryLiveData.setName("Nigeria");
     randomCountryLiveData.setCapital("Abuja");

     binding.countrySearchBox.getText().clear();
     binding.countrySearchBox.setOnClickListener(v ->
             Navigation.findNavController(binding.getRoot()).navigate(R.id.action_homeFragment_to_searchCountryFragment));

     binding.knowMoreField.setOnClickListener(v -> callSearchApi(randomCountryLiveData.getName()));

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

             Country country = countryRepository.getCountryDetailsFromApi(randomNo);
             return country;
         }

         @Override
         protected void onPostExecute(Country country) {
             randomCountryLiveData = country;
             if (randomCountryLiveData != null) {
                 binding.countryTitle.setText(randomCountryLiveData.getName());
                 binding.capitalName.setText(randomCountryLiveData.getCapital());
                 binding.knowMoreField.setText("Know more about " + randomCountryLiveData.getName());
             }
         }
     };

     task.execute(1);
 }

 //Methods to be implemented in the UI are declared here
 public interface OnFragmentInteractionListener {
     // TODO: Update argument type and name
     void onFragmentInteraction(String tag);
 }*/