package john.snow.citymapper.details.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import john.snow.citymapper.core.common.Resource
import john.snow.citymapper.core.execution.SchedulersFacade
import john.snow.citymapper.details.helper.DistanceHelper
import john.snow.citymapper.details.model.LineStopEntity
import john.snow.citymapper.details.repository.LineSequenceRepository

class LineSequenceViewModel(
        private val schedulersFacade: SchedulersFacade,
        private val lineSequenceRepository: LineSequenceRepository
) : ViewModel() {

    val lineSequenceResponse = MutableLiveData<Resource<List<LineStopEntity>>>()
    private val compositeDisposable = CompositeDisposable()

    fun loadLineSequence(lineId: String, hubNaptanId: String, userLat: Double, userLon: Double) {
        compositeDisposable.add(lineSequenceRepository.getLineSequence(lineId)
                .subscribeOn(schedulersFacade.io())
                .observeOn(schedulersFacade.ui())
                .doOnSubscribe({ _ -> lineSequenceResponse.setValue(Resource.loading()) })
                .flatMap({ response ->
                    val entities = mutableListOf<LineStopEntity>()
                    response.stopPointSequences.getOrNull(0)?.stopPoint?.onEach { stopPoint ->
                        response.stations.find {
                            stopPoint.parentId == it.id || stopPoint.id == it.id
                        }?.let { station ->
                            entities.add(
                                    LineStopEntity(
                                            station.name,
                                            station.id == hubNaptanId,
                                            DistanceHelper.distFrom(userLat, userLon, station.lat, station.lon)
                                    )
                            )
                        }
                    }
                    // find the nearest one
                    entities.minBy { it.distanceFromUser }?.let { it.highlight = true }
                    Single.just(entities)
                })

                .subscribe(
                        { lineSequence -> lineSequenceResponse.setValue(Resource.success(lineSequence)) },
                        { throwable -> lineSequenceResponse.value = Resource.error(throwable) }
                )
        )
    }
}
