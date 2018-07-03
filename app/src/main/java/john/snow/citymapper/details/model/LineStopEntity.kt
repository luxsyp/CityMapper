package john.snow.citymapper.details.model

data class LineStopEntity(
        val name: String,
        val selected: Boolean,
        var distanceFromUser: Double
) {
    var highlight: Boolean = false
}