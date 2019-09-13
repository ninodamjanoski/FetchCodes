package com.endumedia.fetchcodes.ui

import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import com.endumedia.fetchcodes.R
import com.endumedia.fetchcodes.repository.FetchCodesRepository
import com.endumedia.fetchcodes.repository.FetchCodesRepositoryImpl
import com.endumedia.fetchcodes.repository.ImmediateSchedulerProvider
import com.endumedia.fetchcodes.utils.CountingAppExecutorsRule
import org.hamcrest.core.StringContains.containsString
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeoutException

/**
 * Created by Nino on 13.09.19
 */
class FetchCodesFragmentTest : BaseUiTest() {


    @Rule
    @JvmField
    val countingAppExecutors = CountingAppExecutorsRule()

    override fun initFetchCodesRepository(): FetchCodesRepository {
        return FetchCodesRepositoryImpl(fakeApi,
            db.codesDao(), ImmediateSchedulerProvider())
    }

    /**
     * Open screen and check if needed views are
     * shown, and fields are empty
     */
    @Test
    fun openScreenNoData() {

        startMainActivity()
        Espresso.onView(withId(R.id.tvResponseCodeInfo))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.tvResponseCode))
            .check(matches(isDisplayed()))
        Espresso.onView(withId(R.id.tvTimesFetched))
            .check(matches(isDisplayed()))
        Espresso.onView(withId(R.id.tvTimesFetchedInfo))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.btFetch))
            .check(matches(isDisplayed()))

        // Check if value views are empty
        Espresso.onView(withId(R.id.tvResponseCode))
            .check(matches(withText(containsString(""))))
        Espresso.onView(withId(R.id.tvTimesFetched))
            .check(matches(withText(containsString(""))))
    }


    /**
     * Open screen and check if db persisted data is loaded
     * into the views
     */
    @Test
    fun openScreenLoadDbPersistedData() {

        db.codesDao().saveCode(itemsFactory.createItem())

        startMainActivity()
        Espresso.onView(withId(R.id.tvResponseCodeInfo))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.tvResponseCode))
            .check(matches(isDisplayed()))
        Espresso.onView(withId(R.id.tvTimesFetched))
            .check(matches(isDisplayed()))
        Espresso.onView(withId(R.id.tvTimesFetchedInfo))
            .check(matches(isDisplayed()))

        Espresso.onView(withId(R.id.btFetch))
            .check(matches(isDisplayed()))

        // Check if values are equal with produced item values
        Espresso.onView(withId(R.id.tvResponseCode))
            .check(matches(withText(itemsFactory.list.last().responseCode)))
        Espresso.onView(withId(R.id.tvTimesFetched))
            .check(matches(withText(itemsFactory.list.size.toString())))
    }

    /**
     * Open screen and press fetch, then fetch successfuly, then check if
     * data is loaded into the views
     */
    @Test
    fun pressFetchInitialLoadData() {

        startMainActivity()
        // Load item to fake api
        fakeApi.addResponseCodeResult(itemsFactory.createItem())
        fakeApi.addNextPath(itemsFactory.listNextPath.last())

        Espresso.onView(withId(R.id.btFetch)).perform(click())

//        onView(withId(R.id.pbLoading)).check(matches(isDisplayed()))


//        onView(withId(R.id.btFetch)).check(matches(not(isEnabled())))

        Espresso.onView(withId(R.id.tvResponseCode))
            .check(matches(withText(containsString(itemsFactory.list.last().responseCode))))
        Espresso.onView(withId(R.id.tvTimesFetched))
            .check(matches(withText(itemsFactory.list.size.toString())))
    }


    /**
     * Open screen and press fetch, then fetch successfully, then press fetch again
     * to load new data, and verify the changes
     */
    @Test
    fun pressFetchLoadNewData() {

        startMainActivity()
        // Load item to fake api
        fakeApi.addResponseCodeResult(itemsFactory.createItem())
        fakeApi.addNextPath(itemsFactory.listNextPath.last())

        Espresso.onView(withId(R.id.btFetch)).perform(click())

        Espresso.onView(withId(R.id.tvResponseCode))
            .check(matches(withText(containsString(itemsFactory.list.last().responseCode))))
        Espresso.onView(withId(R.id.tvTimesFetched))
            .check(matches(withText(itemsFactory.list.size.toString())))

        // Load item to fake api
        fakeApi.addResponseCodeResult(itemsFactory.createItem())
        fakeApi.addNextPath(itemsFactory.listNextPath.last())

        Espresso.onView(withId(R.id.btFetch)).perform(click())

        Espresso.onView(withId(R.id.tvResponseCode))
            .check(matches(withText(containsString(itemsFactory.list.last().responseCode))))
        Espresso.onView(withId(R.id.tvTimesFetched))
            .check(matches(withText(itemsFactory.list.size.toString())))
    }

    /**
     * Open screen and press fetch, when no network, then check if
     * failure message is shown in the snackbar
     */
    @Test
    fun pressFetchInitialNoNetwork() {

        startMainActivity()

        fakeApi.failurePathMsg = "Failed to connect to /192.168.0.138:8000"

        Espresso.onView(withId(R.id.btFetch)).perform(click())

        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(fakeApi.failurePathMsg)))
    }


    @Throws(InterruptedException::class, TimeoutException::class)
    private fun startMainActivity() {
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            MainActivity::class.java)

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        activity = InstrumentationRegistry.getInstrumentation().startActivitySync(intent)
    }

}