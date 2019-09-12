package com.endumedia.fetchcodes.repository

import com.endumedia.fetchcodes.vo.NextPathResult
import com.endumedia.fetchcodes.vo.ResponseCodeResult
import java.util.*
import java.util.concurrent.atomic.AtomicLong


/**
 * Created by Nino on 12.09.19
 */
class ItemsFactory {

    private val counter = AtomicLong(0)
    private val FAKE_URL = "http://192.168.0.138:8000/"
    val listNextPath = mutableListOf<NextPathResult>()
    val list = mutableListOf<ResponseCodeResult>()

    fun createItem(): ResponseCodeResult {
        val path = FAKE_URL + counter.incrementAndGet()
        listNextPath.add(NextPathResult(path))
        val responseCode = UUID.randomUUID().toString()
        val item = ResponseCodeResult(0, path, responseCode)
        list.add(item)
        return item
    }

    fun clear() {
        listNextPath.clear()
        list.clear()
    }
}