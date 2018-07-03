package john.snow.citymapper.arrivals.repository

import io.reactivex.Single
import john.snow.citymapper.arrivals.model.Arrival

interface ArrivalsRepository {
    fun getArrivals(naptanId: String) : Single<List<Arrival>>
}