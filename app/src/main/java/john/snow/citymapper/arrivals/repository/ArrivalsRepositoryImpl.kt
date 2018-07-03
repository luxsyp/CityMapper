package john.snow.citymapper.arrivals.repository

import io.reactivex.Single
import john.snow.citymapper.arrivals.api.ArrivalsService
import john.snow.citymapper.arrivals.model.Arrival

class ArrivalsRepositoryImpl(
        private val arrivalsService: ArrivalsService
): ArrivalsRepository {

    override fun getArrivals(naptanId: String): Single<List<Arrival>> {
        return arrivalsService.getArrivals(naptanId)
    }
}