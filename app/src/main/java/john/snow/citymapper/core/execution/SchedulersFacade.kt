package john.snow.citymapper.core.execution

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

interface SchedulersFacade {
    fun io(): Scheduler
    fun computation(): Scheduler
    fun ui(): Scheduler
}
