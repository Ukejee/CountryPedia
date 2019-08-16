package com.example.ukeje.countrypedia.adapters;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ukeje.countrypedia.database.Country;
import com.example.ukeje.countrypedia.databinding.ItemFavoriteListBinding;
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;

import java.util.List;

/**
 * @author .: Ukeje Emeka
 * @email ..: ukejee3@gmail.com
 * @created : 6/18/19
 */
public class FavoriteListRvAdapter extends RecyclerView.Adapter<FavoriteListRvAdapter.FavoriteListViewHolder> {

    private List<Country> countyList;
    private FavListClickListener listener;
    private Activity activity;

    public FavoriteListRvAdapter(List<Country> countryList, Activity activity, FavListClickListener listener) {

        countyList = countryList;
        this.listener = listener;
        this.activity = activity;

    }

    @NonNull
    @Override
    public FavoriteListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemFavoriteListBinding binding = ItemFavoriteListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FavoriteListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteListViewHolder holder, int position) {
        Country country = countyList.get(position);

        holder.binding.setCountry(country);

        holder.binding.parent.setOnClickListener(view -> {
            if(listener != null)
                listener.onItemClick(country);
        });

        GlideToVectorYou.justLoadImage(activity, Uri.parse(country.getFlagImageUrl()), holder.binding.countryFlag);

    }

    public void refreshList(List<Country> newList){
        countyList = newList;
        notifyDataSetChanged();

       notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return countyList.size();
    }


    class FavoriteListViewHolder extends RecyclerView.ViewHolder {
        ItemFavoriteListBinding binding;

        FavoriteListViewHolder(ItemFavoriteListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

   public interface FavListClickListener {
        void onItemClick(Country country);
    }
}