package com.example.catbreeds;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FavFrag extends Fragment {

    View view;

    public FavFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_fav, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Toolbar toolbar = view.findViewById(R.id.favoritetoolbar);
        toolbar.setTitle("My Favorites");
        toolbar.inflateMenu(R.menu.favorite);
        toolbar.setNavigationIcon(R.drawable.arrow_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO add arrow back functionality
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }
}