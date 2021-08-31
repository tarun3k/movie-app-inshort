package com.tarun3k.movieapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.tarun3k.movieapp.repo.MovieRepository;
import com.tarun3k.movieapp.repo.remote.NetworkHelper;
import com.tarun3k.movieapp.ui.base.BaseFragment;
import com.tarun3k.movieapp.ui.fragments.HomeFragment;
import com.tarun3k.movieapp.ui.fragments.MovieDetailsFragment;
import com.tarun3k.movieapp.viewmodel.HomeViewModel;

public class MainActivity extends AppCompatActivity {


    private HomeViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new HomeViewModel(new MovieRepository(new NetworkHelper()));
            }
        }).get(HomeViewModel.class);
        intiObservers();
        setUpHomeFragment();
    }

    private void intiObservers() {
        viewModel.openDetailsFragmentLiveData.observe(this, open -> {
            if(open!=null && open) {
                replaceFragment(new MovieDetailsFragment(), R.id.frame_container, true);
            }
        });
    }

    private void setUpHomeFragment() {
        replaceFragment(new HomeFragment(), R.id.frame_container, false);
    }

    private void replaceFragment(BaseFragment fragment, Integer id, Boolean addToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(id, fragment);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(fragment.getScreenName());
        }
        fragmentTransaction.commitAllowingStateLoss();
    }
    private void addFragment(BaseFragment fragment, Integer id, Boolean addToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(id, fragment);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(fragment.getScreenName());
        }
        fragmentTransaction.commitAllowingStateLoss();
    }
}