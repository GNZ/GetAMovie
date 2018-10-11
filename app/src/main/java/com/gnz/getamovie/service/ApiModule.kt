package com.gnz.getamovie.service

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class ApiModule {

    companion object {
        const val MOVIES_REMOTE_REPOSITORY = "MOVIES_REMOTE_REPOSITORY"
    }

    @Singleton
    @Provides
    fun provideMoviesApi(retrofit: Retrofit): MoviesApi = retrofit.create(MoviesApi::class.java)

    @Singleton
    @Provides
    fun provideMoviesRemoteRepository(remoteMoviesRepository: RemoteMoviesRepository): MoviesRepository = remoteMoviesRepository

    @Singleton
    @Provides
    fun provideNowPlayingDelegator(remoteNowPlayingDelegate: RemoteNowPlayingDelegate): NowPlayingDelegate = remoteNowPlayingDelegate

    @Singleton
    @Provides
    fun provideSearchMovieDelegate(remoteSearchMovieDelegate: RemoteSearchMovieDelegate): SearchMovieDelegate = remoteSearchMovieDelegate

    @Singleton
    @Provides
    fun provideImageService(remoteImageService: RemoteImageProvider): ImageProvider = remoteImageService
}