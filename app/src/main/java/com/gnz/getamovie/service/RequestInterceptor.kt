package com.gnz.getamovie.service

import com.gnz.getamovie.application.CommonModule
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton


@Singleton
class RequestInterceptor @Inject constructor(@Named(CommonModule.API_KEY_NAMED) private val apiKey: String) : Interceptor {

    companion object {
        private const val API_KEY_PARAMETER = "api_key"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalHttpUrl = originalRequest.url()

        val url = originalHttpUrl.newBuilder()
                .addQueryParameter(API_KEY_PARAMETER, apiKey)
                .build()

        val requestBuilder = originalRequest.newBuilder()
                .url(url)

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}