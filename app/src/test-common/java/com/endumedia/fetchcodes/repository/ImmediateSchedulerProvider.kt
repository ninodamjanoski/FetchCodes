package com.endumedia.fetchcodes.repository

import com.endumedia.fetchcodes.util.SchedulerProvider
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executor


/**
 * Created by Nino on 12.09.19
 */
class ImmediateSchedulerProvider : SchedulerProvider {

    private val networkExecutor = Executor { command -> command.run() }
    private val scheduler = Schedulers.from(networkExecutor)

    override fun getComputation(): Scheduler {
        return scheduler
    }

    override fun getIo(): Scheduler {
        return scheduler
    }

    override fun getMain(): Scheduler {
        return scheduler
    }
}