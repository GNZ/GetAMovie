package com.gnz.getamovie.features.moviedetails


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.gnz.getamovie.R
import com.gnz.getamovie.application.extensions.getFullImageUrl
import com.gnz.getamovie.features.nowplaying.pagination.MovieDetails
import kotlinx.android.synthetic.main.fragment_movie_details.*


class MovieDetailsFragment : Fragment() {

    companion object {
        private val PATH_EXTRA = "PATH_EXTRA"
        private val TITLE_EXTRA = "TITLE_EXTRA"
        private val DETAILS_EXTRA = "DETAILS_EXTRA"
        fun newInstance(movieDetails: MovieDetails): MovieDetailsFragment {
            val bundle = Bundle().apply {
                putString(PATH_EXTRA, movieDetails.posterPath)
                putString(TITLE_EXTRA, movieDetails.title)
                putString(DETAILS_EXTRA, movieDetails.details)
            }
            return MovieDetailsFragment().apply {
                arguments = bundle
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.movies_details)

        arguments?.let {
            if (it.containsKey(PATH_EXTRA)) {
                Glide.with(this).load(it.getString(PATH_EXTRA).getFullImageUrl())
                        .apply(RequestOptions.fitCenterTransform())
                        .apply(RequestOptions().error(R.drawable.default_image))
                        .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                        .into(moviePosterImageView)
            }

            if (it.containsKey(TITLE_EXTRA)) {
                movieTitleTextView.text = it.getString(TITLE_EXTRA)
            }

            if (it.containsKey(DETAILS_EXTRA)) {
                moviewDetailsTextView.text = it.getString(DETAILS_EXTRA)
            }
        }
    }
}
