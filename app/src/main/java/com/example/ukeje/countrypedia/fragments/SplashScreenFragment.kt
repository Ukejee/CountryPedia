package com.example.ukeje.countrypedia.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.ukeje.countrypedia.R
import com.example.ukeje.countrypedia.databinding.FragmentSplashScreenBinding
import com.example.ukeje.countrypedia.viewmodel.SharedFragmentViewModel
import com.example.ukeje.countrypedia.web.helper.ResponseType
import com.google.android.material.snackbar.Snackbar
import java.util.*

/**
 * @author .: Oriaghan Uyi
 * @email ..: uyioriaghan@gmail.com, uyi.oriaghan@cwg-plc.com
 * @created : 2019-07-23 11:25
 */
class SplashScreenFragment : BaseFragment() {

    private lateinit var binding: FragmentSplashScreenBinding
    private lateinit var viewModel: SharedFragmentViewModel

    override val fragmentTag: String
        get() = SPLASH_FRAGMENT


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        binding = FragmentSplashScreenBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //initialize view model
        viewModel = ViewModelProviders.of(mainActivity).get(SharedFragmentViewModel::class.java)
        viewModel.countryPediaRepository = Objects.requireNonNull(mainActivity).countryPediaRepository

        setup()
    }

    private fun setup() {

        viewModel.initializingCountryListIntoDb().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when {
                it.responseType == ResponseType.LOADING -> {
                    binding.loadingGroup.visibility = View.VISIBLE
                    binding.loadingText.text = getString(R.string.splash_screen_loading_text)
                }
                it.responseType == ResponseType.SUCCESS -> {
                    binding.loadingGroup.visibility = View.GONE
                    findNavController().navigate(R.id.action_splashScreenFragment_to_homeFragment)
                }
                it.responseType == ResponseType.ERROR || it.responseType == ResponseType.NETWORK_FAILURE -> {
                    binding.loadingGroup.visibility = View.GONE
                    binding.loadingGroup.visibility = View.GONE

                    val errorMessage = if (it.responseType == ResponseType.ERROR) it.errorObject!!.message else "Network Unavailable"
                    val snackBar = Snackbar.make(binding.parent, errorMessage, Snackbar.LENGTH_INDEFINITE)
                    snackBar.setAction("RETRY") {
                        setup()
                    }
                    snackBar.show()
                }
            }

        })

    }
}