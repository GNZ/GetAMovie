package com.gnz.getamovie.features

import android.app.SearchManager
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.SearchView
import android.view.Menu
import com.gnz.getamovie.R
import com.gnz.getamovie.features.moviedetails.MovieDetailsFragment
import com.gnz.getamovie.features.nowplaying.NowPlayingFragment
import com.gnz.getamovie.features.nowplaying.pagination.MovieDetails

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .replace(R.id.containerLayout, NowPlayingFragment.newInstance(), "MOVIE")
                .commit()
    }

    fun showMovieDetails(movieDetails: MovieDetails) {
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .add(R.id.containerLayout, MovieDetailsFragment.newInstance(movieDetails), "DETAILS")
                .addToBackStack("MOVIE")
                .commit()
    }
}
