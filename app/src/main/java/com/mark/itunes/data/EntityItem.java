package com.mark.itunes.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "item")
public class EntityItem {
    @PrimaryKey
    @ColumnInfo(name = "trackId")
    private int trackId;

    @ColumnInfo(name = "trackName")
    private String trackName;

    @ColumnInfo(name = "artistName")
    private String artistName;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] image;

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public EntityItem(int trackId, String trackName, String artistName, byte[] image) {
        this.trackId = trackId;
        this.trackName = trackName;
        this.artistName = artistName;
        this.image = image;
    }

    public int getTrackId() {
        return trackId;
    }

    public String getTrackName() {
        return trackName;
    }

    public String getArtistName() {
        return artistName;
    }
}
