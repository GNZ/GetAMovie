package com.gnz.getamovie.application

import com.gnz.getamovie.service.ApiModule
import com.gnz.getamovie.service.NetworkModule
import dagger.Component
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
            ApplicationModule::class,
            CommonModule::class,
            NetworkModule::class,
            ApiModule::class,
            ViewModelModule::class,
            BuildersModule::class
        ]
)
internal interface ApplicationComponent : AndroidInjector<App> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<App>()
}