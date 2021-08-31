package com.tarun3k.movieapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SingleMovieData extends Movie {
    public class Genres{
        @SerializedName("id")
        public Integer id;

        @SerializedName("name")
        public String name;
    }

    @SerializedName("genres")
    public List<Genres> listOfGenres;

    @SerializedName("overview")
    public String overview;
}


