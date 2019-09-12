package com.endumedia.fetchcodes.api

import android.util.Log
import com.endumedia.fetchcodes.vo.NextPathResult
import com.endumedia.fetchcodes.vo.ResponseCodeResult
import io.reactivex.Flowable
import io.reactivex.Single
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


/**
 * Created by Nino on 11.09.19
 */
interface FetchCodesApi {

    @GET
    fun getPath(): Single<NextPathResult>

    @GET("/{endpoint}")
    fun getResponseCode(@Path("endpoint") endPoint: String): Single<ResponseCodeResult>


    companion object {
        private const val BASE_URL = "http://localhost:8000/"
        fun create(): FetchCodesApi = create(HttpUrl.parse(BASE_URL)!!)
        fun create(httpUrl: HttpUrl): FetchCodesApi {
            val logger = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                Log.d("API", it)
            })
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
            return Retrofit.Builder()
                .baseUrl(httpUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(FetchCodesApi::class.java)
        }
    }

}