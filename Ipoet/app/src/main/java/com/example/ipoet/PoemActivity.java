package com.example.ipoet;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ipoet.database.FavViewModel;
import com.example.ipoet.database.FavouritePoems;
import com.example.ipoet.models.Poem;
import com.example.ipoet.widget.PoemWidgetService;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;



public class PoemActivity extends AppCompatActivity {
    private static final String BASE_URL ="http://poetrydb.org/" ;
    private String category="";
    Poem[] poems;
    List<String> poemLines;
    TextView poemTitle;
    TextView authorName;
    TextView poem;
    Button favButton;
    private FavViewModel favViewModel;
    boolean flag;
    String pLines;
    String title;
    String pTitle,pAuthor;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poem);
        favViewModel = ViewModelProviders.of(this).get(FavViewModel.class);
        Intent intent=getIntent();
        category=intent.getStringExtra(this.getString(R.string.CATEGORY_KEY));
        position=intent.getIntExtra(getString(R.string.POS_KEY),0);
        title=intent.getStringExtra(this.getString(R.string.TITLE_KEY));
        poemTitle=findViewById(R.id.poem_title);
        authorName=findViewById(R.id.author_name);
        favButton=findViewById(R.id.fav_button);
        poem=findViewById(R.id.poem_text);
        MobileAds.initialize(this, getString(R.string.MOBILE_AD_APP_ID));
        AdView mAdView =  findViewById(R.id.adsView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);
        if(category.equals(getString(R.string.FAV_CAT)))
            loadFromFavourites(position);
        else
            getPoems(category);

    }
    public void getPoems(final String cat)
    {
        StringRequest request = new StringRequest(BASE_URL+cat+"/"+title, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                poems = gson.fromJson(response, Poem[].class);
                pTitle=poems[0].getTitle();
                pAuthor=poems[0].getAuthor();
                poemTitle.setText(pTitle);
                authorName.setText(pAuthor);
                poemLines=poems[0].getLines();
                pLines=listToString(poemLines);
                PoemWidgetService.seeviceCall(PoemActivity.this,pLines);
                poem.setText(pLines);
                updateFavButton();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.i("INFO",getString(R.string.FETCH_ERROR_MSG));
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
    private String listToString(List<String> list)
    {
        String a="";
        for(int i=0;i<list.size();i++)
        {
            a+=list.get(i)+"\n";
        }
        return a;
    }
    public void updateFavButton() {
        FavouritePoems r = favViewModel.isItInDatabase(pTitle);
        if (r != null) {
           favButton.setText(getString(R.string.REMOVE_FAV_TEXT));

            flag = true;
        } else {
           favButton.setText(getString(R.string.ADD_TO_FAV));
            flag = false;
        }
    }
    public void addToFav(View view) {
        if (flag) {
            FavouritePoems favouriteMovies = new FavouritePoems(pTitle,pAuthor,pLines);
            favViewModel.delete(favouriteMovies);
            Toast.makeText(this, getString(R.string.REMOVE_TOAST), Toast.LENGTH_SHORT).show();
            favButton.setText(getString(R.string.ADD_TO_FAV));
            flag = !flag;
        } else {
            FavouritePoems favouriteMovies = new FavouritePoems(pTitle,pAuthor,pLines);
            Toast.makeText(this, getString(R.string.ADD_TOAST), Toast.LENGTH_SHORT).show();
            favViewModel.insert(favouriteMovies);
            favButton.setText(getString(R.string.REMOVE_FAV_TEXT));
            flag = !flag;
        }


    }
    public void loadFromFavourites(final int pos)
    {
        favViewModel.getAllWords().observe(this, new Observer<List<FavouritePoems>>() {
            @Override
            public void onChanged(@Nullable List<FavouritePoems> fav)
            {

                if(fav!=null) {
                    poemTitle.setText(fav.get(pos).getTitle());
                    authorName.setText(fav.get(pos).getAuthor());
                    poem.setText(fav.get(pos).getPoem());
                    pTitle=fav.get(pos).getTitle();
                    updateFavButton();
                    PoemWidgetService.seeviceCall(PoemActivity.this,fav.get(pos).getPoem());
                }

            }
        });
    }
}
