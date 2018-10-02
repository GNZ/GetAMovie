package com.gnz.getamovie.features.movielist.pagination

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import android.arch.paging.PageKeyedDataSource
import com.gnz.getamovie.data.common.*
import com.gnz.getamovie.data.movies.MovieItem
import com.gnz.getamovie.data.movies.BLANK_MOVIE_LIST
import com.gnz.getamovie.service.ApiCallDelegate
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber


open class MovieListDataSourceFactory(
        val apiCallDelegate: ApiCallDelegate,
        private val composite: CompositeDisposable
) : DataSource.Factory<Int, MovieItem>() {

    val movieListDataSourceLiveData = MutableLiveData<MovieListDataSource>()

    override fun create(): DataSource<Int, MovieItem> {
        val movieListDataSource = MovieListDataSource(apiCallDelegate, composite)
        movieListDataSourceLiveData.postValue(movieListDataSource)
        return movieListDataSource
    }
}

class MovieListDataSource(private val apiCallDelegate: ApiCallDelegate,
                          private val composite: CompositeDisposable) : PageKeyedDataSource<Int, MovieItem>() {

    val currentMoviesState = MutableLiveData<ResourceState>()

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, MovieItem>) {
        getMovieList(INITIAL_PAGE) { movieList ->
            callback.onResult(movieList, null, INITIAL_PAGE + 1)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, MovieItem>) {
        getMovieList(params.key) { movieList ->
            callback.onResult(movieList, params.key + 1)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, MovieItem>) {
        val before = if (params.key == INITIAL_PAGE) {
            null
        } else {
            params.key - 1
        }
        getMovieList(params.key) { movieList ->
            callback.onResult(movieList, before)
        }
    }

    private fun getMovieList(page: Int, callback: (List<MovieItem>) -> Unit) {
        currentMoviesState.postValue(Loading)
        composite.add(
                apiCallDelegate.getPage(page).subscribeBy(
                        onSuccess = { movieList ->
                            when {
                                movieList === BLANK_MOVIE_LIST -> currentMoviesState.postValue(EmptyQueryState)
                                movieList.results.isEmpty() -> currentMoviesState.postValue(EmptyState)
                                else -> {
                                    currentMoviesState.postValue(PopulateState)
                                    callback.invoke(movieList.results)
                                }
                            }
                        },
                        onError = { throwable ->
                            Timber.e(throwable, "Error loading the movies playing right now")
                            currentMoviesState.postValue(ErrorState(throwable))
                        }
                )
        )
    }

    companion object {
        private const val INITIAL_PAGE = 1
    }
}