package com.example.ukeje.countrypedia.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ukeje.countrypedia.R
import com.example.ukeje.countrypedia.adapters.SearchResultListAdapter
import com.example.ukeje.countrypedia.databinding.FragmentSearchCountryBinding
import com.example.ukeje.countrypedia.viewmodel.SharedFragmentViewModel
import com.example.ukeje.countrypedia.web.helper.ResponseType


class SearchCountryFragment : BaseFragment() {

    private var linearLayoutManager: RecyclerView.LayoutManager? = null
    private lateinit var searchResultListAdapter: SearchResultListAdapter

    private lateinit var viewModel: SharedFragmentViewModel

    private lateinit var binding: FragmentSearchCountryBinding

    override val fragmentTag: String
        get() = SEARCH_COUNTRY_FRAGMENT

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = FragmentSearchCountryBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProviders.of(activity!!).get(SharedFragmentViewModel::class.java)

        init()
        binding.editText.requestFocus()
        appUtils.openKeyboard()
        return binding.root
    }

    private fun init() {

        linearLayoutManager = LinearLayoutManager(activity)
        binding.listView.layoutManager = linearLayoutManager

        //initialize adapter
        searchResultListAdapter = SearchResultListAdapter(listOf()) {
            viewModel.countryDetails = it
            appUtils.hideKeyboard()
            findNavController().navigate(R.id.action_searchCountryFragment_to_countryDetailsFragment)
        }

        binding.listView.adapter = searchResultListAdapter

        binding.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                //empty
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                callSearchApi(s.toString())
            }

            override fun afterTextChanged(s: Editable) {
                callSearchApi(s.toString())
            }
        })
    }

    private fun callSearchApi(countryName: String) {
        val countryDetailsLiveData = viewModel.callGetCountryDetailsApi(countryName)
        countryDetailsLiveData.observe(viewLifecycleOwner, Observer {
            when {
                it.responseType == ResponseType.LOADING -> {
                    //empty
                }
                it.responseType == ResponseType.SUCCESS -> {
                    binding.listView.visibility = View.VISIBLE
                    binding.errorMessage.visibility = View.INVISIBLE
                    searchResultListAdapter.refreshList(it.successObject!!)

                }
                it.responseType == ResponseType.ERROR -> {
                    binding.listView.visibility = View.GONE
                    binding.errorMessage.visibility = View.VISIBLE
                    binding.errorMessage.text = getString(R.string.search_result_not_found)
                }
                it.responseType == ResponseType.NETWORK_FAILURE -> {
                    binding.listView.visibility = View.GONE
                    binding.errorMessage.visibility = View.VISIBLE
                    binding.errorMessage.text = getString(R.string.no_internet_connection)
                }
            }
        })

    }
}
