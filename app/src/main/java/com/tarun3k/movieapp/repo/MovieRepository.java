package com.tarun3k.movieapp.repo;

import androidx.lifecycle.MutableLiveData;

import com.tarun3k.movieapp.model.Movie;
import com.tarun3k.movieapp.model.SearchResponse;
import com.tarun3k.movieapp.model.SingleMovieData;
import com.tarun3k.movieapp.repo.remote.NetworkHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {

    NetworkHelper networkHelper;

    List<Movie> trendingMovies = new ArrayList();
    List<Movie> nowPlayingMovies = new ArrayList<>();

   public MutableLiveData<List<Movie>> trendingMoviesLiveData = new MutableLiveData<>();
   public MutableLiveData<List<Movie>> nowPlayingMoviesLiveData = new MutableLiveData<>();
   public MutableLiveData<List<Movie>> searchMoviesLiveData = new MutableLiveData<>();
   public MutableLiveData<Integer> nowPlayingTotalPagesLiveData = new MutableLiveData<>();
   public MutableLiveData<SingleMovieData> currentSelectedMovieLiveData = new MutableLiveData<>();

    public MovieRepository(NetworkHelper networkHelper) {
        this.networkHelper = networkHelper;
    }


    public void fetchTrending() {
        networkHelper.getService().getTrending("movie", "day").enqueue(trendingCallBack);
    }

    public void fetchNowPlaying(Integer pageNo) {
        networkHelper.getService().getNowPlaying(pageNo).enqueue(nowPlayingCallBack);
    }

    public void fetchSingleMovie(Integer movieId) {
        currentSelectedMovieLiveData.setValue(null);
        networkHelper.getService().getMovieById(movieId).enqueue(currentMovieCallback);
    }

    public void searchMovie(String text) {
        networkHelper.getService().searchMovie(text).enqueue(searchMovieCallback);
    }

    Callback<SearchResponse> trendingCallBack = new Callback<SearchResponse>() {
        @Override
        public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
            if(response.body()!=null) {
                addImageUrlInfo(response.body().getMovieList());
                trendingMovies.addAll(response.body().getMovieList());
                setTrendingLiveData();
            }
        }

        @Override
        public void onFailure(Call<SearchResponse> call, Throwable t) {
            //do nothing for now
        }
    };

    private void addImageUrlInfo(List<Movie> movieList) {
        for (Movie movie: movieList) {
            addImageInfo(movie);
        }
    }

    private void addImageInfo(Movie movie) {
        movie.imageUrl = "https://image.tmdb.org/t/p/w500"+movie.imagePath;
    }

    private void setTrendingLiveData() {
        trendingMoviesLiveData.setValue(trendingMovies);
    }
    private void setNowPlayingLiveData() {
        nowPlayingMoviesLiveData.setValue(nowPlayingMovies);
    }

    Callback<SearchResponse> nowPlayingCallBack = new Callback<SearchResponse>() {
        @Override
        public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
            if(response.body()!=null) {
                addImageUrlInfo(response.body().getMovieList());
                nowPlayingMovies.addAll(response.body().getMovieList());
                if(response.body().getPageNo() == 1) {
                    nowPlayingTotalPagesLiveData.setValue(response.body().getTotalPages());
                }
                setNowPlayingLiveData();
            }
        }

        @Override
        public void onFailure(Call<SearchResponse> call, Throwable t) {
            //do nothing for now
        }
    };

    Callback<SingleMovieData> currentMovieCallback = new Callback<SingleMovieData>() {
        @Override
        public void onResponse(Call<SingleMovieData> call, Response<SingleMovieData> response) {
            if(response.body()!=null) {
               addImageInfo(response.body());
               currentSelectedMovieLiveData.setValue(response.body());
            }
        }

        @Override
        public void onFailure(Call<SingleMovieData> call, Throwable t) {
            // do ntohing
        }
    };

    Callback<SearchResponse> searchMovieCallback = new Callback<SearchResponse>() {
        @Override
        public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
            if(response.body()!=null) {
                addImageUrlInfo(response.body().getMovieList());
                searchMoviesLiveData.setValue(response.body().getMovieList());
            }
        }

        @Override
        public void onFailure(Call<SearchResponse> call, Throwable t) {
            // do ntohing
        }
    };
}
