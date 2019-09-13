package com.endumedia.fetchcodes.repository

import com.endumedia.fetchcodes.api.FetchCodesApi
import com.endumedia.fetchcodes.vo.NextPathResult
import com.endumedia.fetchcodes.vo.ResponseCodeResult
import io.reactivex.Single
import org.junit.internal.Throwables
import retrofit2.mock.Calls
import java.io.IOException
import java.lang.Exception


/**
 * Created by Nino on 12.09.19
 */
class FakeFetchCodeApi : FetchCodesApi {

    private val model = mutableListOf<ResponseCodeResult?>()
    private val modelNextPath = mutableListOf<NextPathResult?>()

    var failurePathMsg: String? = null
    var failureResponseMsg: String? = null

    fun addNextPath(nextPathResult: NextPathResult) {
        modelNextPath.add(nextPathResult)
    }

    fun addResponseCodeResult(responseCodeResult: ResponseCodeResult) {
        model.add(responseCodeResult)
    }


    fun clear() {
        failurePathMsg = null
        failureResponseMsg = null
        modelNextPath.clear()
        model.clear()
    }

    override fun getPath(): Single<NextPathResult> {
        failurePathMsg?.let {
            return Single.error(IOException(it))
        }

        return Single.just(modelNextPath.last())
    }

    override fun getResponseCode(endPoint: String): Single<ResponseCodeResult> {
        failureResponseMsg?.let {
            return Single.error(IOException(it))
        }

        return Single.just(model.last())

    }
}