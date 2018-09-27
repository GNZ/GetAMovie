package com.gnz.getamovie.application

import com.gnz.getamovie.application.scopes.FragmentScope
import com.gnz.getamovie.features.nowplaying.NowPlayingFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class BuildersModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun bindNowPlayingFragment(): NowPlayingFragment
}