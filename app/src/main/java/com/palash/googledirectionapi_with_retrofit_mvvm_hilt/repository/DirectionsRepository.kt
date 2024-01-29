package com.palash.googledirectionapi_with_retrofit_mvvm_hilt.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.palash.googledirectionapi_with_retrofit_mvvm_hilt.api_service.DirectionsAPI
import com.palash.googledirectionapi_with_retrofit_mvvm_hilt.models.DirectionsResponse
import javax.inject.Inject

class DirectionsRepository @Inject constructor(private val directionsAPI: DirectionsAPI) {

    private var _direction = MutableLiveData<DirectionsResponse>()
    val direction : LiveData<DirectionsResponse> get() = _direction

    suspend fun getDirections(origin : String, destination : String, apiKey : String){
        val response = directionsAPI.getDirections(origin, destination, apiKey)
        if (response.isSuccessful){
            _direction.postValue(response.body())
        }else{
            //error handle.................................
        }
    }
}