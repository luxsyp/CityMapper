package john.snow.citymapper.details.api

import io.reactivex.Single
import john.snow.citymapper.details.model.LineSequenceResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface LineSequenceService {

    @GET("Line/{lineId}/Route/Sequence/inbound")
    fun getLineSequence(
            @Path("lineId") lineId: String
    ): Single<LineSequenceResponse>
}