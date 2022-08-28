package com.example.catbreeds;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.catbreeds.room.CatDAO;
import com.example.catbreeds.room.CatDB;
import com.example.catbreeds.room.Connections;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements CatAdapter.ItemClicked, FavCatAdapter.FavItemClicked {

    private TheCatApi theCatApi;

    public static Cat cat; // to add favorite from detail fragment
    CatDAO catDAO;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    RecyclerView favRecyclerView;
    RecyclerView.Adapter favAdapter;
    RecyclerView.LayoutManager favLayoutManager;

    EditText etSearch;
    ImageButton imBtnSearch;
    ImageView ivImage, ivStar;
    TextView tvDesc, tvOrigin, tvWiki, tvSpan, tvFriendly;

    FragmentManager manager;
    Fragment detailFrag, listFrag, favFrag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbarMain();

        initComponent();

        initRetrofit();

        getFullResult(); // the full cat list will be displayed when app started

        manager.beginTransaction()
                .hide(detailFrag)
                .hide(favFrag)
                .show(listFrag)
                .commit();


        catDAO = Connections.getInstance(this).getDatabase().getCatDAO();
        imBtnSearch.setOnClickListener(new View.OnClickListener() { // display result according to search text
            @Override
            public void onClick(View view) {
                String text = etSearch.getText().toString().trim();
                if (text.isEmpty()) {
                    getFullResult();
                } else {
                    getBySearch(text);
                }
            }
        });

    }

    private void initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.thecatapi.com/v1/breeds/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        theCatApi = retrofit.create(TheCatApi.class);
    }

    private void initComponent() {
        ivStar = findViewById(R.id.ivStar);
        etSearch = findViewById(R.id.etSearch);
        imBtnSearch = findViewById(R.id.imBtnSearch);
        ivImage = findViewById(R.id.ivImage);
        tvDesc = findViewById(R.id.tvDesc);
        tvOrigin = findViewById(R.id.tvOrigin);
        tvWiki = findViewById(R.id.tvWiki);
        tvSpan = findViewById(R.id.tvSpan);
        tvFriendly = findViewById(R.id.tvFriendly);

        manager = this.getSupportFragmentManager();
        listFrag = manager.findFragmentById(R.id.listFrag);
        detailFrag = manager.findFragmentById(R.id.detailFrag);
        favFrag = manager.findFragmentById(R.id.favFrag);

        recyclerView = findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        favRecyclerView = findViewById(R.id.favList);
        favRecyclerView.setHasFixedSize(true);
        favLayoutManager = new LinearLayoutManager(this);
        favRecyclerView.setLayoutManager(favLayoutManager);
    }

    private void initToolbarMain() {
        Toolbar toolbar = findViewById(R.id.listtoolbar);
        toolbar.setTitle("Cat Breeds");
        toolbar.inflateMenu(R.menu.list);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() { // when favorite toolbar item clicked
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                favAdapter = new FavCatAdapter(MainActivity.this, catDAO.getAllCatDB()); // get all favorite cats and pass to the adapter of favcat
                favRecyclerView.setAdapter(favAdapter);

                manager.beginTransaction()
                        .hide(detailFrag)
                        .hide(listFrag)
                        .show(favFrag)
                        .addToBackStack(null)
                        .commit();
                return true;
            }
        });
    }


    private void getBySearch(String text) { // search by text entered search text input field
        Call<List<Cat>> call = theCatApi.getBySearch(text);
        call.enqueue(new Callback<List<Cat>>() {
            @Override
            public void onResponse(Call<List<Cat>> call, Response<List<Cat>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Error: " + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }

                adapter = new CatAdapter(MainActivity.this, response.body()); // response body consist of list of cats
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Cat>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getFullResult() { // when app initialize, the full result from api will be displayed

        Call<List<Cat>> call = theCatApi.getCats();
        call.enqueue(new Callback<List<Cat>>() {
            @Override
            public void onResponse(Call<List<Cat>> call, Response<List<Cat>> response) {

                if (!response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Error: " + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }
                adapter = new CatAdapter(MainActivity.this, response.body()); // response body consist of list of cats
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Cat>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onItemClicked(Cat cat) {
        this.cat = cat;
        String sourceSting = "<b>%s</b> %s";
        tvDesc.setText(cat.getDescription());
        tvOrigin.setText(Html.fromHtml(String.format(sourceSting, getString(R.string.origin), cat.getOrigin())));
        tvWiki.setText(Html.fromHtml(String.format(sourceSting, getString(R.string.wiki), cat.getWikiUrl())));
        tvSpan.setText(Html.fromHtml(String.format(sourceSting, getString(R.string.life_span), cat.getLifeSpan())));
        tvFriendly.setText(Html.fromHtml(String.format(sourceSting, getString(R.string.dog_friendly), cat.getDogFriendly())));

        if (cat.getImageClass() == null) {
            Picasso.get().load("https://cdn2.thecatapi.com/images/" + cat.getImageID() + ".jpg").into(ivImage);
        } else {
            Picasso.get().load(cat.getImageClass().getUrl()).into(ivImage);
        }

        DetailFrag.toolbar.setTitle(cat.getName()); // set the detail fragment title

        manager.beginTransaction()
                .show(detailFrag)
                .hide(listFrag)
                .hide(favFrag)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onFavItemClicked(CatDB catDB) {
        String sourceSting = "<b>%s</b> %s";
        tvDesc.setText(catDB.getDescription());
        tvOrigin.setText(Html.fromHtml(String.format(sourceSting, getString(R.string.origin), catDB.getOrigin())));
        tvWiki.setText(Html.fromHtml(String.format(sourceSting, getString(R.string.wiki), catDB.getWikiUrl())));
        tvSpan.setText(Html.fromHtml(String.format(sourceSting, getString(R.string.life_span), catDB.getLifeSpan())));
        tvFriendly.setText(Html.fromHtml(String.format(sourceSting, getString(R.string.dog_friendly), catDB.getDogFriendly())));

        if (catDB.getImgUrl() == null) {
            Picasso.get().load("https://cdn2.thecatapi.com/images/" + catDB.getImageID() + ".jpg").into(ivImage);
        } else {
            Picasso.get().load(catDB.getImgUrl()).into(ivImage);
        }

        DetailFrag.toolbar.setTitle(catDB.getName()); // set the detail fragment title

        manager.beginTransaction()
                .show(detailFrag)
                .hide(listFrag)
                .hide(favFrag)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void notifyFavCatList() {
        favAdapter.notifyDataSetChanged();
    }

    @Override
    public void notifyCatList() {
        adapter.notifyDataSetChanged();
    }
}