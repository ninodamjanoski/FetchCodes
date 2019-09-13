package com.endumedia.fetchcodes.ui

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.arch.core.executor.testing.CountingTaskExecutorRule
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.endumedia.fetchcodes.db.FetchCodesDb
import com.endumedia.fetchcodes.repository.*
import com.endumedia.notes.utils.ViewModelUtil
import org.junit.After
import org.junit.Before
import org.junit.Rule


/**
 * Created by Nino on 13.09.19
 */
abstract class BaseUiTest {


    protected lateinit var activity: Activity
    @get:Rule
    var testRule = CountingTaskExecutorRule()

    protected val itemsFactory = ItemsFactory()

    protected val fakeApi = FakeFetchCodeApi()
    private var app: Application? = null

    protected lateinit var db: FetchCodesDb

    @Before
    fun init() {
        app = ApplicationProvider.getApplicationContext<Application>()
        db = Room.inMemoryDatabaseBuilder(app!!, FetchCodesDb::class.java)
            .allowMainThreadQueries()
            .build()
        injectViewModel()
    }

    @After
    fun clear() {
        itemsFactory.clear()
        fakeApi.clear()
    }

    abstract fun initFetchCodesRepository(): FetchCodesRepository

    private fun injectViewModel() {

        app?.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {

            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
                if (activity is FragmentActivity) {
                    activity.supportFragmentManager
                        .registerFragmentLifecycleCallbacks(
                            object : FragmentManager.FragmentLifecycleCallbacks() {
                                override fun onFragmentCreated(
                                    fm: FragmentManager,
                                    f: Fragment,
                                    savedInstanceState: Bundle?) {
                                    if (activity is MainActivity) {
                                        if (f is FetchCodesFragment) {
                                            val repository = initFetchCodesRepository()
                                            val model = FetchCodesViewModel(repository)
                                            f.viewModelFactory = ViewModelUtil.createFor(model)
                                        }
                                    }
                                }
                            }, true)
                }
            }

            override fun onActivityPaused(activity: Activity?) {
            }

            override fun onActivityResumed(activity: Activity?) {
            }

            override fun onActivityStarted(activity: Activity?) {
            }

            override fun onActivityDestroyed(activity: Activity?) {
            }

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
            }

            override fun onActivityStopped(activity: Activity?) {
            }

        })
    }


}