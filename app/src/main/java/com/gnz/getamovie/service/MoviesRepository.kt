package com.gnz.getamovie.service

import com.gnz.getamovie.data.movies.MovieList
import io.reactivex.Single


interface MoviesRepository {

    fun getNowPlayingList(language: String? = null, page: Int? = null, region: String? = null): Single<MovieList>

    fun searchMovie(query: String, page: Int?): Single<MovieList>
}

interface ApiCallDelegate {
    fun getPage(page: Int): Single<MovieList>
}