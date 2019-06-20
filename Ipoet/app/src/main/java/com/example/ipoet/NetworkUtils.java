package com.example.ipoet;

import android.net.Uri;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NetworkUtils {
    public static URL buildUrl(String sort_order) throws MalformedURLException {
        final String BASE_URL = "http://poetrydb.org/"+sort_order;
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .build();
        return new URL(builtUri.toString());
    }
    public static List<String> parseJson(String j,String cat)
    {
        final String AUTHORS = "authors";
        final String TITLES="titles";
        JSONObject moviesJson;
        JSONArray resultsArray;
        try {
            moviesJson = new JSONObject(j);
            if(cat.equals("author"))
            resultsArray = moviesJson.getJSONArray(AUTHORS);
            else
                resultsArray = moviesJson.getJSONArray(TITLES);
            List<String> poems = new ArrayList<>();
            for (int i = 0; i < resultsArray.length(); i++) {
                poems.add(resultsArray.get(i).toString());
            }
            return poems;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }
}
