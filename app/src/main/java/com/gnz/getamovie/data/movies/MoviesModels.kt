package com.gnz.getamovie.data.movies

import java.util.*


data class MovieList(
        val page: Int,
        val results: List<MovieItem>,
        val dates: Dates,
        val total_pages: Int,
        val total_results: Int
)

data class Dates(
        val maximum: String,
        val minimum: String
)

data class MovieItem(
        val poster_path: String?,
        val adult: Boolean,
        val overview: String,
        val release_date: String,
        val genre_ids: List<Int>,
        val id: Int,
        val original_title: String,
        val original_language: String,
        val title: String,
        val backdrop_path: String?,
        val popularity: Double,
        val vote_count: Int,
        val video: Boolean,
        val vote_average: Double
)

val BLANK_MOVIE_LIST = MovieList(0, listOf(), Dates("",""), 0, 0)

private val someDate = Dates("", "")

val emptyMovieList = MovieList(0, listOf(), someDate, 0, 0)

private fun getRandom() = Random().nextInt(999) + 1

private val movie = MovieItem("",
        false, "",
        "", listOf(),
        1,
        "",
        "",
        "",
        "",
        2.toDouble(),
        1,
        false,
        2.toDouble())

val notEmptyMovieList = MovieList(getRandom(), listOf(movie), someDate, getRandom(), getRandom())