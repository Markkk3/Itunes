package com.mark.itunes.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ItemsList {

    @SerializedName("results")
    @Expose
    private List<Item> results;

    public List<Item> getResults() {
        return results;
    }

}
