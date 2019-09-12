package com.endumedia.fetchcodes.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.endumedia.fetchcodes.ui.FetchCodesViewModel
import com.endumedia.notes.di.FetchCodesViewModelFactory
import com.endumedia.notes.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


/**
 * Created by Nino on 19.08.19
 */
@Suppress("unused")
@Module
abstract class ViewModelModule {

//    @Binds
//    @IntoMap
//    @ViewModelKey(NotesViewModel::class)
//    abstract fun bindNotesViewModel(notesViewModel: NotesViewModel): ViewModel
//
    @Binds
    @IntoMap
    @ViewModelKey(FetchCodesViewModel::class)
    abstract fun bindFetchCodesViewModel(fetchCodesViewModel: FetchCodesViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: FetchCodesViewModelFactory): ViewModelProvider.Factory
}
