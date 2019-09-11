package com.endumedia.fetchcodes.repository

import androidx.lifecycle.LiveData


/**
 * Created by Nino on 11.09.19
 */
interface FetchCodesRepository {
    fun saveCode(code: String)
    fun getLatestCode(): LiveData<String>
}