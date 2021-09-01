package com.tarun3k.movieapp.repo;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tarun3k.movieapp.model.Movie;
import com.tarun3k.movieapp.model.SearchResponse;
import com.tarun3k.movieapp.model.SingleMovieData;
import com.tarun3k.movieapp.repo.local.MovieDataBase;
import com.tarun3k.movieapp.repo.local.MoviesDao;
import com.tarun3k.movieapp.repo.local.SingleMovieDao;
import com.tarun3k.movieapp.repo.remote.NetworkHelper;
import com.tarun3k.movieapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class MovieRepository {

    NetworkHelper networkHelper;

    List<Movie> trendingMovies = new ArrayList();
    List<Movie> nowPlayingMovies = new ArrayList<>();

   public LiveData<List<Movie>> trendingMoviesLiveData = new MutableLiveData<>();
   public LiveData<List<Movie>> nowPlayingMoviesLiveData = new MutableLiveData<>();
   public LiveData<List<Movie>> searchMoviesLiveData = new MutableLiveData<>();
   public LiveData<Integer> nowPlayingTotalPagesLiveData = new MutableLiveData<>();
   public LiveData<SingleMovieData> currentSelectedMovieLiveData = new MutableLiveData<>();

    private final MoviesDao mMovieDao;
    private final SingleMovieDao singleMovieDao;

    private LiveData<List<Movie>> mSeenMovies= new MutableLiveData<>();
    private LiveData<List<Movie>> savedMovies = new MutableLiveData<>();

    @Inject
    public MovieRepository(NetworkHelper networkHelper, Application application) {
        this.networkHelper = networkHelper;
        MovieDataBase db = MovieDataBase.getDatabase(application);
        mMovieDao = db.movieDao();
        singleMovieDao = db.singleMovieDao();
        mSeenMovies= singleMovieDao.getLastSeen();
        savedMovies = singleMovieDao.getSavedMovies();
        if(!Utils.isNetworkAvailable()) {
            trendingMoviesLiveData = mMovieDao.getTrendingMovies();
            nowPlayingMoviesLiveData = mMovieDao.getNowPlayingMovies();
        }
    }

    public LiveData<List<Movie>> getAllSeenMovies() {
        return mSeenMovies;
    }

    public LiveData<List<Movie>> getSavedMovies() {
        return savedMovies;
    }

    private void insert(Movie movie) {
        MovieDataBase.databaseWriteExecutor.execute(() -> {
            mMovieDao.insert(movie);
        });
    }

    private  void insertSingleMovie(SingleMovieData singleMovieData) {
        MovieDataBase.databaseWriteExecutor.execute(() -> {
            singleMovieDao.insert(singleMovieData);
        });
    }

    public void saveMovieState(Integer movieId, boolean state) {
        MovieDataBase.databaseWriteExecutor.execute(()-> {
            SingleMovieData singleMovieData = singleMovieDao.get(movieId);
            Movie movie = mMovieDao.get(movieId);
            if(movie!=null) {
                movie.isSaved = state;
                mMovieDao.insert(movie);
            }
            singleMovieData.isSaved = state;
            singleMovieDao.insert(singleMovieData);
        });
    }

    public void fetchTrending() {
        if (Utils.isNetworkAvailable()) {
            networkHelper.getService().getTrending("movie", "day").enqueue(trendingCallBack);
        }
    }

    public void fetchNowPlaying(Integer pageNo) {
        if (Utils.isNetworkAvailable()) {
            networkHelper.getService().getNowPlaying(pageNo).enqueue(nowPlayingCallBack);
        }
    }

    public void fetchSingleMovie(Integer movieId) {
        ((MutableLiveData<SingleMovieData> )currentSelectedMovieLiveData).setValue(null);
        if (Utils.isNetworkAvailable()) {
            networkHelper.getService().getMovieById(movieId).enqueue(currentMovieCallback);
        } else {
            MovieDataBase.databaseWriteExecutor.execute(()-> {
                ((MutableLiveData<SingleMovieData>)currentSelectedMovieLiveData ).postValue(singleMovieDao.get(movieId));
            });
         }

    }

    public void searchMovie(String text) {
        networkHelper.getService().searchMovie(text).enqueue(searchMovieCallback);
    }

    Callback<SearchResponse> trendingCallBack = new Callback<SearchResponse>() {
        @Override
        public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
            if(response.body()!=null) {
                addExtraInfo(new ArrayList<>(response.body().getMovieList()), 0);
                trendingMovies.addAll(response.body().getMovieList());
                setTrendingLiveData();
            }
        }

        @Override
        public void onFailure(Call<SearchResponse> call, Throwable t) {
            //do nothing for now
        }
    };

    private void addExtraInfo(List<Movie> movieList, int type) {
        for (Movie movie: movieList) {
            addImageInfo(movie);
            MovieDataBase.databaseWriteExecutor.execute(()-> {
                addStateInfo(movie, type, mMovieDao.get(movie.id));
                insert(movie);
            });
        }
    }

    private void addStateInfo(Movie movie, int type, Movie localMovie) {
        if(localMovie!=null) {
            movie.isLastSeen = localMovie.isLastSeen;
            movie.isNowPlaying = localMovie.isNowPlaying;
            movie.isSaved = localMovie.isSaved;
            movie.isTrending = localMovie.isTrending;
        }
        switch (type) {
            case 0: movie.isTrending = true; break;
            case 1: movie.isNowPlaying = true; break;
            case 2: movie.isLastSeen = true; break;
            case 3: movie.isSaved = true; break;
            default:break;
        }
    }

    private void addImageInfo(Movie movie) {
        movie.imageUrl = "https://image.tmdb.org/t/p/w500"+movie.imagePath;
    }

    private void setTrendingLiveData() {
        ((MutableLiveData<List<Movie>> )trendingMoviesLiveData).setValue(trendingMovies);
    }
    private void setNowPlayingLiveData() {
        ((MutableLiveData<List<Movie>> )nowPlayingMoviesLiveData).setValue(nowPlayingMovies);
    }

    Callback<SearchResponse> nowPlayingCallBack = new Callback<SearchResponse>() {
        @Override
        public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
            if(response.body()!=null) {
                addExtraInfo(response.body().getMovieList(), 1);
                nowPlayingMovies.addAll(response.body().getMovieList());
                if(response.body().getPageNo() == 1) {
                    ((MutableLiveData<Integer> )nowPlayingTotalPagesLiveData).setValue(response.body().getTotalPages());
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
               MovieDataBase.databaseWriteExecutor.execute(()-> {
                   addStateInfo(response.body(), 2, singleMovieDao.get(response.body().id));
                   insertSingleMovie(response.body());
                   ((MutableLiveData<SingleMovieData> )currentSelectedMovieLiveData).postValue(response.body());
               });
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
                addExtraInfo(response.body().getMovieList(), -1);
                ((MutableLiveData<List<Movie>> )searchMoviesLiveData).setValue(response.body().getMovieList());
            }
        }

        @Override
        public void onFailure(Call<SearchResponse> call, Throwable t) {
            // do ntohing
        }
    };
}
