package com.gnz.getamovie.service

import com.gnz.getamovie.data.movies.MovieList
import io.reactivex.Single
import javax.inject.Inject

class RemoteMoviesRepository @Inject constructor(private val moviesApi: MoviesApi): MoviesRepository {

    override fun getNowPlayingList(language: String?, page: Int?, region: String?): Single<MovieList> =
            moviesApi.getNowPlayingList(language, page, region)
}