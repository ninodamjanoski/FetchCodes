package com.endumedia.fetchcodes.repository

import androidx.lifecycle.LiveData
import com.endumedia.fetchcodes.vo.ResponseCodeResult


/**
 * Created by Nino on 12.09.19
 */

data class Listing<T>(
    val responseCodeResult: LiveData<ResponseCodeResult>,
    val responseCodeCount: LiveData<Long>,
    val fetch: () -> Unit,
    val networkState: LiveData<NetworkState>
)