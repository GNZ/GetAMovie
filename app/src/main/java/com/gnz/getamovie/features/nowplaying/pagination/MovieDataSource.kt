package com.gnz.getamovie.features.nowplaying.pagination

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import android.arch.paging.PageKeyedDataSource
import com.gnz.getamovie.data.common.*
import com.gnz.getamovie.data.movies.MovieItem
import com.gnz.getamovie.service.MoviesRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber


class MovieListDataSourceFactory(
        private val repository: MoviesRepository,
        private val composite: CompositeDisposable
) : DataSource.Factory<Int, MovieItem>() {

    val movieListDataSourceLiveData = MutableLiveData<MovieListDataSource>()

    override fun create(): DataSource<Int, MovieItem> {
        val movieListDataSource = MovieListDataSource(repository, composite)
        movieListDataSourceLiveData.postValue(movieListDataSource)
        return movieListDataSource
    }
}

class MovieListDataSource(private val repository: MoviesRepository,
                          private val composite: CompositeDisposable) : PageKeyedDataSource<Int, MovieItem>() {

    val nowPlayingMovies = MutableLiveData<ResourceState>()

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, MovieItem>) {
        getNowPlayingMovies(INITIAL_PAGE) { movieList ->
            callback.onResult(movieList, null, INITIAL_PAGE)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, MovieItem>) {
        getNowPlayingMovies(params.key) { movieList ->
            callback.onResult(movieList, params.key + 1)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, MovieItem>) {
        val before = if (params.key == INITIAL_PAGE) {
            null
        } else {
            params.key - 1
        }
        getNowPlayingMovies(params.key) { movieList ->
            callback.onResult(movieList, before)
        }
    }

    private fun getNowPlayingMovies(page: Int, callback: (List<MovieItem>) -> Unit) {
        nowPlayingMovies.postValue(Loading)
        composite.add(
                repository.getNowPlayingList(language = null, page = page, region = null)
                        .subscribeBy(
                                onSuccess = { movieList ->
                                    if (movieList.results.isEmpty()) {
                                        nowPlayingMovies.postValue(EmptyState)
                                    } else {
                                        nowPlayingMovies.postValue(PopulateState)
                                        callback.invoke(movieList.results)
                                    }
                                },
                                onError = { throwable ->
                                    Timber.e(throwable, "Error loading the movies playing right now")
                                    nowPlayingMovies.postValue(ErrorState(throwable))
                                }
                        )
        )
    }

    companion object {
        private const val INITIAL_PAGE = 1
    }
}