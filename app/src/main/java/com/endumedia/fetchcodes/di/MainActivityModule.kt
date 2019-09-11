package com.endumedia.fetchcodes.di

import com.endumedia.fetchcodes.repository.FetchCodesRepository
import com.endumedia.fetchcodes.repository.FetchCodesRepositoryImpl
import com.endumedia.fetchcodes.ui.MainActivity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by Nino on 11.09.19
 */

@Suppress("unused")
@Module
abstract class MainActivityModule {

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeMainActivity(): MainActivity

    @Binds
    abstract fun bindNotesRepository(notesRepository: FetchCodesRepositoryImpl): FetchCodesRepository
}