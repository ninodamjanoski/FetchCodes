package com.endumedia.fetchcodes.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.endumedia.fetchcodes.vo.ResponseCodeResult


/**
 * Created by Nino on 11.09.19
 */
@Dao
abstract class ResponseCodesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun saveCode(code: ResponseCodeResult)

    @Query("SELECT * FROM ResponseCodeResult")
    abstract fun getLatestCode(): LiveData<ResponseCodeResult>

    @Query("SELECT COUNT(*) FROM ResponseCodeResult")
    abstract fun getCount(): LiveData<Long>
}