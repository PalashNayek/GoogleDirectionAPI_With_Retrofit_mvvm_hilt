package com.palash.googledirectionapi_with_retrofit_mvvm_hilt.models

data class GeocodedWaypoint(
    val geocoder_status: String,
    val place_id: String,
    val types: List<String>
)