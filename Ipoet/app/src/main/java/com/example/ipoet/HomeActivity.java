package com.example.ipoet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ipoet.models.Authors;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


public class HomeActivity extends AppCompatActivity {
    RecyclerView recycler;
    MainAdapter mainAdapter;
    Authors authors;
    ProgressBar progressBar;
    private  static String category="title";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recycler=findViewById(R.id.recycle_home);
        progressBar=findViewById(R.id.mainProgress);

        if(savedInstanceState!=null)
        {
                authors = (Authors) savedInstanceState.getSerializable(getString(R.string.AUTHORS_KEY));
                mainAdapter=new MainAdapter(this,authors.getAuthors(),category);
                recycler.setAdapter(mainAdapter);
                progressBar.setVisibility(View.INVISIBLE);
                recycler.setLayoutManager(new GridLayoutManager(HomeActivity.this,1));
            Toast.makeText(this, "In saved", Toast.LENGTH_SHORT).show();
        }
        else if(isNetworkAvailable())
        {   recycler.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            new  FetchData().execute(category);
        }
        else{
            new AlertDialog.Builder(this)
                    .setTitle("Info")
                    .setMessage(R.string.ALERT_MSG)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent=new Intent(HomeActivity.this,FavouritesActivity.class);
                            startActivity(intent);
                        }
                    })
                    .show();
        }


    }
    private boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo info=connectivityManager.getActiveNetworkInfo();
        return info!=null&&info.isConnected();
    }
    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        if(menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.authors)
        {
            category=getString(R.string.AUTHOR_CAT);
            recycler.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            new  FetchData().execute(category);
        }
        if(item.getItemId()==R.id.titles)
        {
            category=getString(R.string.TITLE_CAT);
            recycler.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            new  FetchData().execute(category);
        }
        if(item.getItemId()==R.id.favourites)
        {
            category=getString(R.string.FAV_CAT);
            Intent intent=new Intent(this,FavouritesActivity.class);
            startActivity(intent);

        }
        if(item.getItemId()==R.id.feedback)
        {
            Intent intent=new Intent(this,FeedbackActivity.class);
            startActivity(intent);

        }
        if(item.getItemId()==R.id.sign_out) {
            GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            GoogleSignInClient account=GoogleSignIn.getClient(this,gso);
                account.signOut()
                        .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent intent=new Intent(HomeActivity.this,MainActivity.class);
                                startActivity(intent);
                            }
                        });

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(getString(R.string.AUTHORS_KEY),authors);
    }

    public class FetchData extends AsyncTask<String,Void, List<String>>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(!isNetworkAvailable())
            {
                new AlertDialog.Builder(HomeActivity.this)
                        .setTitle(R.string.ALERT_TITLE)
                        .setMessage(R.string.ALERT_MSG)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent=new Intent(HomeActivity.this,FavouritesActivity.class);
                                startActivity(intent);
                            }
                        })
                        .show();
            }
        }

        @Override
        protected List<String> doInBackground(String... strings) {

            String moviesJsonString;
            try {
                URL url = NetworkUtils.buildUrl(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inStream = urlConnection.getInputStream();
                StringBuilder builder = new StringBuilder();
                if (inStream == null) {
                    return null;
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line).append("\n");
                }
                if (builder.length() == 0)
                    return null;
                moviesJsonString = builder.toString();
                return NetworkUtils.parseJson(moviesJsonString,strings[0]);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return  null;
        }
        @Override
        protected void onPostExecute(List<String> data) {
            super.onPostExecute(data);
            if (data != null) {
                authors=new Authors();
                authors.setAuthors(data);
                mainAdapter = new MainAdapter(HomeActivity.this, data, category);
                progressBar.setVisibility(View.INVISIBLE);
                setTitle(category+"s");
                recycler.setVisibility(View.VISIBLE);
                if(recycler!=null) {
                    recycler.setAdapter(mainAdapter);
                    recycler.setLayoutManager(new GridLayoutManager(HomeActivity.this, 1));
                }
            }
            else{
                Log.i(getString(R.string.TAG),getString(R.string.FETCH_ERROR_MSG));
            }
        }

    }
}
