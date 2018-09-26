package com.gnz.getamovie.features.nowplaying

import android.arch.lifecycle.*
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.gnz.getamovie.data.common.ResourceState
import com.gnz.getamovie.data.movies.MovieItem
import com.gnz.getamovie.features.nowplaying.pagination.MovieListDataSource
import com.gnz.getamovie.features.nowplaying.pagination.MovieListDataSourceFactory
import com.gnz.getamovie.service.MoviesRepository
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


class NowPlayingViewModel @Inject constructor(moviesRepository: MoviesRepository) : ViewModel() {

    val movieListLiveData: LiveData<PagedList<MovieItem>>
    private val composite = CompositeDisposable()
    val dataSourceFactory: MovieListDataSourceFactory

    init {
        dataSourceFactory = MovieListDataSourceFactory(moviesRepository, composite)

        val config = PagedList.Config.Builder()
                .setPageSize(PAGE_SIZE)
                .setEnablePlaceholders(false)
                .build()

        movieListLiveData = LivePagedListBuilder<Int, MovieItem>(dataSourceFactory, config).build()
    }

    fun getState(): LiveData<ResourceState> =
            Transformations.switchMap<MovieListDataSource, ResourceState>(
                    dataSourceFactory.movieListDataSourceLiveData
            ) { it.nowPlayingMoviesState }

    companion object {
        private const val PAGE_SIZE = 2
    }
}