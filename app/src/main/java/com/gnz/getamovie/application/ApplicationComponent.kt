package com.gnz.getamovie.application

import com.gnz.getamovie.application.scopes.AppScope
import com.gnz.getamovie.service.ApiModule
import com.gnz.getamovie.service.NetworkModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule


@AppScope
@Component(
        modules = [
            ApplicationModule::class,
            CommonModule::class,
            NetworkModule::class,
            ApiModule::class,
            BuildersModule::class
        ]
)
internal interface ApplicationComponent : AndroidInjector<App> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<App>()
}