package john.snow.citymapper.location

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import john.snow.citymapper.BuildConfig.APPLICATION_ID
import john.snow.citymapper.R
import john.snow.citymapper.ui.MainActivity

class LocationPermissionController(private val activity: MainActivity) {

    fun hasLocationPermission() =
            ActivityCompat.checkSelfPermission(activity, ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED

    fun requestLocationPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, ACCESS_COARSE_LOCATION)) {
            activity.showErrorWithAction(R.string.permission_rationale, android.R.string.ok, {
                startLocationPermissionRequest()
            })

        } else {
            startLocationPermissionRequest()
        }
    }

    private fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(activity, arrayOf(ACCESS_COARSE_LOCATION), REQUEST_CODE_LOCATION_PERMISSIONS)
    }

    fun onRequestPermissionResult(requestCode: Int, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSIONS) {
            when {
                grantResults[0] == PackageManager.PERMISSION_GRANTED -> activity.getLastLocation()
                else -> {
                    activity.showErrorWithAction(R.string.permission_denied, R.string.permission_settings, {
                        activity.startActivity(Intent().apply {
                            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            data = Uri.fromParts(DATA_SCHEME_PACKAGE, APPLICATION_ID, null)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        })
                    })
                }
            }
        }
    }

    private companion object {
        private const val DATA_SCHEME_PACKAGE = "package"
        private const val REQUEST_CODE_LOCATION_PERMISSIONS = 21
    }
}