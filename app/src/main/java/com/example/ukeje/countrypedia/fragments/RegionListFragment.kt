package com.example.ukeje.countrypedia.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer

import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ukeje.countrypedia.R

import com.example.ukeje.countrypedia.adapters.RegionListAdapter
import com.example.ukeje.countrypedia.databinding.FragmentRegionListBinding
import com.example.ukeje.countrypedia.utils.AppUtils
import com.example.ukeje.countrypedia.viewmodel.SharedFragmentViewModel
import com.example.ukeje.countrypedia.web.helper.ResponseType
import com.google.android.material.snackbar.Snackbar

import java.util.ArrayList
import java.util.Arrays
import java.util.Objects


class RegionListFragment : BaseFragment() {

    lateinit var viewModel: SharedFragmentViewModel
    lateinit var binding: FragmentRegionListBinding
    internal var navMenu: BottomNavDrawerDialogFragment? = null
    var linearLayoutManager: LinearLayoutManager? = null
    lateinit var regionListAdapter: RegionListAdapter
    var regionList = ArrayList(Arrays.asList("Africa", "Americas", "Asia", "Europe", "Oceania"))
    var listener: View.OnClickListener = View.OnClickListener {
        callListApi(regionList[binding.regionList.getChildLayoutPosition(it)].toLowerCase())
    }

    override val fragmentTag: String
        get() = REGION_LIST_FRAGMENT

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegionListBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProviders.of(Objects.requireNonNull<FragmentActivity>(activity)).get(SharedFragmentViewModel::class.java)
        init()

        return binding.root
    }

    fun init() {

        linearLayoutManager = LinearLayoutManager(activity)
        regionListAdapter = RegionListAdapter(regionList, {view -> callListApi(regionList[binding.regionList.getChildLayoutPosition(view)].toLowerCase())})
        binding.regionList.layoutManager = linearLayoutManager
        binding.regionList.adapter = RegionListAdapter(regionList){view ->  callListApi(regionList[binding.regionList.getChildLayoutPosition(view)].toLowerCase())}

    }

    private fun callListApi(region: String){
        val countryListLiveData = viewModel.callGetCountryListByRegionApi(region)

        countryListLiveData.observe(viewLifecycleOwner, Observer {
            when{
                it.responseType == ResponseType.LOADING -> {
                    //empty
                }
                it.responseType == ResponseType.SUCCESS -> {
                    viewModel.countryList = it.successObject!!
                    findNavController().navigate(R.id.action_regionListFragment_to_countryListFragment)
                }
                it.responseType == ResponseType.ERROR ->{
                    AppUtils.error(it.errorObject.toString())
                }
                it.responseType == ResponseType.NETWORK_FAILURE ->{
                    showAlert("Network Failure")
                }
            }
        })
    }

    //    public void onClickRegion(View view) {
    //        callSearchApi(regionList.get(binding.regionList.getChildLayoutPosition(view)).toLowerCase());
    //        viewModel.setRegionSelected(regionList.get(binding.regionList.getChildLayoutPosition(view)));
    //    }

    //    public void callSearchApi(String region) {
    //        viewModel.setRegionSelected(region);
    //        viewModel.showProgressDialog(getActivity());
    //        viewModel.loadCountryList(new ApiResponseListener<List<CountryResponse>, ErrorResponse>() {
    //            @Override
    //            public void onApiSuccessful(List<CountryResponse> successResponse) {
    //                viewModel.countryList = successResponse;
    //                viewModel.cancelProgressDialog();
    ////                onButtonPressed(COUNTRY_LIST_FRAGMENT);
    //            }
    //
    //            @Override
    //            public void onApiFailed(@Nullable ErrorResponse errorResponse) {
    //                viewModel.showAlert(errorResponse.getMessage(), getActivity());
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
