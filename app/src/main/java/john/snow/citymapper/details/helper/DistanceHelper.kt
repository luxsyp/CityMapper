package john.snow.citymapper.details.helper

import android.location.Location
import john.snow.citymapper.location.LondonConstants

object DistanceHelper {
    private const val FIFTEEN_KILOMETERS = 15000

    fun isInLondon(longitude: Double, latitude: Double) : Boolean {
        val results = FloatArray(1)
        Location.distanceBetween(LondonConstants.LONDON_CENTER_LATITUDE, LondonConstants.LONDON_CENTER_LONGITUDE, latitude, longitude, results)
        val distanceInMeters = results[0]
        return distanceInMeters < FIFTEEN_KILOMETERS
    }

    fun distFrom(originLat: Double, originLon: Double, destLat: Double, destLon: Double): Double {
        val earthRadius = 6378.137
        val dLat = destLat * Math.PI / 180 - originLat * Math.PI / 180
        val dLon = destLon * Math.PI / 180 - originLon * Math.PI / 180
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(originLat * Math.PI / 180) * Math.cos(destLat * Math.PI / 180) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        val d = earthRadius * c
        return d * 1000.0 // meters
    }
}