package com.endumedia.fetchcodes.util

import io.reactivex.Scheduler


/**
 * Created by Nino on 12.09.19
 */
interface SchedulerProvider {
    fun getComputation(): Scheduler

    fun getIo(): Scheduler

    fun getMain(): Scheduler

}