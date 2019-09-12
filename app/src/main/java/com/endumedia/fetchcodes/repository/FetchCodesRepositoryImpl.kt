package com.endumedia.fetchcodes.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.endumedia.fetchcodes.api.FetchCodesApi
import com.endumedia.fetchcodes.db.ResponseCodesDao
import com.endumedia.fetchcodes.endPoint
import com.endumedia.fetchcodes.util.SchedulerProvider
import com.endumedia.fetchcodes.vo.ResponseCodeResult
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


/**
 * Created by Nino on 11.09.19
 */
class FetchCodesRepositoryImpl @Inject constructor(private val fetchCodesApi: FetchCodesApi,
                                                   private val codesDao: ResponseCodesDao,
                                                   private val schedulerProvider: SchedulerProvider) : FetchCodesRepository {

    override fun saveCode(responseCodeResult: ResponseCodeResult?) {
        responseCodeResult?.let {
            codesDao.saveCode(it)
        }
    }

    override fun getLatestCode(): Listing<ResponseCodeResult> {

        val fetchTrigger = MutableLiveData<Unit>()
        val fetchState = Transformations.switchMap(fetchTrigger) {
            fetchCode()
        }

        return Listing(
            responseCodeResult = codesDao.getLatestCode(),
            responseCodeCount = codesDao.getCount(),
            fetch = {
                fetchTrigger.value = null
            },
            networkState = fetchState
        )
    }

    private fun fetchCode(): LiveData<NetworkState> {

        val networkState = MutableLiveData<NetworkState>()
        networkState.value = NetworkState.LOADING

        fetchCodesApi.getPath()
            .flatMap { result -> fetchCodesApi.getResponseCode(result.endPoint())}
            .subscribeOn(schedulerProvider.getIo())
            .subscribe { result, error ->
                when (error) {
                    null -> networkState.postValue(NetworkState.LOADED)
                    error -> networkState.postValue(NetworkState.error(error.message))
                }
                saveCode(result)
            }
        return networkState
    }

}