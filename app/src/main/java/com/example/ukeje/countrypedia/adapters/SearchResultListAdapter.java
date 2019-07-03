package com.example.ukeje.countrypedia.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ukeje.countrypedia.R;
import com.example.ukeje.countrypedia.web.responses.CountryResponse;

import java.util.List;

/**
 * @author .: Ukeje Emeka
 * @email ..: ukejee3@gmail.com
 * @created : 6/26/19
 */
public class SearchResultListAdapter extends RecyclerView.Adapter<SearchResultListAdapter.MyViewHolder> {

    Context mContext;
    List<CountryResponse> list;
    View view;
    OnClickListener listener;

    public SearchResultListAdapter(Context context, List<CountryResponse> list, OnClickListener listener){

        this.mContext = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result_list, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        CountryResponse country = list.get(position);
        holder.countryName.setText(country.getName());
        holder.itemView.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView countryName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            countryName = itemView.findViewById(R.id.result_country_name);
        }
    }
}
