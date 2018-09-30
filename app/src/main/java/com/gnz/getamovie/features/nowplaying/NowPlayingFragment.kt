package com.gnz.getamovie.features.nowplaying

import android.arch.paging.PagedList
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.gnz.getamovie.R
import com.gnz.getamovie.application.ViewModelFactory
import com.gnz.getamovie.application.extensions.observe
import com.gnz.getamovie.application.extensions.visibleOrGone
import com.gnz.getamovie.application.extensions.withViewModel
import com.gnz.getamovie.data.common.*
import com.gnz.getamovie.data.movies.MovieItem
import com.gnz.getamovie.features.MainActivity
import com.gnz.getamovie.features.nowplaying.pagination.MovieDetails
import com.gnz.getamovie.features.nowplaying.pagination.NowPlayingAdapter
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_now_playing.*
import org.jetbrains.anko.support.v4.toast
import javax.inject.Inject


class NowPlayingFragment : Fragment() {

    companion object {
        fun newInstance() = NowPlayingFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var movieAdapter: NowPlayingAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_now_playing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initData()
    }

    private fun initViews() {
        movieAdapter = NowPlayingAdapter(Glide.with(this))

        (nowPlayingRecyclerView.layoutManager as GridLayoutManager).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int =
                        if (movieAdapter.isLastItemPosition(position)) this@apply.spanCount
                        else 1
            }
        }
        nowPlayingRecyclerView.adapter = movieAdapter
    }

    private fun initData() {
        withViewModel<NowPlayingViewModel>(viewModelFactory) {
            initViewModel(movieAdapter.observeMovieClick())
            observe(getState(), ::showState)
            observe(movieListLiveData, ::showNowPlayingMovies)
            observe(movieClickLiveData,::showMovieDetails)
        }
    }

    private fun showState(resourceState: ResourceState) = when (resourceState) {
        is Loading -> showLoadingState(true)
        is PopulateState -> showLoadingState(false)
        is EmptyState -> showEmptyState()
        is ErrorState -> showErrorState()
    }

    private fun showNowPlayingMovies(movieList: PagedList<MovieItem>) {
        movieAdapter.submitList(movieList)
    }

    private fun showLoadingState(showLoading: Boolean) {
        progressBar.visibleOrGone(showLoading)
        movieAdapter.shouldShowLoading(showLoading)
        nothingToShowImage.visibleOrGone(false)
    }

    private fun showEmptyState() {
        nothingToShowImage.visibleOrGone(true)
        nowPlayingRecyclerView.visibleOrGone(false)
        progressBar.visibleOrGone(false)
    }

    private fun showErrorState() {
        showEmptyState()
        toast(R.string.error_loading)
    }

    private fun showMovieDetails(movieDetails: MovieDetails){
        (activity as MainActivity).showMovieDetails(movieDetails)
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }
}