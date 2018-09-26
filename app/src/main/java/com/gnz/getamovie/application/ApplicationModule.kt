package com.gnz.getamovie.application

import android.app.Application
import android.content.Context
import com.gnz.getamovie.BuildConfig
import com.gnz.getamovie.application.scopes.AppContext
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Named
import javax.inject.Singleton


@Module(includes = [(AndroidSupportInjectionModule::class), (AndroidInjectionModule::class)])
abstract class ApplicationModule {

    @Singleton
    @Binds
    @AppContext
    abstract fun provideApplicationContext(application: Application): Context
}