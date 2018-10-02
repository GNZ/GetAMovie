package com.gnz.getamovie.features.moviedetails

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.gnz.getamovie.R
import com.gnz.getamovie.application.extensions.startActivity
import com.gnz.getamovie.features.movielist.pagination.MovieDetails

class MovieDetailsActivity : AppCompatActivity() {

    companion object {

        private const val MOVIE_DETAILS_EXTRA = "EXTRA::MOVIE_DETAILS"

        fun showDetails(context: Context, movieDetails: MovieDetails) = context.startActivity<MovieDetailsActivity> {
            putExtra(MOVIE_DETAILS_EXTRA, movieDetails)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        supportActionBar?.let {
            it.setHomeButtonEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
        }

        intent.getParcelableExtra<MovieDetails>(MOVIE_DETAILS_EXTRA)?.let {
            supportFragmentManager.beginTransaction()
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .add(R.id.movieDetailsContainer, MovieDetailsFragment.newInstance(it), "DETAILS")
                    .commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
            when (item.itemId) {
                android.R.id.home -> {
                    onBackPressed()
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
}
