package com.kings.locationmap

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*
import com.kings.locationmap.pokemoon.ui.ListActivity
import kotlinx.android.synthetic.main.activity_maps.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location

    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private var locationUpdateState = false

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        //Location callback Updates location realtime as changes are made
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationCallback = object : LocationCallback(){
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)

                database = FirebaseDatabase.getInstance()
                reference = database.reference

                lastLocation = p0.lastLocation
                reference.child("kings").setValue(DataBaseModel(lastLocation.longitude, lastLocation.latitude))
                Log.i("Location Kings", "${lastLocation.longitude}, ${lastLocation.latitude}")
            }
        }

        createLocationRequest()
        mapTypeSatelite()
        mapTypeNormal()
        pokemoonPage()
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case
     * Getting Coordinates of Partner and setting up placemarkers to partner's location
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMarkerClickListener(this)

        var databasex = FirebaseDatabase.getInstance().getReference("Austine")
        var updateAust = object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                var lat = snapshot.child("latitude").value.toString().toDouble()
                var long = snapshot.child("longitude").value.toString().toDouble()

                var codinatex = LatLng(lat,long)
                map.addMarker(MarkerOptions().position(codinatex))
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(codinatex, 20f))
                placeMarkers(codinatex)
                Log.i("Location Austine", "$codinatex")
            }
        }
        databasex.addValueEventListener(updateAust)
        setUpMap()

    }

    override fun onMarkerClick(p0: Marker?) = false

    /**
     * Setting up map to read current location
     * Permission is first requested
     * Fused Location sets up the most recent location updates
     */
    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        map.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                lastLocation = location
            }
        }
    }

    /**
     * Setting up markers on map
     * and setting marker size
     */
    private fun placeMarkers (location: LatLng) {
        val  l = 100
        val b = 100
        val bmp = BitmapFactory.decodeResource(resources, R.drawable.augustine_pic)
        val smallMarker = Bitmap.createScaledBitmap(bmp,l,b, false)
        val markerOptions =  MarkerOptions().position(location)

        markerOptions.title("Augustine Lat: ${lastLocation.latitude}, Long: ${lastLocation.longitude}")
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
        map.clear()
        map.addMarker(markerOptions)
    }


    /**
     *Asking for permission to get location updates
     * On permission granted location update request is made
     */
    private fun startLocationUpdates() {
        if(ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    /**
     *Sets frequency of updates
     * interval and fastestInterval sets the time difference to each updates
     */
    private fun createLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.interval = 6000
        locationRequest.fastestInterval = 1000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener{ locationUpdateState = true
            startLocationUpdates() }
        task.addOnFailureListener { e ->
            if (e is ResolvableApiException) {
                try { e.startResolutionForResult(this@MapsActivity, REQUEST_CHECK_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) { }
            }
        }
    }

    /**
     *Overrides OnActivityResult
     * If result is okay location is updated
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                locationUpdateState = true
                startLocationUpdates()
            }
        }
    }


    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }


    public override fun onResume() {
        super.onResume()
        if(!locationUpdateState) {
            startLocationUpdates()
        }
    }

    /**
     *Onclick listener to load next page
     */
    fun pokemoonPage (){
        pokemoon.setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     *Onclick listner to change map type to normal
     */
    fun mapTypeSatelite(){
        satelite_image_view.setOnClickListener {
            satelite_image_view.visibility = View.INVISIBLE
            map_type_Normal.visibility = View.VISIBLE
            map.mapType = GoogleMap.MAP_TYPE_SATELLITE
        }
    }

    /**
     * On Click Listener to change map type to satellite
     */
    fun mapTypeNormal(){
        map_type_Normal.setOnClickListener {
            map_type_Normal.visibility = View.INVISIBLE
            satelite_image_view.visibility = View.VISIBLE
            map.mapType = GoogleMap.MAP_TYPE_NORMAL
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val REQUEST_CHECK_SETTINGS = 2
    }
}