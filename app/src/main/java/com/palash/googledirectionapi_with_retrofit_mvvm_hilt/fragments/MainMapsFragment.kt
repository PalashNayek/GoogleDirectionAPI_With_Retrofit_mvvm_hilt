package com.palash.googledirectionapi_with_retrofit_mvvm_hilt.fragments

import android.Manifest
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import com.palash.googledirectionapi_with_retrofit_mvvm_hilt.R
import com.palash.googledirectionapi_with_retrofit_mvvm_hilt.databinding.FragmentMainMapsBinding
import com.palash.googledirectionapi_with_retrofit_mvvm_hilt.models.Route
import com.palash.googledirectionapi_with_retrofit_mvvm_hilt.view_models.DirectionsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainMapsFragment : Fragment() {

    private var _binding: FragmentMainMapsBinding? = null
    private val binding get() = _binding!!

    private var mapFragment: SupportMapFragment? = null

    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mMap: GoogleMap

    companion object {
        private const val LOCATION_REQUEST_CODE = 1
    }


    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true
        //mMap.setOnMarkerClickListener(mMap)

        setUpMap()

    }

    private val directionsViewModels by viewModels<DirectionsViewModel>()

    private var distance: String? = null
    private var duration: String? = null

    //Destination lat long //22.501314095804787, 88.38329130099031
    private val destinationsLat: Double = "22.501314095804787".toDouble()
    private val destinationLong: Double = "88.38329130099031".toDouble()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context!!)


        //direction api fetch data..................
        directionsViewModels.directionViewModel.observe(viewLifecycleOwner, Observer {
            drawPolylines(it.routes)
        })

    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            &&
            ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE
            )
            mapFragment?.getMapAsync(callback)
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context!!)
            return
        }

        mMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(requireActivity()) { location ->
            if (location != null) {
                lastLocation = location
                val currentLatLong = LatLng(location.latitude, location.longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 14.5f))

                //make direction api call
                directionsViewModels.fetchDirection(
                    location.latitude.toString() + "," + location.longitude.toString(),
                    "$destinationsLat,$destinationLong",
                    "AIzaSyBOD5-j2ElNi1GuIbPEZntT1iNLHKassW4"
                )
            } else {
                Toast.makeText(context, "Please turn on your device location", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    //override fun onMarkerClick(p0: Marker) = false

    private fun drawPolylines(routes: List<Route>) {
        for (route in routes) {
            for (tx in route.legs) {
                distance = tx.distance.text
                duration = tx.duration.text
            }
            val polylineOptions = PolylineOptions()
            polylineOptions.color(Color.BLACK)
            polylineOptions.width(5f)

            // Decode polyline points
            val points = PolyUtil.decode(route.overview_polyline.points ?: "")
            for (point in points) {
                polylineOptions.add(point)
            }

            mMap.addPolyline(polylineOptions)
            Log.d("Distance", distance.toString())
            Log.d("Duration", duration.toString())
            val markerOptions = MarkerOptions().position(LatLng(destinationsLat, destinationLong))
            markerOptions.title("$distance")
            mMap.addMarker(markerOptions)
            // mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(route., 100))
            val boundsBuilder = LatLngBounds.Builder().include(LatLng(lastLocation.latitude,lastLocation.longitude)).include(LatLng(destinationsLat, destinationLong))

            val bounds = boundsBuilder.build()
            // Animate camera to fit the bounds with padding (optional)
            val padding = 50 // in pixels
            val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
            mMap.animateCamera(cameraUpdate)
        }
    }

    private fun placeMarkerOnMap(currentLatLong: LatLng) {

        val markerOptions = MarkerOptions().position(currentLatLong)
        markerOptions.title("$distance")
        mMap.addMarker(markerOptions)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}