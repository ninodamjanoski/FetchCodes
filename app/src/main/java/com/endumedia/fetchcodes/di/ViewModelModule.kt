package com.endumedia.fetchcodes.di

import androidx.lifecycle.ViewModelProvider
import com.endumedia.notes.di.FetchCodesViewModelFactory
import dagger.Binds
import dagger.Module


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
//    @Binds
//    @IntoMap
//    @ViewModelKey(NewNoteViewModel::class)
//    abstract fun bindNewNoteViewModel(newNoteViewModel: NewNoteViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: FetchCodesViewModelFactory): ViewModelProvider.Factory
}
