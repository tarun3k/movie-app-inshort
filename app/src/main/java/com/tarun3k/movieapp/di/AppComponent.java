package com.tarun3k.movieapp.di;


import android.app.Application;

import com.tarun3k.movieapp.MainActivity;
import com.tarun3k.movieapp.ui.fragments.HomeFragment;
import com.tarun3k.movieapp.ui.fragments.MovieDetailsFragment;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Component
@Singleton
public interface AppComponent {

    void inject(MainActivity activity);

    void inject(HomeFragment homeFragment);

    void inject(MovieDetailsFragment detailsFragment);

    @Component.Factory
    interface Factory {
        AppComponent create(@BindsInstance Application application);
    }
}
