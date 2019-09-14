package com.endumedia.fetchcodes.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.endumedia.fetchcodes.db.FetchCodesDb
import com.endumedia.fetchcodes.db.ResponseCodesDao
import com.endumedia.fetchcodes.vo.ResponseCodeResult
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.never

/**
 * Created by Nino on 12.09.19
 */
class FetchCodesRepositoryImplTest {


    @get:Rule // used to make all live data calls sync
    val instantExecutor = InstantTaskExecutorRule()

    private val db = mock(FetchCodesDb::class.java)
    private val dao = mock(ResponseCodesDao::class.java)

    private val fakeApi = FakeFetchCodeApi()
    private val responseCodesFactory = ItemsFactory()

    private lateinit var repository: FetchCodesRepository
    private val saveCodeLiveData = MutableLiveData<ResponseCodeResult>()
    private val FAILED_TO_CONNECT_MESSAGE = "Failed to connect to /192.168.0.138:8000"

    @Before
    fun init() {
        Mockito.`when`(db.codesDao()).thenReturn(dao)
//        Mockito.doReturn(getProductsDataSourceFromDb()).`when`(dao).getNotes()
        repository = FetchCodesRepositoryImpl(fakeApi, db.codesDao(),
            ImmediateSchedulerProvider())
    }


    @After
    fun clear() {
        responseCodesFactory.clear()
        fakeApi.clear()
    }

    /**
     * Testing if item is fetched, and correct network state is called
     */
    @Test
    fun fetchItem() {

        Mockito.doReturn(MutableLiveData<ResponseCodeResult>())
            .`when`(dao).getLatestCode()
        Mockito.doReturn(MutableLiveData<Long>()).`when`(dao).getCount()

        fakeApi.addResponseCodeResult(responseCodesFactory.createItem())
        fakeApi.addNextPath(responseCodesFactory.listNextPath.last())

        val listing = repository.getLatestCode()

        val networkObserver = mock(Observer::class.java) as Observer<NetworkState>
        listing.networkState.observeForever(networkObserver)

        val saveObserver = mock(Observer::class.java) as Observer<ResponseCodeResult>
        Mockito.`when`(dao.saveCode(responseCodesFactory.list.last())).then {
            saveCodeLiveData.value = it.arguments[0] as ResponseCodeResult?
            it
        }
        saveCodeLiveData.observeForever(saveObserver)

        listing.fetch.invoke()

        MatcherAssert.assertThat(getNetworkState(listing), CoreMatchers.`is`(NetworkState.LOADED))
        val inOrder = Mockito.inOrder(networkObserver)
        inOrder.verify(networkObserver).onChanged(NetworkState.LOADING)
        inOrder.verify(networkObserver).onChanged(NetworkState.LOADED)
        inOrder.verifyNoMoreInteractions()

        // Check if save to db is called
        Mockito.verify(saveObserver).onChanged(responseCodesFactory.list.last())
    }


    /**
     * When item is successfully fetched, then verify fetch again
     */
    @Test
    fun fetchItemThenRepeat() {

        Mockito.doReturn(MutableLiveData<ResponseCodeResult>())
            .`when`(dao).getLatestCode()
        Mockito.doReturn(MutableLiveData<Long>()).`when`(dao).getCount()

        fakeApi.addResponseCodeResult(responseCodesFactory.createItem())
        fakeApi.addNextPath(responseCodesFactory.listNextPath.last())

        val listing = repository.getLatestCode()

        val networkObserver = mock(Observer::class.java) as Observer<NetworkState>
        listing.networkState.observeForever(networkObserver)

        val saveObserver = mock(Observer::class.java) as Observer<ResponseCodeResult>
        Mockito.`when`(dao.saveCode(responseCodesFactory.list.last())).then {
            saveCodeLiveData.value = it.arguments[0] as ResponseCodeResult?
            it
        }
        saveCodeLiveData.observeForever(saveObserver)

        listing.fetch.invoke()

        MatcherAssert.assertThat(getNetworkState(listing), CoreMatchers.`is`(NetworkState.LOADED))
        val inOrder = Mockito.inOrder(networkObserver)
        inOrder.verify(networkObserver).onChanged(NetworkState.LOADING)
        inOrder.verify(networkObserver).onChanged(NetworkState.LOADED)
        inOrder.verifyNoMoreInteractions()

        // Fetch again, and verify if everything is in order
        fakeApi.addResponseCodeResult(responseCodesFactory.createItem())
        fakeApi.addNextPath(responseCodesFactory.listNextPath.last())
        listing.fetch.invoke()

        MatcherAssert.assertThat(getNetworkState(listing), CoreMatchers.`is`(NetworkState.LOADED))
        inOrder.verify(networkObserver).onChanged(NetworkState.LOADING)
        inOrder.verify(networkObserver).onChanged(NetworkState.LOADED)
        inOrder.verifyNoMoreInteractions()

        // Check if save to db is called 2 times, for every fetch
        Mockito.verify(saveObserver, Mockito.atMost(2)).onChanged(responseCodesFactory.list.last())
    }

    /**
     * Fetch response code when theres no network,
     * verify correct network state is called
     */
    @Test
    fun noNetworkNothingPersisted() {
        fakeApi.failurePathMsg = FAILED_TO_CONNECT_MESSAGE

        Mockito.doReturn(MutableLiveData<ResponseCodeResult>())
            .`when`(dao).getLatestCode()
        Mockito.doReturn(MutableLiveData<Long>()).`when`(dao).getCount()

        val listing = repository.getLatestCode()

        val networkObserver = mock(Observer::class.java) as Observer<NetworkState>
        listing.networkState.observeForever(networkObserver)

        listing.fetch.invoke()

        MatcherAssert.assertThat(getNetworkState(listing),
            CoreMatchers.`is`(NetworkState.error(fakeApi.failurePathMsg)))

        val inOrder = Mockito.inOrder(networkObserver)
        inOrder.verify(networkObserver).onChanged(NetworkState.LOADING)
        inOrder.verify(networkObserver).onChanged(NetworkState.error(fakeApi.failurePathMsg))
        inOrder.verifyNoMoreInteractions()
    }

    /**
     * When item is successfully fetched, then verify fetch again
     */
    @Test
    fun fetchItemNoNetworkThenRepeatFetchSuccessfully() {

        fakeApi.failurePathMsg = FAILED_TO_CONNECT_MESSAGE

        Mockito.doReturn(MutableLiveData<ResponseCodeResult>())
            .`when`(dao).getLatestCode()
        Mockito.doReturn(MutableLiveData<Long>()).`when`(dao).getCount()

        val listing = repository.getLatestCode()

        val networkObserver = mock(Observer::class.java) as Observer<NetworkState>
        listing.networkState.observeForever(networkObserver)

        listing.fetch.invoke()

        MatcherAssert.assertThat(getNetworkState(listing),
            CoreMatchers.`is`(NetworkState.error(FAILED_TO_CONNECT_MESSAGE)))
        val inOrder = Mockito.inOrder(networkObserver)
        inOrder.verify(networkObserver).onChanged(NetworkState.LOADING)
        inOrder.verify(networkObserver).onChanged(NetworkState.error(FAILED_TO_CONNECT_MESSAGE))
        inOrder.verifyNoMoreInteractions()

        // Fetch again, and verify if everything is in order
        fakeApi.clear()
        fakeApi.addResponseCodeResult(responseCodesFactory.createItem())
        fakeApi.addNextPath(responseCodesFactory.listNextPath.last())

        val saveObserver = mock(Observer::class.java) as Observer<ResponseCodeResult>
        Mockito.`when`(dao.saveCode(responseCodesFactory.list.last())).then {
            saveCodeLiveData.value = it.arguments[0] as ResponseCodeResult?
            it
        }
        saveCodeLiveData.observeForever(saveObserver)

        listing.fetch.invoke()

        MatcherAssert.assertThat(getNetworkState(listing), CoreMatchers.`is`(NetworkState.LOADED))
        inOrder.verify(networkObserver).onChanged(NetworkState.LOADING)
        inOrder.verify(networkObserver).onChanged(NetworkState.LOADED)
        inOrder.verifyNoMoreInteractions()

        // Check if save to db is called
        Mockito.verify(saveObserver).onChanged(responseCodesFactory.list.last())
    }

    private fun getResponseCodeResult(listing: Listing<ResponseCodeResult>): ResponseCodeResult {
        val observer = LoggingObserver<ResponseCodeResult>()
        listing.responseCodeResult.observeForever(observer)
        MatcherAssert.assertThat(observer.value, CoreMatchers.`is`(CoreMatchers.notNullValue()))
        return observer.value!!
    }

    private fun getResponseCodeCount(listing: Listing<ResponseCodeResult>): Long {
        val observer = LoggingObserver<Long>()
        listing.responseCodeCount.observeForever(observer)
        MatcherAssert.assertThat(observer.value, CoreMatchers.`is`(CoreMatchers.notNullValue()))
        return observer.value!!
    }

    /**
     * extract the latest network state from the listing
     */
    private fun getNetworkState(listing: Listing<ResponseCodeResult>) : NetworkState? {
        val networkObserver = LoggingObserver<NetworkState>()
        listing.networkState.observeForever(networkObserver)
        return networkObserver.value
    }

    /**
     * simple observer that logs the latest value it receives
     */
    private class LoggingObserver<T> : Observer<T> {
        var value : T? = null
        override fun onChanged(t: T?) {
            this.value = t
        }
    }
}