package com.gnz.getamovie.service

import javax.inject.Inject


class RemoteImageProvider @Inject constructor() : ImageProvider {

    override fun getImageUrl(imageId: String): String =
            MOVIE_BASE_URL + imageId

    companion object {
        const val MOVIE_BASE_URL = "https://image.tmdb.org/t/p/w500"
    }
}