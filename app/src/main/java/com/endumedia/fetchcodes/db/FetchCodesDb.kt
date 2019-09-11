package com.endumedia.fetchcodes.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.endumedia.fetchcodes.vo.ResponseCodeResult


/**
 * Created by Nino on 11.09.19
 */
@Database(
    entities = [ResponseCodeResult::class],
    version = 1,
    exportSchema = false)
abstract class FetchCodesDb : RoomDatabase() {

    companion object {
        fun create(context: Context, useInMemory : Boolean): FetchCodesDb {
            val databaseBuilder = if(useInMemory) {
                Room.inMemoryDatabaseBuilder(context, FetchCodesDb::class.java)
            } else {
                Room.databaseBuilder(context, FetchCodesDb::class.java, "fetchCodes.db")
            }
            return databaseBuilder
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    abstract fun codesDao(): ResponseCodesDao
}