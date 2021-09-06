package com.mark.itunes.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mark.itunes.data.EntityItem;

public class Item {

    @SerializedName("trackId")
    @Expose
    private Integer trackId;
    @SerializedName("collectionId")
    @Expose
    private Integer collectionId;
    @SerializedName("artistName")
    @Expose
    private String artistName;
    @SerializedName("trackCensoredName")
    @Expose
    private String trackCensoredName;
    @SerializedName("collectionName")
    @Expose
    private String collectionName;
    @SerializedName("artworkUrl100")
    @Expose
    private String artworkUrl100;

    private boolean isSaved;

    public Integer getTrackId() {
        if (trackId != null)
            return trackId;
        return collectionId;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getTrackCensoredName() {
        if (trackCensoredName != null)
            return trackCensoredName;
        return collectionName;
    }

    public String getArtworkUrl100() {
        return artworkUrl100;
    }

    public EntityItem getEntityItem(byte[] image) {
        return new EntityItem(getTrackId(), getTrackCensoredName(), artistName, image);
    }

    public void setSave(boolean b) {
        isSaved = b;
    }

    public boolean isSaved() {
        return isSaved;
    }
}
