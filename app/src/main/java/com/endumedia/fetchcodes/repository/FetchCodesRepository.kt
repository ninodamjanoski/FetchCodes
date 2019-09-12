package com.endumedia.fetchcodes.repository

import com.endumedia.fetchcodes.vo.ResponseCodeResult


/**
 * Created by Nino on 11.09.19
 */
interface FetchCodesRepository {
    fun saveCode(responseCodeResult: ResponseCodeResult?)
    fun getLatestCode(): Listing<ResponseCodeResult>
}