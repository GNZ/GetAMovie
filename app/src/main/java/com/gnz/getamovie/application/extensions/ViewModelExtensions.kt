package com.gnz.getamovie.application.extensions

import android.arch.lifecycle.*
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity


inline fun <reified T : ViewModel> FragmentActivity.getViewModel(
        viewModelFactory: ViewModelProvider.Factory
): T = ViewModelProviders.of(this, viewModelFactory)[T::class.java]

inline fun <reified T : ViewModel> Fragment.getViewModel(
        viewModelFactory: ViewModelProvider.Factory
): T = ViewModelProviders.of(this, viewModelFactory)[T::class.java]

inline fun <reified T : ViewModel> FragmentActivity.withViewModel(
        viewModelFactory: ViewModelProvider.Factory,
        body: T.() -> Unit
): T = getViewModel<T>(viewModelFactory).apply(body)

inline fun <reified T : ViewModel> Fragment.withViewModel(
        viewModelFactory: ViewModelProvider.Factory,
        body: T.() -> Unit
): T = getViewModel<T>(viewModelFactory).apply(body)

fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L, body: (T) -> Unit) {
    liveData.observe(this, Observer { it?.apply(body) })
}

fun <T : Any, L : LiveData<T>> LifecycleOwner.stopObserve(liveData: L, body: (T) -> Unit) {
    liveData.removeObserver({ it?.apply(body) })
}
