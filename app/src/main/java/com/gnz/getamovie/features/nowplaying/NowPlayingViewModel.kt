package com.gnz.getamovie.features.nowplaying

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PagedList
import com.gnz.getamovie.data.common.Response
import com.gnz.getamovie.data.movies.MovieItem
import com.gnz.getamovie.service.MoviesRepository
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


class NowPlayingViewModel @Inject constructor(app: Application,
                                              private val moviesRepository: MoviesRepository) : AndroidViewModel(app) {

    private val moviesResposeLiveData: MutableLiveData<Response<PagedList<MovieItem>>> = MutableLiveData()
    val composite = CompositeDisposable()

    fun observeNowPlayingMovies(): LiveData<PagedList<MovieItem>> {

    }
}