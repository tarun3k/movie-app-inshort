package com.tarun3k.movieapp.ui.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LiveData;

import com.tarun3k.movieapp.MainActivity;
import com.tarun3k.movieapp.databinding.FragmentHomeBinding;
import com.tarun3k.movieapp.model.Movie;
import com.tarun3k.movieapp.ui.base.BaseFragment;
import com.tarun3k.movieapp.ui.custom.HomeMovieWidget;
import com.tarun3k.movieapp.ui.custom.PaginationListener;
import com.tarun3k.movieapp.viewmodel.HomeViewModel;

import java.util.List;

import javax.inject.Inject;


public class HomeFragment extends BaseFragment {

    public static String SCREEN_NAME = "HomeFragment";

    private FragmentHomeBinding binding;

    @Inject
    public HomeViewModel viewModel;


    @Override
    public String getScreenName() {
        return SCREEN_NAME;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //viewModel = new ViewModelProvider(mActivity).get(HomeViewModel.class);
        ((MainActivity) requireActivity()).appComponent.inject(this);
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        viewModel.fetchData();
        viewModel.intiAllLiveDatas();
        intiViews();
        attachCallback();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        intiObservers();
        return binding.getRoot();
    }


    private void intiViews() {
        binding.searchItems.disableTitle();
        binding.searchItems.disableMoviesImage();
        binding.searchContainer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //do nothing
            }

            @Override
            public void afterTextChanged(Editable editable) {
                viewModel.searchMovie(editable.toString());
                if(editable.length() ==0) {
                    viewModel.resetSearchMovies();
                }
            }
        });
        addWidgets();
    }

    private void attachCallback() {
        binding.searchItems.setCallback((movieId -> {
            viewModel.onMovieItemClick(movieId);
        }));

    }

    private void enableNowPlayingPagination(Integer totalNumber) {
        int childSize=binding.widgets.getChildCount();
        for(int index=0;index<childSize;index++) {
            View view = binding.widgets.getChildAt(index);
            if(view instanceof HomeMovieWidget) {
                HomeMovieWidget homeMovieWidget = ((HomeMovieWidget)view);
                if("Now Playing".equalsIgnoreCase(homeMovieWidget.getTitle())) {
                    enablePagination(homeMovieWidget, totalNumber);
                }
            }
        }
    }

    private void enablePagination(HomeMovieWidget homeMovieWidget, Integer totalNumber) {
        homeMovieWidget.enablePagination(totalNumber, new PaginationListener() {
            @Override
            public void onNextPage(Integer pageNo) {
                viewModel.fetchNowPlaying(pageNo);
            }

            @Override
            public void onPaginationFinished() {
                //
            }
        });
    }


    private void intiObservers() {
        startObservingWidgets();
        viewModel.observeNowPlayingTotalPagesLiveData().observe(getViewLifecycleOwner(), this::enableNowPlayingPagination);
        viewModel.observeSearchMovies().observe(getViewLifecycleOwner(), movies -> {
            binding.searchItems.updateView("", movies);
        });
    }

    private void startObservingWidgets() {
        int index =0;
        for (Pair<String,LiveData<List<Movie>>> moviePairLivedata: viewModel.getList()) {
            int finalIndex = index;
            moviePairLivedata.second.observe(getViewLifecycleOwner(), list -> {
                View view =binding.widgets.getChildAt(finalIndex);
                if(view instanceof HomeMovieWidget) {
                    ((HomeMovieWidget) view).updateView(moviePairLivedata.first, list);
                }
            });
            index++;
        }
    }

    private void addWidgets() {
        binding.widgets.removeAllViews();
        for (int index =0;index<viewModel.getList().size();index++) {
            HomeMovieWidget homeMovieWidget = new HomeMovieWidget(binding.widgets.getContext());

            ConstraintLayout.LayoutParams layoutParams =new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0,12,0,0);
            homeMovieWidget.setmLayoutParams(layoutParams);

            homeMovieWidget.setCallback((movieId -> {
                viewModel.onMovieItemClick(movieId);
            }));
            binding.widgets.addView(homeMovieWidget);
        }
    }
}
