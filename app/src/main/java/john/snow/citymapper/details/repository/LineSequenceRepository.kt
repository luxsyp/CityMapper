package john.snow.citymapper.details.repository

import io.reactivex.Single
import john.snow.citymapper.details.model.LineSequenceResponse

interface LineSequenceRepository {
    fun getLineSequence(lineId: String) : Single<LineSequenceResponse>
}