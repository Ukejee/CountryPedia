package com.example.ukeje.countrypedia.adapters;

import android.app.Activity;
import android.content.Context;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.example.ukeje.countrypedia.R;
import com.example.ukeje.countrypedia.web.responses.CountryResponse;
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * @author .: Ukeje Emeka
 * @email ..: ukejee3@gmail.com
 * @created : 6/6/19
 */
public class CountryListAdapter extends RecyclerView.Adapter<CountryListAdapter.MyHolder>{

    List<CountryResponse> list;
  //  ImageLoader imageLoader;
    View.OnClickListener listener;
    View view;
    Context mContext;


    public CountryListAdapter(List<CountryResponse> list, View.OnClickListener listener, Context mContext) {
        this.list = list;
      //  this.imageLoader=imageLoader;
        this.listener = listener;
        this.mContext = mContext;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_list,parent,false);
        MyHolder myHolder = new MyHolder(view);
        view.setOnClickListener(listener);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        CountryResponse country = list.get(position);
        holder.name.setText(country.getName());
        holder.capital.setText(country.getCapital());
        String image1 = country.getFlag();
        //imageLoader.displayImage(image1, holder.image);
        Context context = holder.image.getContext();
        //holder.image.setImageResource(R.drawable.globe_icon);
        Picasso.with(context).load(image1).into(holder.image);
        holder.itemView.setOnClickListener(listener);
        //holder.image.setImageDrawable(Drawable.createFromPath("res/drawable/globe_icon.xml"));

        Activity activity = (Activity) mContext;
        GlideToVectorYou.justLoadImage(activity, Uri.parse(country.getFlag()),holder.image);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class MyHolder extends RecyclerView.ViewHolder{
        TextView name, capital;
        ImageView image;

        public MyHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.country_list_flag);
            name = itemView.findViewById(R.id.country_list_name);
            capital = itemView.findViewById(R.id.country_list_capital);

        }

    }

}
