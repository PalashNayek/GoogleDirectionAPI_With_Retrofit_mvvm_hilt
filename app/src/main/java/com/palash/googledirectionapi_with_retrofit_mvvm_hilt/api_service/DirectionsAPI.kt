package com.palash.googledirectionapi_with_retrofit_mvvm_hilt.api_service

import com.palash.googledirectionapi_with_retrofit_mvvm_hilt.models.DirectionsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface DirectionsAPI {

    @GET("maps/api/directions/json")
    suspend fun getDirections(@Query("origin") origin: String, @Query("destination") destination: String, @Query("key") apiKey: String) : Response<DirectionsResponse>
}