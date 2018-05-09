
/* proof of concept program to track location of device on a map
** written 2018.04.17 by Aaron Gordon
 */

package com.example.aaron.trackme

import android.Manifest
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private var currentSpot = LatLng(lat,lng)

    private var requestingLocationUpdates = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_maps)

        mylog(TAG,"onCreate()")
        updateValuesFromBundle(savedInstanceState)      //used, at least, on device rotation

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        // Holds The Map
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            mylog(TAG,"onCreate(): Fine_Location Permission is not granted")
        }

        // The following is run when new location found
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                mylog(TAG,"locationCallback: Starting")
                locationResult ?: return
                val location = locationResult.lastLocation
                val xLat:Double = location.latitude
                val xLng:Double = location.longitude
                mylog(TAG,"locationCallback: lat = $xLat, long = $xLng")
                val here = LatLng(xLat,xLng)
                locate(here,"Callback Update")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mylog(TAG,"onResume(): ")

        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000L  //5 seconds

        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(locationRequest)
        val locationSettingsRequest = builder.build()
        val settingsClient = LocationServices.getSettingsClient(this)
        settingsClient.checkLocationSettings(locationSettingsRequest)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        try {
            fusedLocationClient.requestLocationUpdates(locationRequest,locationCallback, null)
        } catch (oops:SecurityException) {
            mylog(TAG,"onResume(): No permission for location update")
        } catch (e:Exception) {
            mylog(TAG,"onResume(): exception: $e")
        }
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        mylog(TAG,"Stopping Updates -----------------------")
        if (::fusedLocationClient.isInitialized) {
           fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, requestingLocationUpdates)
        outState?.putDouble(SAVED_LAT,currentSpot.latitude)
        outState?.putDouble(SAVED_LNG,currentSpot.longitude)
        mylog(TAG,"onSavedIS(): ${currentSpot.latitude}, ${currentSpot.longitude}")
        super.onSaveInstanceState(outState)
    }

    private fun updateValuesFromBundle(savedInstanceState: Bundle?) {
        var currentLat = lat
        var currentLng = lng
        if (savedInstanceState != null) {
            currentLat = savedInstanceState.getDouble(SAVED_LAT, lat)
            currentLng = savedInstanceState.getDouble(SAVED_LNG, lng)
            // Update the value of requestingLocationUpdates from the Bundle.
            if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
                requestingLocationUpdates = savedInstanceState.getBoolean(
                        REQUESTING_LOCATION_UPDATES_KEY)
            }
        }
        currentSpot  = LatLng(currentLat, currentLng)
        mylog(TAG,"updateFromBundle():($currentLat,$currentLng)")
    }

    /**
     * Perform a Log.i and also write message to the textarea on the device
     */
    private fun mylog(tag:String, message:String) {
        Log.i(tag,message)
        msg.append(message + "\n")
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Metro AES Building, Denver, Colorado, USA.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker and move the camera
        val markerSpot = currentSpot
        locate(markerSpot, MARKER_LABEL)
    }

    /**
     * Put a marker on the map at the passed-in coordinates and place the passed-in
     * string into the marker so the string shows when the marker is touched
     */
    private fun locate(coords:LatLng, label:String) {
        mMap.addMarker(MarkerOptions().position(coords).title(label))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(coords))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom (coords, zoom1))
        currentSpot = coords
        mylog(TAG, "Locate(): $label is at ${coords.latitude}, ${coords.longitude}")
    }

    companion object {
        const val lat = 39.74518    //bigger towards north pole
        const val lng = -105.0087   //more negative is west
        const val MARKER_LABEL = "MSU AES Building"
        const val zoom1 = 17.5f
        const val TAG = "TrackMe"
        const val REQUESTING_LOCATION_UPDATES_KEY = "RLU_KEY"
        const val SAVED_LAT = "savedLat"
        const val SAVED_LNG = "savedLNG"
    }
}
