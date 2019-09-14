package com.endumedia.fetchcodes.ui

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.endumedia.fetchcodes.R
import com.endumedia.fetchcodes.repository.FetchCodesRepository
import com.endumedia.fetchcodes.repository.FetchCodesRepositoryImpl
import com.endumedia.fetchcodes.repository.ImmediateSchedulerProvider
import com.endumedia.fetchcodes.utils.CountingAppExecutorsRule
import org.hamcrest.core.StringContains.containsString
import org.junit.Rule
import org.junit.Test

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
    fun openScreenInitialNoData() {

        startMainActivity()
        matchMainViewsDisplayed()

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
        matchMainViewsDisplayed()

        // Check if values are equal with produced item values
        Espresso.onView(withId(R.id.tvResponseCode))
            .check(matches(withText(itemsFactory.list.last().responseCode)))
        Espresso.onView(withId(R.id.tvTimesFetched))
            .check(matches(withText(itemsFactory.list.size.toString())))
    }

    private fun matchMainViewsDisplayed() {
        onView(withId(R.id.tvResponseCode))
            .check(matches(isDisplayed()))
        onView(withId(R.id.tvResponseCodeInfo))
            .check(matches(isDisplayed()))
        onView(withId(R.id.tvTimesFetched))
            .check(matches(isDisplayed()))
        onView(withId(R.id.tvTimesFetchedInfo))
            .check(matches(isDisplayed()))

        onView(withId(R.id.btFetch))
            .check(matches(isDisplayed()))
    }

    /**
     * Open screen and press fetch, no preloaded data,
     * then fetch successfully, and check if data is loaded into the views
     */
    @Test
    fun pressFetchInitialLoadData() {

        startMainActivity()
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
     * Open screen and press fetch, when already loaded data from db,
     * then fetch successfully, then check if latest data is loaded into the views
     */
    @Test
    fun pressFetchLoadDataNonEmptyDb() {

        db.codesDao().saveCode(itemsFactory.createItem())

        startMainActivity()
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
     * Open screen and press fetch, then fetch successfully,
     * then press fetch again to load new data, and verify the changes
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
}