package com.gnz.getamovie.features

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
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
                .replace(R.id.containerLayout, NowPlayingFragment.newInstance())
                .commit()
    }

    fun showMovieDetails(movieDetails: MovieDetails){
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .replace(R.id.containerLayout, MovieDetailsFragment.newInstance(movieDetails))
                .addToBackStack("Details")
                .commit()
    }
}
