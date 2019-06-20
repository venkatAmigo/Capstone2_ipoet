
package com.example.ipoet.models;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Authors implements Serializable {

    @SerializedName("authors")
    @Expose
    private List<String> authors = null;

    public void Authors(){}

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

}
