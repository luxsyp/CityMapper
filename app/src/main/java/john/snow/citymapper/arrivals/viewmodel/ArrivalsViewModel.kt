package john.snow.citymapper.arrivals.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import john.snow.citymapper.arrivals.model.Arrival
import john.snow.citymapper.arrivals.repository.ArrivalsRepository
import john.snow.citymapper.core.common.Resource
import john.snow.citymapper.core.execution.SchedulersFacade
import java.util.concurrent.TimeUnit


class ArrivalsViewModel(
        private val schedulersFacade: SchedulersFacade,
        private val arrivalsRepository: ArrivalsRepository
) : ViewModel() {

    private var pauseMonitoring: Boolean = false
    private val observableResponses =
            mutableMapOf<String, Pair<CompositeDisposable, MutableLiveData<Resource<List<Arrival>>>>>()

    fun getObservableData(naptanId: String): MutableLiveData<Resource<List<Arrival>>> {
        if (!observableResponses.containsKey(naptanId)) {
            observableResponses[naptanId] = CompositeDisposable() to MutableLiveData()
        }
        return observableResponses[naptanId]?.second
                ?: MutableLiveData() // never happens
    }

    fun loadArrivals(naptanId: String) {
        if (!observableResponses.containsKey(naptanId)) {
            observableResponses[naptanId] = CompositeDisposable() to MutableLiveData()
        }
        observableResponses[naptanId]?.let { observableResponse ->
            observableResponse.first.add(
                    arrivalsRepository.getArrivals(naptanId)
                            .subscribeOn(schedulersFacade.io())
                            .repeatWhen({ completed -> completed.delay(30, TimeUnit.SECONDS) })
                            .observeOn(schedulersFacade.ui())
                            .doOnSubscribe({ _ -> observableResponse.second.setValue(Resource.loading()) })
                            .observeOn(schedulersFacade.ui())
                            .subscribe(
                                    { arrivals ->
                                        observableResponse.second.setValue(Resource.success(
                                                arrivals.sortedBy { it.timeToStation }
                                                        .take(3)
                                        ))
                                    },
                                    { throwable -> observableResponse.second.value = Resource.error(throwable) }
                            )
            )
        }
    }

    fun onDetachedLine(naptanId: String) {
        observableResponses[naptanId]?.first?.run {
            clear()
            dispose()
        }
        observableResponses.remove(naptanId)
    }

    fun restartMonitoring() {
        if (pauseMonitoring) {
            observableResponses.onEach { loadArrivals(it.key) }
            pauseMonitoring = false
        }
    }

    fun pauseMonitoring() {
        pauseMonitoring = true
        observableResponses.onEach { it.value.first.clear() }
    }
}