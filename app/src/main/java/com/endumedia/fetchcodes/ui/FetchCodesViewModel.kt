package com.endumedia.fetchcodes.ui

import androidx.lifecycle.ViewModel
import com.endumedia.fetchcodes.repository.FetchCodesRepository
import javax.inject.Inject


/**
 * Created by Nino on 11.09.19
 */
class FetchCodesViewModel @Inject constructor(repository: FetchCodesRepository) : ViewModel() {

    private val listing = repository.getLatestCode()

    val responseCode = listing.responseCodeResult

    val responseCodeCount = listing.responseCodeCount

    val networkState = listing.networkState

    fun fetch() {
        listing.fetch.invoke()
    }
}