package com.example.ukeje.countrypedia.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.ukeje.countrypedia.R
import com.example.ukeje.countrypedia.activity.MainActivity
import com.example.ukeje.countrypedia.utils.AppUtils

/**
 * @author .: Oriaghan Uyi
 * @email ..: uyioriaghan@gmail.com, uyi.oriaghan@cwg-plc.com
 * @created : 2019-07-08 14:27
 */
abstract class BaseFragment : Fragment() {

    lateinit var appUtils: AppUtils

    abstract val fragmentTag: String

    lateinit var mainActivity: MainActivity

    private lateinit var mProgressDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appUtils = AppUtils(requireActivity())

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainActivity = requireActivity() as MainActivity
        mProgressDialog = Dialog(requireContext())
    }

    companion object {

        var COUNTRY_LIST_FRAGMENT = "country.list.fragment"
        var COUNTRY_DETAILS_FRAGMENT = "country.details.fragment"
        var FAVORITE_LIST_FRAGMENT = "favorite.list.fragment"
        var HOME_FRAGMENT = "home.fragment"
        var SPLASH_FRAGMENT = "splash.fragment"
        var REGION_LIST_FRAGMENT = "region.list.fragment"
        var SEARCH_COUNTRY_FRAGMENT = "search.country.fragment"
        var BOTTOM_NAV_DRAWER_FRAGMENT = "bottom.navigation.drawer.fragment"
    }

    fun showProgressDialog() {
        if (mProgressDialog.isShowing) cancelProgressDialog()

        mProgressDialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        mProgressDialog.setContentView(R.layout.progress_indicator)
        mProgressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mProgressDialog.show()
        mProgressDialog.setCancelable(false)

    }

    fun showMessage(title: String, message: String, context: FragmentActivity) {
        cancelProgressDialog()
        try {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(title)
            builder.setMessage(message)
            builder.setPositiveButton("OK", null)
            builder.show()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    fun showAlert(message: String) {
        cancelProgressDialog()
        try {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Error!")
            builder.setMessage(message)
            builder.setPositiveButton("OK", null)

            builder.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun cancelProgressDialog() {
        if (mProgressDialog.isShowing) {
            mProgressDialog.dismiss()
        }
    }
}