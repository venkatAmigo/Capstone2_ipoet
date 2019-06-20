
package com.example.ipoet.models;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Titles implements Serializable {

    @SerializedName("titles")
    @Expose
    private List<String> titles = null;

    public void Titles(){

    }
    public List<String> getTitles() {
        return titles;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

}
