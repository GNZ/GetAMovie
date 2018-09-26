package com.gnz.getamovie.application


import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class App : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<App> =
            DaggerApplicationComponent.builder()
                    .create(this)
}