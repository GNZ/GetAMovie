package com.gnz.getamovie.service

import com.gnz.getamovie.data.movies.MovieList
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query


interface MoviesApi {

    @GET("movie/now_playing")
    fun getNowPlayingList(@Query("language") language: String?,
                          @Query("page") page: Int?,
                          @Query("region") region: String?): Single<MovieList>
}