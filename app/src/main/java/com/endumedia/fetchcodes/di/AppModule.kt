package com.endumedia.fetchcodes.di

import android.app.Application
import androidx.room.Room
import com.endumedia.fetchcodes.api.FetchCodesApi
import com.endumedia.fetchcodes.db.FetchCodesDb
import com.endumedia.fetchcodes.db.ResponseCodesDao
import dagger.Module
import dagger.Provides
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Singleton


/**
 * Created by Nino on 19.08.19
 */
@Module(includes = [ViewModelModule::class])
class AppModule {

    @Singleton
    @Provides
    fun provideDailyNotesApi(): FetchCodesApi {
        return FetchCodesApi.create()
    }

    @Singleton
    @Provides
    fun provideDb(app: Application): FetchCodesDb {
        return Room
            .databaseBuilder(app, FetchCodesDb::class.java, "fetchCodes.db")
            .fallbackToDestructiveMigration()
            .build()
    }


    @Singleton
    @Provides
    fun provideCodesDao(db: FetchCodesDb): ResponseCodesDao {
        return db.codesDao()
    }

    @Singleton
    @Provides
    fun provideIOAppExecutor(): Executor {
        return Executors.newFixedThreadPool(5)
    }
}