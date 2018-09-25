package com.gnz.getamovie.data.movies


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