package john.snow.citymapper.stoppoint.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import john.snow.citymapper.core.common.Resource
import john.snow.citymapper.core.execution.SchedulersFacade
import john.snow.citymapper.stoppoint.model.StopPoint
import john.snow.citymapper.stoppoint.repository.StopPointRepository


class StopPointViewModel(
        private val schedulersFacade: SchedulersFacade,
        private val stopPointRepository: StopPointRepository
) : ViewModel() {

    val stopPointResponse = MutableLiveData<Resource<List<StopPoint>>>()
    private val compositeDisposable = CompositeDisposable()

    fun loadStopPoint(longitude: Double, latitude: Double) {
        compositeDisposable.add(stopPointRepository.getStopPoint(longitude, latitude)
                .subscribeOn(schedulersFacade.io())
                .observeOn(schedulersFacade.ui())
                .doOnSubscribe({ _ -> stopPointResponse.setValue(Resource.loading()) })
                .subscribe(
                        { stopPoint -> stopPointResponse.setValue(Resource.success(stopPoint)) },
                        { throwable -> stopPointResponse.value = Resource.error(throwable) }
                )
        )
    }
}
