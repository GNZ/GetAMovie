package com.gnz.getamovie.application.extensions


const val MOVIE_BASE_URL = "https://image.tmdb.org/t/p/w500"

fun String.getFullImageUrl(): String =
        MOVIE_BASE_URL + this