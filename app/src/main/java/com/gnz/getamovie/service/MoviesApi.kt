package com.gnz.getamovie.service

import retrofit2.http.GET
import retrofit2.http.Query


interface MoviesApi {

    @GET("/movie/now_playing")
    fun getNowPlayingList(@Query("language") language: String?,
                          @Query("page") page: Int?,
                          @Query("region") region: String?)
}