package com.gnz.getamovie.features.nowplaying

import android.arch.lifecycle.*
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.gnz.getamovie.data.common.ResourceState
import com.gnz.getamovie.data.movies.MovieItem
import com.gnz.getamovie.features.nowplaying.pagination.MovieDetails
import com.gnz.getamovie.features.nowplaying.pagination.MovieListDataSource
import com.gnz.getamovie.features.nowplaying.pagination.MovieListDataSourceFactory
import com.gnz.getamovie.service.MoviesRepository
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject


class NowPlayingViewModel @Inject constructor(moviesRepository: MoviesRepository) : ViewModel() {

    val movieListLiveData: LiveData<PagedList<MovieItem>> by lazy {
        val config = PagedList.Config.Builder()
                .setPageSize(PAGE_SIZE)
                .setEnablePlaceholders(false)
                .build()
        LivePagedListBuilder<Int, MovieItem>(dataSourceFactory, config).build()
    }
    private val _movieClickLiveData by lazy {
        MutableLiveData<MovieDetails>()
    }

    val movieClickLiveData: LiveData<MovieDetails> = _movieClickLiveData

    private val composite = CompositeDisposable()
    private val dataSourceFactory: MovieListDataSourceFactory = MovieListDataSourceFactory(moviesRepository, composite)

    fun initViewModel(clickObservable: Observable<MovieDetails>) {
        composite.add(
                clickObservable
                        .subscribe(_movieClickLiveData::postValue)
        )
    }

    fun getState(): LiveData<ResourceState> =
            Transformations.switchMap<MovieListDataSource, ResourceState>(
                    dataSourceFactory.movieListDataSourceLiveData
            ) { it.nowPlayingMoviesState }

    companion object {
        const val PAGE_SIZE = 2
    }
}