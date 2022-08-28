package com.example.catbreeds;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.catbreeds.room.CatDAO;
import com.example.catbreeds.room.CatDB;
import com.example.catbreeds.room.Connections;


public class DetailFrag extends Fragment {

    View view;
    CatDAO catDAO;
    public static Toolbar toolbar;

    public DetailFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        catDAO = Connections.getInstance(getContext()).getDatabase().getCatDAO();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_detail, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        toolbar = view.findViewById(R.id.detailtoolbar);
        toolbar.inflateMenu(R.menu.detail);
        toolbar.setNavigationIcon(R.drawable.arrow_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO add arrow back functionality
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() { // MainActivity.cat is when we clicked in order to get detail
            @Override
            public boolean onMenuItemClick(MenuItem item) { // from here, we also can add detailed cat to favorite db
                CatDB catDB = new CatDB();

                if (MainActivity.cat.isFav()) { // if we already have it, then we can remove from favorite db
                    catDAO.delete((MainActivity.cat.getName()));
                    Toast.makeText(getContext(), "Removed from favorite list", Toast.LENGTH_LONG).show();
                    for (Cat cat : CatAdapter.cats) {
                        if (cat.getName().equals(MainActivity.cat.getName())) {
                            CatAdapter.cats.get(CatAdapter.cats.indexOf(cat)).setFav(false);
                        }
                    }
                } else {
                    catDB.setName(MainActivity.cat.getName());
                    catDB.setDescription(MainActivity.cat.getDescription());
                    catDB.setFav(MainActivity.cat.isFav());
                    catDB.setDogFriendly(MainActivity.cat.getDogFriendly());
                    catDB.setImgUrl(MainActivity.cat.getImageClass() == null ? null : MainActivity.cat.getImageClass().getUrl());
                    catDB.setLifeSpan(MainActivity.cat.getLifeSpan());
                    catDB.setOrigin(MainActivity.cat.getOrigin());
                    catDB.setWikiUrl(MainActivity.cat.getWikiUrl());
                    catDB.setImageID(MainActivity.cat.getImageID());
                    try {
                        catDAO.insert(catDB);
                        Toast.makeText(getContext(), "Added to favorite list", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    for (Cat cat : CatAdapter.cats) { // we must refresh the cat list also
                        if (cat.getName().equals(MainActivity.cat.getName())) {
                            CatAdapter.cats.get(CatAdapter.cats.indexOf(cat)).setFav(true);
                        }
                    }
                }
                return true;
                //TODO notify the CatAdapter to refresh star at main cat list
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }
}