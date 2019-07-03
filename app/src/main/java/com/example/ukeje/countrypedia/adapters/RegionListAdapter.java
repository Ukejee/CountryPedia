package com.example.ukeje.countrypedia.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ukeje.countrypedia.R;

import java.util.ArrayList;

/**
 * @author .: Ukeje Emeka
 * @email ..: ukejee3@gmail.com
 * @created : 6/19/19
 */
public class RegionListAdapter extends RecyclerView.Adapter<RegionListAdapter.MyHolder> {

    Context mContext;
    ArrayList<String> regionList;
    View.OnClickListener listener;
    View view;

    public RegionListAdapter(Context mContext, ArrayList<String> regionList, View.OnClickListener listener){
        this.mContext = mContext;
        this.regionList = regionList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_region_list, parent,false);
        MyHolder myHolder = new MyHolder(view);
        view.setOnClickListener(listener);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        holder.regionName.setText(regionList.get(position));

    }

    @Override
    public int getItemCount() {
        return regionList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        TextView regionName;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            regionName = itemView.findViewById(R.id.item_region_name);
        }
    }
}
