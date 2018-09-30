package com.gnz.getamovie.feature.nowplaying

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import android.arch.paging.PagedList
import com.gnz.getamovie.data.common.*
import com.gnz.getamovie.data.movies.MovieItem
import com.gnz.getamovie.data.movies.MovieList
import com.gnz.getamovie.data.movies.emptyMovieList
import com.gnz.getamovie.data.movies.notEmptyMovieList
import com.gnz.getamovie.features.nowplaying.NowPlayingViewModel
import com.gnz.getamovie.features.nowplaying.pagination.MovieDetails
import com.gnz.getamovie.service.MoviesRepository
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations


class NowPlayingViewModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    val repository = mock<MoviesRepository>()

    val stateObserver = mock<Observer<ResourceState>>()

    val pagingListObserver = mock<Observer<PagedList<MovieItem>>>()

    val movieClikedObserver = mock<Observer<MovieDetails>>()

    val movieClickSubject = PublishSubject.create<MovieDetails>()

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
    }

    private fun mockNowPlaying(block: SingleEmitter<MovieList>.() -> Unit) {
        whenever(repository.getNowPlayingList(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(Single.create { e -> e.block() })
    }

    private fun initializeViewModel() = NowPlayingViewModel(repository)
            .apply {
                getState().observeForever(stateObserver)
                movieListLiveData.observeForever(pagingListObserver)
                movieClickLiveData.observeForever(movieClikedObserver)
            }

    @Test
    fun `should show the error state when the repository returns an error`() {
        val errorMessage = "error message"
        val throwable = RuntimeException(errorMessage)
        mockNowPlaying { onError(throwable) }

        initializeViewModel()

        verify(stateObserver).onChanged(Loading)
        verify(stateObserver).onChanged(ErrorState(throwable))
    }

    @Test
    fun `should show the empty state when there is noting to fetch`() {
        mockNowPlaying { onSuccess(emptyMovieList) }

        initializeViewModel()

        verify(stateObserver).onChanged(Loading)
        verify(stateObserver).onChanged(EmptyState)
    }

    @Test
    fun `should show the populate state of movies when the call is successful`() {
        mockNowPlaying { onSuccess(notEmptyMovieList) }

        initializeViewModel()

        verify(stateObserver).onChanged(Loading)
        verify(stateObserver).onChanged(PopulateState)
    }

    @Test
    fun `should show a list of movies when the call is successful`() {
        mockNowPlaying { onSuccess(notEmptyMovieList) }

        var pagedList: PagedList<MovieItem>? = null
        NowPlayingViewModel(repository)
                .apply {
                    getState().observeForever(stateObserver)
                    movieListLiveData.observeForever { pagedList = it }
                }

        pagedList?.loadAround(1)

        verify(stateObserver, times(1 + NowPlayingViewModel.PAGE_SIZE))
                .onChanged(Loading)
        verify(stateObserver, times(1 + NowPlayingViewModel.PAGE_SIZE))
                .onChanged(PopulateState)
    }

    @Test
    fun `should show the second page and load the third successfully`() {
        mockNowPlaying { onSuccess(notEmptyMovieList) }

        var pagedList: PagedList<MovieItem>? = null
        NowPlayingViewModel(repository)
                .apply {
                    getState().observeForever(stateObserver)
                    movieListLiveData.observeForever { pagedList = it }
                }

        mockNowPlaying { onSuccess(notEmptyMovieList) }

        pagedList?.loadAround(1)

        verify(stateObserver, times(3)).onChanged(Loading)
        verify(stateObserver, times(3)).onChanged(PopulateState)
    }

    @Test
    fun `should open the activities details when a movie is clicked`(){
        val movieDetails = MovieDetails("","","")

        mockNowPlaying { onSuccess(emptyMovieList) }
        initializeViewModel().apply {
            initViewModel(movieClickSubject)
        }

        movieClickSubject.onNext(movieDetails)

        verify(movieClikedObserver).onChanged(movieDetails)
    }
}