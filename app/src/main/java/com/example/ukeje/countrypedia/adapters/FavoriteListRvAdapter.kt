package com.example.ukeje.countrypedia.adapters

import android.app.Activity
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.example.ukeje.countrypedia.database.Country
import com.example.ukeje.countrypedia.databinding.ItemFavoriteListBinding
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou

/**
 * @author .: Ukeje Emeka
 * @email ..: ukejee3@gmail.com
 * @created : 6/18/19
 */
class FavoriteListRvAdapter(private var countyList: List<Country>?, private val activity: Activity, private val listener: (Country) -> Unit) : RecyclerView.Adapter<FavoriteListRvAdapter.FavoriteListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteListViewHolder {

        val binding = ItemFavoriteListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteListViewHolder, position: Int) {
        val country = countyList!![position]

        holder.binding.country = country

        holder.binding.parent.setOnClickListener { view ->
            listener(country)
        }

        GlideToVectorYou.justLoadImage(activity, Uri.parse(country.flagImageUrl), holder.binding.countryFlag)

    }

    fun refreshList(newList: List<Country>) {
        countyList = newList
        notifyDataSetChanged()

        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return countyList!!.size
    }


    inner class FavoriteListViewHolder(var binding: ItemFavoriteListBinding) : RecyclerView.ViewHolder(binding.root)

}