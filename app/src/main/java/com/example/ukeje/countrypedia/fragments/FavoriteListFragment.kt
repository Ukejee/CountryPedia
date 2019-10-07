package com.example.ukeje.countrypedia.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ukeje.countrypedia.R
import com.example.ukeje.countrypedia.adapters.FavoriteListRvAdapter
import com.example.ukeje.countrypedia.databinding.FragmentFavoriteListBinding
import com.example.ukeje.countrypedia.utils.AppUtils
import com.example.ukeje.countrypedia.viewmodel.SharedFragmentViewModel
import com.example.ukeje.countrypedia.web.helper.ResponseType
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class FavoriteListFragment : BaseFragment() {

    private var linearLayoutManager: LinearLayoutManager? = null
    private lateinit var  favListRvRvAdapter: FavoriteListRvAdapter
    private lateinit var binding: FragmentFavoriteListBinding
    private lateinit var viewModel: SharedFragmentViewModel

    override val fragmentTag: String
        get() = FAVORITE_LIST_FRAGMENT

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(mainActivity).get(SharedFragmentViewModel::class.java)
        viewModel.countryPediaRepository = mainActivity.countryPediaRepository
        init()
    }

    private fun init() {

        favListRvRvAdapter = FavoriteListRvAdapter(listOf(), requireActivity()){favCountry ->
            AppUtils.debug("clicked: $favCountry")
            val favCountryDetailLiveData = viewModel.callGetCountryDetailsApi(favCountry.name!!)
            favCountryDetailLiveData.observe(viewLifecycleOwner, Observer {
                when(it.responseType){
                    ResponseType.LOADING ->{
                        showProgressDialog()
                    }
                    ResponseType.SUCCESS ->{
                        viewModel.countryDetails = it.successObject!![0]
                        cancelProgressDialog()
                        findNavController().navigate(R.id.action_favoriteListFragment_to_countryDetailsFragment)
                    }
                    ResponseType.ERROR -> {
                        showAlert(it.errorObject?.message ?: "An Error occurred try again")
                    }
                    ResponseType.NETWORK_FAILURE -> {
                        showAlert("Network Unavailable")
                    }
                }
            })

        }

        linearLayoutManager = LinearLayoutManager(activity)
        binding.favoriteList.layoutManager = linearLayoutManager
        binding.favoriteList.adapter = favListRvRvAdapter

        //get all favourite countries
        compositeDisposable.add(viewModel.getFavouriteCountries()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            AppUtils.debug("list of favourite countries ${it.size}")
                                favListRvRvAdapter.refreshList(it)
                        },
                        { error ->
                            AppUtils.error("error in getting favourite countries: $error")
                        }
                ))
    }

    //    public void getFavoriteCountries(){
    //
    ////        countryPediaRepository = new CountryPediaRepository(getActivity());
    //
    //        new AsyncTask<Void, Void, List<Country>>() {
    //            @Override
    //            protected List<Country> doInBackground(Void...voids) {
    //
    //                return countryPediaRepository.getFavoriteCountries(true);
    //            }
    //
    //            @Override
    //            protected void onPostExecute(List<Country> favoriteCountryList) {
    //                favoriteCountriesList = favoriteCountryList;
    //                myAdapter = new FavoriteListAdapter(favoriteCountriesList, listener, getActivity());
    //                binding.favoriteList.setAdapter(myAdapter);
    //
    //            }
    //        }.execute();
    //
    //    }

    //    public void callSearchApi(View view){
    //        viewModel.setSearchedCountry(favoriteCountriesList.get(binding.favoriteList.getChildLayoutPosition(view)).getName());
    //        viewModel.showProgressDialog(getActivity());
    //        viewModel.loadCountryDetails(new ApiResponseListener<List<CountryResponse>, ErrorResponse>() {
    //            @Override
    //            public void onApiSuccessful(List<CountryResponse> successResponse) {
    //                viewModel.countryDetails = successResponse.get(0);
    //                viewModel.cancelProgressDialog();
    ////                onButtonPressed(SEARCH_COUNTRY_FRAGMENT);
    //            }
    //
    //            @Override
    //            public void onApiFailed(@Nullable ErrorResponse errorResponse) {
    //                if(errorResponse != null) {
    //                    viewModel.showAlert(errorResponse.getMessage(), getActivity());
    //                }
    //                else{
    //                    viewModel.showAlert("Not Found", getActivity());
    //                }
    //                viewModel.cancelProgressDialog();
    //            }
    //
    //            @Override
    //            public void onNetworkFailure() {
    //                viewModel.showAlert("NETWORK FAILURE", getActivity());
    //                viewModel.cancelProgressDialog();
    //
    //            }
    //        });
    //    }
}// Required empty public constructor
