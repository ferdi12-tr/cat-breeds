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

import java.util.List;

public class FavCatAdapter extends RecyclerView.Adapter<FavCatAdapter.ViewHolder> {
    public static List<CatDB> favList;
    FavItemClicked activity;
    CatDAO catDAO;
    Context context; // this context is MainActivity

    public interface FavItemClicked {
        void onFavItemClicked(CatDB catDB);
        void notifyCatList();
        void notifyFavCatList();
    }

    public FavCatAdapter(Context context, List<CatDB> favList) {
        this.favList = favList;
        activity = (FavItemClicked) context;
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
                    activity.onFavItemClicked(favList.get(favList.indexOf((CatDB) view.getTag())));
                }
            });

            ivStar.setOnClickListener(new View.OnClickListener() { // when we press star from fav list we want to delete from db
                @Override
                public void onClick(View view) {
                    CatDB catDB = favList.get(favList.indexOf((CatDB) itemView.getTag()));

                    for (Cat cat : CatAdapter.cats) { // we must refresh the cat list also
                        if (cat.getName().equals(catDB.getName())) {
                            CatAdapter.cats.get(CatAdapter.cats.indexOf(cat)).setFav(false);
                        }
                    }
                    try {
                        catDAO.delete(catDB.getName()); // delete from database
                        favList = catDAO.getAllCatDB(); // get all cats after delete action
                        Toast.makeText(context, "Removed from favorite list", Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    // notify both cat list and fav cat list adapter
                    activity.notifyCatList();
                    activity.notifyFavCatList();
                }
            });
        }
    }

    @NonNull
    @Override
    public FavCatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);

        return new FavCatAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavCatAdapter.ViewHolder holder, int position) {

        holder.itemView.setTag(favList.get(position));
        holder.tvCatName.setText(favList.get(position).getName());

        holder.ivStar.setImageResource(R.drawable.star_full);

        if (favList.get(position).getImgUrl() == null) { // we cannot directly access direct link from search api, so direct link must be checked
            Picasso.get().load("https://cdn2.thecatapi.com/images/" + favList.get(position).getImageID() + ".jpg").into(holder.ivCat);
        } else {
            Picasso.get().load(favList.get(position).getImgUrl()).into(holder.ivCat);
        }
    }

    @Override
    public int getItemCount() {
        return favList.size();
    }

}
