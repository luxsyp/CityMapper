package john.snow.citymapper.stoppoint.api

import io.reactivex.Single
import john.snow.citymapper.stoppoint.model.StopPointResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface StopPointService {

    @GET("StopPoint")
    fun nearbyStopPoint(
            @Query("lon") longitude: Double,
            @Query("lat") latitude: Double,
            @Query("stopTypes") stopTypes: String = DEFAULT_STOP_TYPE,
            @Query("radius") radius: Int = DEFAULT_RADIUS
    ): Single<StopPointResponse>

    private companion object {
        private const val DEFAULT_STOP_TYPE = "NaptanMetroStation"
        private const val DEFAULT_RADIUS = 1000
    }
}