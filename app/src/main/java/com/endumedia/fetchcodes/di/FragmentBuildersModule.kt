package com.endumedia.fetchcodes.di

import com.endumedia.fetchcodes.ui.FetchCodesFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by Nino on 11.09.19
 */
@Suppress("unused")
@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeFetchCodesFragment(): FetchCodesFragment
}