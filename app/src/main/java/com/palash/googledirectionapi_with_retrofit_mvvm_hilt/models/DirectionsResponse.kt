package com.palash.googledirectionapi_with_retrofit_mvvm_hilt.models

data class DirectionsResponse(
    val geocoded_waypoints: List<GeocodedWaypoint>,
    val status: String
)