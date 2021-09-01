package com.tarun3k.movieapp.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "movie_table")
public class Movie {

    @SerializedName("adult")
    public Boolean adult;

    @SerializedName("backdrop_path")
    public String backdropPath;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    @SerializedName("id")
    public Integer id;

    @SerializedName("poster_path")
    public String imagePath;

    @SerializedName("title")
    public String title;

    @SerializedName("vote_average")
    public Double rating;

    public String imageUrl;

    @ColumnInfo(name = "is_now_playing")
    public boolean isNowPlaying;
    @ColumnInfo(name = "is_trending")
    public boolean isTrending;
    @ColumnInfo(name = "is_saved")
    public boolean isSaved;
    @ColumnInfo(name = "is_last_seen")
    public boolean isLastSeen;
}
