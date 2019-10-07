package com.example.ukeje.countrypedia.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ukeje.countrypedia.R

import com.example.ukeje.countrypedia.viewmodel.SharedFragmentViewModel
import com.example.ukeje.countrypedia.adapters.CountryListAdapter
import com.example.ukeje.countrypedia.databinding.FragmentCountryListBinding
import com.example.ukeje.countrypedia.utils.AppUtils
import com.example.ukeje.countrypedia.web.helper.ResponseType


class CountryListFragment : BaseFragment() {


    var mAdapter: CountryListAdapter? = null
    private var layoutManager: RecyclerView.LayoutManager? = null

    lateinit var binding: FragmentCountryListBinding
    lateinit var viewModel: SharedFragmentViewModel

    override val fragmentTag: String
        get() = COUNTRY_LIST_FRAGMENT

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentCountryListBinding.inflate(layoutInflater, container, false)
        binding.root
        viewModel = ViewModelProviders.of(this.activity!!).get(SharedFragmentViewModel::class.java)
        init()
        return binding.root
    }

    fun init() {

        //        binding.regionTitle.setText(viewModel.getRegionSelected());
        layoutManager = LinearLayoutManager(activity)
        binding.countryList.layoutManager = layoutManager

        mAdapter = CountryListAdapter(viewModel.countryList, context!!){onClickCountryList(it)}

        layoutManager = LinearLayoutManager(this.activity)
        binding.countryList.layoutManager = layoutManager
        binding.countryList.adapter = mAdapter

    }

    private fun onClickCountryList(view: View) {

        val countryDetailsLiveData = viewModel.callGetCountryDetailsApi(viewModel.countryList[binding.countryList.getChildLayoutPosition(view)].name)
        countryDetailsLiveData.observe(viewLifecycleOwner, Observer {
            when {
                it.responseType == ResponseType.LOADING -> {
                    showProgressDialog()
                }
                it.responseType == ResponseType.SUCCESS -> {
                    cancelProgressDialog()
                    viewModel.countryDetails = it.successObject!![0]
                    findNavController().navigate(R.id.action_countryListFragment_to_countryDetailsFragment)
                }
                it.responseType == ResponseType.ERROR -> {
                    showAlert("Something went wrong")
                    AppUtils.debug(fragmentTag, it.errorObject.toString())
                }
                it.responseType == ResponseType.NETWORK_FAILURE -> {
                    showAlert("No Internet Connection")
                }
            }
        })
    }


}
