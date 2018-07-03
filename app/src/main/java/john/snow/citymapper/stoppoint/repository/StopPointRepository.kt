package john.snow.citymapper.stoppoint.repository

import io.reactivex.Single
import john.snow.citymapper.stoppoint.model.StopPoint

interface StopPointRepository {
    fun getStopPoint(longitude: Double, latitude: Double): Single<List<StopPoint>>
}

