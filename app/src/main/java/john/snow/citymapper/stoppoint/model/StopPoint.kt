package john.snow.citymapper.stoppoint.model

data class StopPoint(
        val id: String,
        val naptanId: String,
        val commonName: String,
        val distance: Double
)