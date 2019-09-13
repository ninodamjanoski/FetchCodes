package com.endumedia.fetchcodes.utils

import com.endumedia.fetchcodes.util.SchedulerProvider
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executor


/**
 * Created by Nino on 13.09.19
 */
class UiScheduler(private val comp: Executor,
                  private val io: Executor,
                  private val main: Executor) : SchedulerProvider {
    override fun getComputation(): Scheduler {
        return Schedulers.from(comp)
    }

    override fun getIo(): Scheduler {
        return Schedulers.from(io)
    }

    override fun getMain(): Scheduler {
        return Schedulers.from(main)
    }
}