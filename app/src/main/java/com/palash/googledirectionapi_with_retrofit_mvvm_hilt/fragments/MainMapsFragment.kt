package com.palash.googledirectionapi_with_retrofit_mvvm_hilt.fragments

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.palash.googledirectionapi_with_retrofit_mvvm_hilt.R
import com.palash.googledirectionapi_with_retrofit_mvvm_hilt.view_models.DirectionsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainMapsFragment : Fragment() {

    private val directionsViewModels by viewModels<DirectionsViewModel>()

    private val callback = OnMapReadyCallback { googleMap ->
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        //request direction api
        directionsViewModels.fetchDirection("","","")
        //result
        directionsViewModels.directionViewModel.observe(viewLifecycleOwner, Observer {
            Log.d("", it.status)
        })

    }
}