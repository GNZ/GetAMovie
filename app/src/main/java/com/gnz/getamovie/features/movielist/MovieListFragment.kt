package com.gnz.getamovie.features.movielist

import android.app.SearchManager
import android.arch.paging.PagedList
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.*
import com.bumptech.glide.Glide
import com.gnz.getamovie.R
import com.gnz.getamovie.application.ViewModelFactory
import com.gnz.getamovie.application.extensions.observe
import com.gnz.getamovie.application.extensions.stopObserve
import com.gnz.getamovie.application.extensions.visibleOrGone
import com.gnz.getamovie.application.extensions.withViewModel
import com.gnz.getamovie.data.common.*
import com.gnz.getamovie.data.movies.MovieItem
import com.gnz.getamovie.features.MainActivity
import com.gnz.getamovie.features.movielist.pagination.MovieDetails
import com.gnz.getamovie.features.movielist.pagination.MovieListAdapter
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_movie_list.*
import org.jetbrains.anko.support.v4.toast
import javax.inject.Inject


class MovieListFragment : Fragment() {

    companion object {
        const val TAG = "MOVIE_LIST_FRAGMENT"
        fun newInstance() = MovieListFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var nowPlayingAdapter: MovieListAdapter

    private lateinit var searchMovieAdapter: MovieListAdapter

    private lateinit var viewModel: MovieListViewModel

    private var searchView: SearchView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_movie_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        initViews()
        initData()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) setTitle()
    }

    private fun initViews() {
        setTitle()
        // Now playing recyclerview
        nowPlayingAdapter = MovieListAdapter(Glide.with(this))
        initRecyclerView(nowPlayingRecyclerView, nowPlayingAdapter)

        // Now playing recyclerview
        searchMovieAdapter = MovieListAdapter(Glide.with(this))
        initRecyclerView(searchRecyclerView, searchMovieAdapter)
    }

    private fun initRecyclerView(recyclerView: RecyclerView, theAdapter: MovieListAdapter) {

        (recyclerView.layoutManager as GridLayoutManager).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int =
                        if (theAdapter.isLastItemPosition(position)) this@apply.spanCount
                        else 1
            }
        }
        recyclerView.adapter = theAdapter
    }

    private fun setTitle() {
        getSupportActionBar()?.let {
            it.setTitle(R.string.movies)
            it.setDisplayShowTitleEnabled(true)
        }
    }

    private fun initData() {
        viewModel = withViewModel(viewModelFactory) {
            val movieClick = nowPlayingAdapter.observeMovieClick()
                    .mergeWith(searchMovieAdapter.observeMovieClick())
                    .doOnNext { clearFocus() }
            initViewModel(movieClick)
            observe(getState(), ::showState)
            observe(movieListLiveData, ::showNowPlayingMovies)
            observe(movieClickLiveData, ::showMovieDetails)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
            when (item.itemId) {
                android.R.id.home -> {
                    searchView?.isIconified = true
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        val searchManager = (activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager)

        searchView = (menu.findItem(R.id.search_movie).actionView as SearchView).apply {
            setSearchableInfo(
                    searchManager.getSearchableInfo(activity?.componentName)
            )
        }

        setupSearch()
    }

    private fun setupSearch() {
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.searchQuery(newText)
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean = false
        })

        searchView?.setOnQueryTextFocusChangeListener({ _, focus ->
            when (focus) {
                true -> searchClicked()
            }
        })

        searchView?.setOnCloseListener {
            searchCanceled()
            false
        }
    }

    private fun clearFocus() {
        searchView?.clearFocus()
    }

    private fun getSupportActionBar() = (activity as AppCompatActivity).supportActionBar

    private fun searchClicked() {
        manageActionBar(false)
        observe(viewModel.movieSearchLiveData, ::showSearchResult)
        showSearch(true)
    }

    private fun searchCanceled() {
        manageActionBar(true)
        showSearch(false)
        stopObserve(viewModel.movieSearchLiveData, {})
        nothingToShowImage.visibleOrGone(false)
        nowPlayingRecyclerView.visibleOrGone(true)
    }

    private fun manageActionBar(shoulShowTitle: Boolean){
        getSupportActionBar()?.let {
            it.setDisplayShowTitleEnabled(shoulShowTitle)
            it.setHomeButtonEnabled(!shoulShowTitle)
            it.setDisplayHomeAsUpEnabled(!shoulShowTitle)
        }
        setTitle()
    }

    private fun showSearch(show: Boolean) {
        nowPlayingRecyclerView.visibleOrGone(!show)
        searchRecyclerView.visibleOrGone(show)
        nothingToShowImage.visibleOrGone(false)
    }

    private fun showState(resourceState: ResourceState) = when (resourceState) {
        is Loading -> showLoadingState(true)
        is PopulateState -> showLoadingState(false)
        is EmptyState -> showEmptyState()
        is EmptyQueryState -> showEmptyQueryState()
        is ErrorState -> showErrorState()
    }

    private fun showNowPlayingMovies(movieList: PagedList<MovieItem>) {
        nowPlayingAdapter.submitList(movieList)
    }

    private fun showSearchResult(movieList: PagedList<MovieItem>) {
        searchMovieAdapter.submitList(movieList)
    }

    private fun showLoadingState(showLoading: Boolean) {
        progressBar.visibleOrGone(showLoading)
        nowPlayingAdapter.shouldShowLoading(showLoading)
        nothingToShowImage.visibleOrGone(false)
    }

    private fun showEmptyState() {
        nothingToShowImage.visibleOrGone(true)
        nowPlayingRecyclerView.visibleOrGone(false)
        progressBar.visibleOrGone(false)
    }

    private fun showEmptyQueryState() {
        nothingToShowImage.visibleOrGone(false)
        nowPlayingRecyclerView.visibleOrGone(false)
        progressBar.visibleOrGone(false)
    }

    private fun showErrorState() {
        showEmptyState()
        toast(R.string.error_loading)
    }

    private fun showMovieDetails(movieDetails: MovieDetails) {
        (activity as MainActivity).showMovieDetails(movieDetails)
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    fun onBackPressed(): Boolean = if (searchView != null && !searchView!!.isIconified) {
        searchCanceled()
        searchView?.isIconified = true
        false
    } else true
}