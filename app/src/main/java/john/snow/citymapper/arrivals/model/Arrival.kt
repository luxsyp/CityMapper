package john.snow.citymapper.arrivals.model

data class Arrival(
        val id: String,
        val naptanId: String,
        val lineId: String,
        val lineName: String,
        val timeToStation: Int
)