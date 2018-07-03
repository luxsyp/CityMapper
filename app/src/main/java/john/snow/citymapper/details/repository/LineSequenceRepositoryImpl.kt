package john.snow.citymapper.details.repository

import io.reactivex.Single
import john.snow.citymapper.details.api.LineSequenceService
import john.snow.citymapper.details.model.LineSequenceResponse

class LineSequenceRepositoryImpl(
        private val lineSequenceService: LineSequenceService
) : LineSequenceRepository {
    override fun getLineSequence(lineId: String): Single<LineSequenceResponse> {
        return lineSequenceService.getLineSequence(lineId)
    }
}