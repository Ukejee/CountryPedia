package com.example.ukeje.countrypedia.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.example.ukeje.countrypedia.databinding.ItemRegionListBinding

import java.util.ArrayList

/**
 * @author .: Ukeje Emeka
 * @email ..: ukejee3@gmail.com
 * @created : 6/19/19
 */
class RegionListAdapter(private var regionList: ArrayList<String>, private var listener: (view:View) -> Unit) : RecyclerView.Adapter<RegionListAdapter.MyHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {

        val binding = ItemRegionListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolder(binding)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.binding.itemRegionName.text = regionList[position]
        holder.binding.root.setOnClickListener { listener(holder.binding.root) }
        
    }

    override fun getItemCount(): Int {
        return regionList.size
    }

    inner class MyHolder(var binding: ItemRegionListBinding) : RecyclerView.ViewHolder(binding.root)

}
