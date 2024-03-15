package com.palash.googledirectionapi_with_retrofit_mvvm_hilt.models

data class Step(
    val distance: Distance,
    val duration: Duration,
    val end_location: EndLocation,
    val html_instructions: String,
    val maneuver: String,
    val polyline: Polyline,
    val start_location: StartLocation,
    val travel_mode: String
)