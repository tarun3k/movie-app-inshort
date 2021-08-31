package com.tarun3k.movieapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResponse {
    @SerializedName("page")
    Integer pageNo;

    @SerializedName("results")
    List<Movie> movieList;

    @SerializedName("total_pages")
    Integer totalPages;

    public Integer getPageNo() {
        return pageNo;
    }

    public List<Movie> getMovieList() {
        return movieList;
    }

    public Integer getTotalPages() {
        return totalPages;
    }
}
