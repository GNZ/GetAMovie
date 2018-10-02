package com.gnz.getamovie.features

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.gnz.getamovie.R
import com.gnz.getamovie.features.moviedetails.MovieDetailsActivity
import com.gnz.getamovie.features.movielist.MovieListFragment
import com.gnz.getamovie.features.movielist.pagination.MovieDetails

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .replace(R.id.containerLayout, MovieListFragment.newInstance(), MovieListFragment.TAG)
                .commit()
    }

    fun showMovieDetails(movieDetails: MovieDetails) {
        MovieDetailsActivity.showDetails(this, movieDetails)
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentByTag(MovieListFragment.TAG)
        if (fragment != null && (fragment as MovieListFragment).onBackPressed()) {
            super.onBackPressed()
        }
    }
}