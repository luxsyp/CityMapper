package john.snow.citymapper.arrivals.api

import io.reactivex.Single
import john.snow.citymapper.arrivals.model.Arrival
import retrofit2.http.GET
import retrofit2.http.Path

interface ArrivalsService {

    @GET("StopPoint/{naptanId}/Arrivals")
    fun getArrivals(
            @Path("naptanId") naptanId: String
    ): Single<List<Arrival>>
}