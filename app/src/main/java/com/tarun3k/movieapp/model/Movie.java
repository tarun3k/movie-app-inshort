package com.tarun3k.movieapp.model;

import com.google.gson.annotations.SerializedName;

public class Movie {

    @SerializedName("adult")
    public Boolean adult;

    @SerializedName("backdrop_path")
    public String backdropPath;

    @SerializedName("id")
    public Integer id;

    @SerializedName("poster_path")
    public String imagePath;

    @SerializedName("title")
    public String title;

    @SerializedName("vote_average")
    public Double rating;

    public String imageUrl;

}
