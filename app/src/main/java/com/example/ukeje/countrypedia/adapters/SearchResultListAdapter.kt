package com.example.ukeje.countrypedia.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ukeje.countrypedia.R
import com.example.ukeje.countrypedia.web.responses.CountryResponse

/**
 * @author .: Ukeje Emeka
 * @email ..: ukejee3@gmail.com
 * @created : 6/26/19
 */
class SearchResultListAdapter(var list: List<CountryResponse>, private var listener: (countryResponse: CountryResponse) -> Unit) : RecyclerView.Adapter<SearchResultListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

       val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search_result_list, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val (name) = list[position]
        holder.countryName.text = name
    }

    fun refreshList(newList: List<CountryResponse>) {
        list = newList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var countryName: TextView = itemView.findViewById(R.id.result_country_name)

        init {

            itemView.setOnClickListener {
                listener(list[adapterPosition])
            }
        }
    }
}
