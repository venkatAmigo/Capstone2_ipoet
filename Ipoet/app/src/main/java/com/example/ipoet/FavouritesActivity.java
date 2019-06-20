package com.example.ipoet;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import com.example.ipoet.database.FavViewModel;
import com.example.ipoet.database.FavouritePoems;
import com.example.ipoet.models.Titles;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;


public class FavouritesActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FavViewModel favViewModel;
    MainAdapter mainAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        recyclerView=findViewById(R.id.fav_recycle);
        setTitle("Favourites");
        favViewModel = ViewModelProviders.of(this).get(FavViewModel.class);
        MobileAds.initialize(this, getString(R.string.MOBILE_AD_APP_IDS));
        AdView mAdView =  findViewById(R.id.adsView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);
        loadFavourites();
    }

    public void loadFavourites()
    {
        favViewModel.getAllWords().observe(this, new Observer<List<FavouritePoems>>() {
            @Override
            public void onChanged(@Nullable List<FavouritePoems> fav)
            {

                if(fav!=null) {

                    convertListToModel(fav);
                }

            }
        });
    }
    public  void convertListToModel(List<FavouritePoems> fav)
    {
        Titles titles = new Titles();
        List<String> adds=new ArrayList<>();
        for (int i = 0; i < fav.size(); i++) {
           adds.add(fav.get(i).getTitle());
        }
        titles.setTitles(adds);
        mainAdapter = new MainAdapter(this,titles.getTitles(),"fav");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mainAdapter);

    }

}
