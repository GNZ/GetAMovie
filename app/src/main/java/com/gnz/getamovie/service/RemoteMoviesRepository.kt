package com.gnz.getamovie.service

import com.gnz.getamovie.data.movies.MovieList
import com.gnz.getamovie.data.movies.blankMovieList
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

open class RemoteMoviesRepository @Inject constructor(private val moviesApi: MoviesApi) : MoviesRepository {

    override fun getNowPlayingList(language: String?, page: Int?, region: String?): Single<MovieList> =
            moviesApi.getNowPlayingList(language, page, region)

    override fun searchMovie(query: String, page: Int?): Single<MovieList> = moviesApi.searchMovie(query, page)
}

class RemoteNowPlayingDelegate @Inject constructor(private val repository: RemoteMoviesRepository) : NowPlayingDelegate {

    override fun getPage(page: Int): Single<MovieList> = repository.getNowPlayingList(null, page, null)
}

class RemoteSearchMovieDelegate @Inject constructor(private val repository: RemoteMoviesRepository) : SearchMovieDelegate {

    override var query: String = ""

    override fun getPage(page: Int): Single<MovieList> = if (query.isNotBlank()) {
        repository.searchMovie(query, page)
    } else {
        Single.just(blankMovieList)
    }
}