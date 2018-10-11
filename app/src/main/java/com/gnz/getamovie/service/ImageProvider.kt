package com.gnz.getamovie.service


interface ImageProvider {

    fun getImageUrl(imageId: String): String
}