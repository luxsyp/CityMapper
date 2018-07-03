package john.snow.citymapper.stoppoint.repository

import io.reactivex.Single
import john.snow.citymapper.stoppoint.api.StopPointService
import john.snow.citymapper.stoppoint.model.StopPoint

class StopPointRepositoryImpl(
        private val stopPointService: StopPointService
) : StopPointRepository {

    override fun getStopPoint(longitude: Double, latitude: Double): Single<List<StopPoint>> {
        return stopPointService.nearbyStopPoint(longitude, latitude).map { it.stopPoints }
    }
}