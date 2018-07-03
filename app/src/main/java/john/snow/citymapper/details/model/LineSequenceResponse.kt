package john.snow.citymapper.details.model

data class StopPointOrder(
        val id: String,
        val parentId: String?
)

data class LineStopPointSequences(
        val stopPoint: List<StopPointOrder>
)

data class LineSequenceResponse(
        val lineId: String,
        val lineName: String,
        val stations: List<LineStation>,
        val stopPointSequences: List<LineStopPointSequences>
)