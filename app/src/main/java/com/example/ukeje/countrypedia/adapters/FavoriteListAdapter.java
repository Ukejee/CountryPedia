package com.example.ukeje.countrypedia.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ukeje.countrypedia.R;
import com.example.ukeje.countrypedia.database.Country;
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;

import java.util.List;

/**
 * @author .: Ukeje Emeka
 * @email ..: ukejee3@gmail.com
 * @created : 6/18/19
 */
public class FavoriteListAdapter extends RecyclerView.Adapter<FavoriteListAdapter.MyHolder> {

    List<Country> list;
    View.OnClickListener listener;
    View view;
    Context mContext;

    public FavoriteListAdapter(List<Country> countryList, View.OnClickListener listener, Context mContext){

        list = countryList;
        this.listener =listener;
        this.mContext = mContext;

    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_list,parent,false);
        MyHolder myHolder = new MyHolder(view);
        view.setOnClickListener(listener);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position){

        Country country = list.get(position);
        holder.countryName.setText(country.getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        TextView countryName;

        public MyHolder(View itemView){

            super(itemView);
            countryName = itemView.findViewById(R.id.favorite_country_name);
        }
    }
}
