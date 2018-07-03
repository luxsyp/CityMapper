package john.snow.citymapper.stoppoint.model

data class LineStop(
        val id: String,
        val naptanId: String,
        val commonName: String,
        val distance: Double
)