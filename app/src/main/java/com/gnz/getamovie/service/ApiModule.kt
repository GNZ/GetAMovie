package com.gnz.getamovie.service

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
class ApiModule {

    companion object {
        const val MOVIES_REMOTE_REPOSITORY = "MOVIES_REMOTE_REPOSITORY"
    }

    @Singleton
    @Provides
    fun provideMoviesApi(retrofit: Retrofit): MoviesApi = retrofit.create(MoviesApi::class.java)

    @Named(MOVIES_REMOTE_REPOSITORY)
    @Singleton
    @Provides
    fun provideMoviesRemoteRepository(remoteMoviesRepository: RemoteMoviesRepository): MoviesRepository = remoteMoviesRepository
}