package john.snow.citymapper.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Looper
import android.support.design.widget.Snackbar
import android.support.design.widget.Snackbar.LENGTH_INDEFINITE
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.android.gms.location.*
import john.snow.citymapper.location.LocationPermissionController
import john.snow.citymapper.location.LondonConstants.LONDON_LATITUDE
import john.snow.citymapper.location.LondonConstants.LONDON_LONGITUDE
import john.snow.citymapper.R
import john.snow.citymapper.details.helper.DistanceHelper
import john.snow.citymapper.stoppoint.ui.StopPointNearbyFragment

class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationPermissionController: LocationPermissionController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationPermissionController = LocationPermissionController(this)

        // get location
        if (locationPermissionController.hasLocationPermission()) {
            getLastLocation()
        } else {
            locationPermissionController.requestLocationPermissions()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationPermissionController.onRequestPermissionResult(requestCode, grantResults)
    }

    internal fun showErrorWithAction(errorMessage: Int, errorCta: Int, ctaClick: () -> Unit) {
        Snackbar.make(findViewById(android.R.id.content), errorMessage, LENGTH_INDEFINITE).apply {
            setAction(errorCta) { ctaClick() }
            show()
        }
    }

    @SuppressLint("MissingPermission")
    internal fun getLastLocation() {
        fusedLocationClient.lastLocation
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful && task.result != null) {
                        onLocationSuccess(task.result.longitude, task.result.latitude)
                    } else {
                        Log.w("MainActivity", "getLastLocation:exception", task.exception)
                        fetchLocation()
                    }
                }
    }

    @SuppressLint("MissingPermission")
    private fun fetchLocation() {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 5000
            fastestInterval = 1000
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                Log.d("onLocationResult", "onLocationResult: " + locationResult.toString())
                locationResult?.let {
                    onLocationSuccess(it.lastLocation.longitude, it.lastLocation.latitude)
                }
            }
        }, Looper.myLooper())
    }

    private fun onLocationSuccess(longitude: Double, latitude: Double) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val fragment = if (DistanceHelper.isInLondon(longitude, latitude)) {
            StopPointNearbyFragment.newInstance(longitude, latitude)
        } else {
            StopPointNearbyFragment.newInstance(LONDON_LONGITUDE, LONDON_LATITUDE)
        }
        fragmentTransaction.add(R.id.container, fragment, TAG_FRAGMENT_STATIONS)
        fragmentTransaction.commitAllowingStateLoss()
    }

    private companion object {
        private const val TAG_FRAGMENT_STATIONS = "TAG_FRAGMENT_STATIONS"
    }
}
