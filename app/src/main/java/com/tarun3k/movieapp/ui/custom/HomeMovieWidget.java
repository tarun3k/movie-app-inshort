package com.tarun3k.movieapp.ui.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tarun3k.movieapp.databinding.HomeMovieWidgetBinding;
import com.tarun3k.movieapp.model.Movie;

import java.util.List;

public class  HomeMovieWidget extends ConstraintLayout {

    private HomeMovieWidgetBinding binding;
    private boolean isLoading;
    private int mNextPageIndex = 0;
    private boolean isPaginationEnabled;
    private PaginationListener paginationListener;
    private Integer totalPages;
    private MoviesAdapter mAdapter;

    public void setCallback(MoviesAdapter.Callback callback) {
        mAdapter.callback = callback;
    }

    public HomeMovieWidget(@NonNull Context context) {
        super(context);
        initBinding();
    }

    private void initBinding() {
        binding = HomeMovieWidgetBinding.inflate(LayoutInflater.from(getContext()), this, true);
       // binding.getRoot().setPadding(12,12,0,12);
        mAdapter = new MoviesAdapter();
        binding.moviesRv.setAdapter(mAdapter);
        binding.moviesRv.setLayoutManager(new LinearLayoutManager(binding.moviesRv.getContext(), RecyclerView.HORIZONTAL, false));
    }

    public void setmLayoutParams(ViewGroup.LayoutParams layoutParams) {
        binding.getRoot().setLayoutParams(layoutParams);
    }

    public HomeMovieWidget(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initBinding();
    }

    public HomeMovieWidget(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initBinding();
    }

    public HomeMovieWidget(@NonNull Context context,  AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initBinding();
    }

    public void updateView(String title, List<Movie> movies) {
        isLoading= false;
        stopLoading();
        binding.title.setText(title);
        updateAdapter(movies);
    }

    private void stopLoading() {
        binding.progressHorizontal.setIndeterminate(false);
        binding.progressHorizontal.setVisibility(GONE);
    }

    private void startLoading() {
        binding.progressHorizontal.setIndeterminate(true);
        binding.progressHorizontal.setVisibility(VISIBLE);
    }

    private void updateAdapter(List<Movie> movies) {
        mAdapter.updateList(movies);
    }

    public void enablePagination(Integer totalPages, PaginationListener paginationListener) {
        if(isPaginationEnabled) return;
        this.totalPages = totalPages;
        isPaginationEnabled = true;
        mNextPageIndex = 1;
        this.paginationListener = paginationListener;
        addPagination();
    }

    public void addPagination() {
        if (!isPaginationEnabled)
            return;
        RecyclerView.LayoutManager mLayoutManager= binding.moviesRv.getLayoutManager();
        binding.moviesRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!isPaginationEnabled)
                    return;
                if ( !isLoading) { //check for scroll down
                    Integer visibleItemCount = mLayoutManager.getChildCount();
                    Integer totalItemCount = mLayoutManager.getItemCount();
                    Integer pastVisibleItems = Math.max(0, binding.moviesRv.getChildAdapterPosition(binding.moviesRv.getChildAt(0)));
                     if (!isLoading && (visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            isLoading = true;
                            startLoading();
                            fetchNextPage();
                      }
                }
            }
        });
    }
    private void fetchNextPage() {
        ++mNextPageIndex;
        if(paginationListener !=null) {
            if(mNextPageIndex<=totalPages) {
                paginationListener.onNextPage(mNextPageIndex);
            } else {
                isPaginationEnabled = false;
            }
        }
    }
    public String getTitle() {
        return binding.title.getText().toString();
    }

    public void disableTitle() {
        binding.title.setVisibility(GONE);
    }

    public void disableMoviesImage() {
        mAdapter.imageEnable = false;
    }
}
