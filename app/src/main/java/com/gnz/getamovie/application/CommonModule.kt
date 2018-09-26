package com.gnz.getamovie.application

import com.gnz.getamovie.BuildConfig
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class CommonModule {

    companion object {
        const val API_KEY_NAMED = "API_KEY_NAMED"
    }

    @Named(API_KEY_NAMED)
    @Singleton
    @Provides
    fun provideApiKey(): String = BuildConfig.MOVIE_DB_API_KEY
}