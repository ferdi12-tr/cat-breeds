package com.example.catbreeds;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.catbreeds.room.CatDAO;
import com.example.catbreeds.room.CatDB;
import com.example.catbreeds.room.Connections;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CatAdapter extends RecyclerView.Adapter<CatAdapter.ViewHolder> {

    public static List<Cat> cats;
    CatAdapter.ItemClicked activity;
    CatDAO catDAO;
    Context context; // this context is MainActivity

    public interface ItemClicked {
        void onItemClicked(Cat cat);
        void notifyCatList();
        void notifyFavCatList();
    }

    public CatAdapter(Context context, List<Cat> list) {
        cats = list;
        activity = (ItemClicked) context;
        this.context = context;
        catDAO = Connections.getInstance(context).getDatabase().getCatDAO();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCat;
        TextView tvCatName;
        ImageView ivStar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivCat = itemView.findViewById(R.id.ivCat);
            tvCatName = itemView.findViewById(R.id.tvCatName);
            ivStar = itemView.findViewById(R.id.ivStar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.onItemClicked(cats.get(cats.indexOf((Cat) view.getTag())));
                }
            });

            ivStar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Cat cat = cats.get(cats.indexOf((Cat) itemView.getTag()));
                    CatDB catDB = new CatDB();

                    if (cat.isFav()) { // if already clicked star then another click must delete from db
                        cat.setFav(false);
                        try {
                            catDAO.delete(cat.getName());
                            Toast.makeText(context, "Removed from favorite list", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } else { // otherwise, add to fav list db
                        cat.setFav(true);
                        catDB.setName(cat.getName());
                        catDB.setDescription(cat.getDescription());
                        catDB.setFav(cat.isFav());
                        catDB.setDogFriendly(cat.getDogFriendly());
                        catDB.setImgUrl(cat.getImageClass() == null ? null : cat.getImageClass().getUrl());
                        catDB.setLifeSpan(cat.getLifeSpan());
                        catDB.setOrigin(cat.getOrigin());
                        catDB.setWikiUrl(cat.getWikiUrl());
                        catDB.setImageID(cat.getImageID());
                        try {
                            catDAO.insert(catDB);
                            Toast.makeText(context, "Added to favorite list", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                    notifyItemChanged(cats.indexOf((Cat) itemView.getTag()));
                }
            });
        }
    }

    @NonNull
    @Override
    public CatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CatAdapter.ViewHolder holder, int position) {

        holder.itemView.setTag(cats.get(position));
        holder.tvCatName.setText(cats.get(position).getName());

        if (cats.get(position).getImageClass() == null) { // we cannot get full link, instead we will get relative link
            Picasso.get().load("https://cdn2.thecatapi.com/images/" + cats.get(position).getImageID() + ".jpg").into(holder.ivCat);
        } else {
            Picasso.get().load(cats.get(position).getImageClass().getUrl()).into(holder.ivCat);
        }

        if (cats.get(position).isFav()) {
            holder.ivStar.setImageResource(R.drawable.star_full);
        } else {
            holder.ivStar.setImageResource(R.drawable.star_outline);
        }
    }

    @Override
    public int getItemCount() {
        return cats.size();
    }

}
