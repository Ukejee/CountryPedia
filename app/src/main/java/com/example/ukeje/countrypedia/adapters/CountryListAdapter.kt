package com.example.ukeje.countrypedia.adapters

import android.app.Activity
import android.content.Context

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


import androidx.recyclerview.widget.RecyclerView

import com.example.ukeje.countrypedia.databinding.ItemCountryListBinding
import com.example.ukeje.countrypedia.web.responses.CountryResponse
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou

/**
 * @author .: Ukeje Emeka
 * @email ..: ukejee3@gmail.com
 * @created : 6/6/19
 */
class CountryListAdapter(private var list: List<CountryResponse>, private var mContext: Context, private var listener: (view: View) -> Unit) : RecyclerView.Adapter<CountryListAdapter.MyHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val binding = ItemCountryListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        //binding.root.setOnClickListener(listener)
        return MyHolder(binding)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val country: CountryResponse = list[position]
        holder.binding.countryName.text = country.name
        holder.binding.countryCapital.text = country.capital
        holder.binding.countryRegion.text = country.region

//        holder.name.text = country.name
//        holder.capital.text = country.capital
        holder.binding.root.setOnClickListener{ listener(holder.binding.root) }

        val activity = mContext as Activity
        GlideToVectorYou.justLoadImage(activity, Uri.parse(country.flag), holder.binding.countryFlag)
    }

    override fun getItemCount(): Int {
        return list.size
    }


    inner class MyHolder(var binding: ItemCountryListBinding) : RecyclerView.ViewHolder(binding.root) {
//        var name: TextView
//        var capital: TextView
//        var image: ImageView
//
//        init {
//            image = itemView.findViewById(R.id.country_flag)
//            name = itemView.findViewById(R.id.country_name)
//            capital = itemView.findViewById(R.id.country_capital)
//        }

    }

}
