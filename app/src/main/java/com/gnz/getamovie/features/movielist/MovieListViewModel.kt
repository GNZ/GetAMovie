package com.gnz.getamovie.features.movielist

import android.arch.lifecycle.*
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.gnz.getamovie.data.common.ResourceState
import com.gnz.getamovie.data.movies.MovieItem
import com.gnz.getamovie.features.movielist.pagination.MovieDetails
import com.gnz.getamovie.features.movielist.pagination.MovieListDataSource
import com.gnz.getamovie.features.movielist.pagination.MovieListDataSourceFactory
import com.gnz.getamovie.service.NowPlayingDelegate
import com.gnz.getamovie.service.SearchMovieDelegate
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


class MovieListViewModel @Inject constructor(nowPlayingDelegate: NowPlayingDelegate,
                                             searchMovieDelegate: SearchMovieDelegate) : ViewModel() {

    private val config = PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .setEnablePlaceholders(false)
            .build()

    val movieListLiveData: LiveData<PagedList<MovieItem>> by lazy {
        LivePagedListBuilder<Int, MovieItem>(nowPlayingSourceFactory, config).build()
    }

    val movieSearchLiveData: LiveData<PagedList<MovieItem>> by lazy {
        LivePagedListBuilder<Int, MovieItem>(searchMovieSourceFactory, config).build()
    }

    private val _movieClickLiveData by lazy {
        MutableLiveData<MovieDetails>()
    }

    val movieClickLiveData: LiveData<MovieDetails> = _movieClickLiveData

    private val composite = CompositeDisposable()
    private val nowPlayingSourceFactory: MovieListDataSourceFactory = MovieListDataSourceFactory(nowPlayingDelegate, composite)
    private val searchMovieSourceFactory: MovieListDataSourceFactory = MovieListDataSourceFactory(searchMovieDelegate, composite)
    private val mediatorLiveData: MediatorLiveData<ResourceState> = MediatorLiveData()

    init {
        mediatorLiveData.addSource(getNowPlayingState(), { resourceState ->
            resourceState?.let { mediatorLiveData.value = it }
        })
        mediatorLiveData.addSource(getSearchState(), { resourceState ->
            resourceState?.let { mediatorLiveData.value = it }
        })
    }

    fun initViewModel(clickObservable: Observable<MovieDetails>) {
        composite.addAll(
                clickObservable
                        .subscribe(_movieClickLiveData::postValue)
        )
    }

    fun searchQuery(query: String) {
        val delegate = searchMovieSourceFactory.apiCallDelegate
        if (delegate is SearchMovieDelegate){
            delegate.query = query
            movieSearchLiveData.value?.dataSource?.invalidate()
        }
    }

    private fun getNowPlayingState(): LiveData<ResourceState> =
            Transformations.switchMap<MovieListDataSource, ResourceState>(
                    nowPlayingSourceFactory.movieListDataSourceLiveData
            ) { it.currentMoviesState }

    private fun getSearchState(): LiveData<ResourceState> =
            Transformations.switchMap<MovieListDataSource, ResourceState>(
                    searchMovieSourceFactory.movieListDataSourceLiveData
            ) { it.currentMoviesState }

    fun getState(): LiveData<ResourceState> = mediatorLiveData

    companion object {
        const val PAGE_SIZE = 2
    }
}