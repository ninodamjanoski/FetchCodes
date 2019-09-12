package com.endumedia.fetchcodes.util

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


/**
 * Created by Nino on 12.09.19
 */
class SchedulerProviderImpl : SchedulerProvider {
    override fun getComputation(): Scheduler {
        return Schedulers.computation()
    }

    override fun getIo(): Scheduler {
        return Schedulers.io()
    }

    override fun getMain(): Scheduler {
        return AndroidSchedulers.mainThread()
    }
}