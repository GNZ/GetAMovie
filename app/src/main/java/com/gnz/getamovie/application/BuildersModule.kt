package com.gnz.getamovie.application

import com.gnz.getamovie.application.scopes.FragmentScope
import com.gnz.getamovie.features.moviedetails.MovieDetailsFragment
import com.gnz.getamovie.features.movielist.MovieListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class BuildersModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun bindNowPlayingFragment(): MovieListFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun bindMovieDetailsFragment(): MovieDetailsFragment
}