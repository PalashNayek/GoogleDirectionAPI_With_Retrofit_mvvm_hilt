package com.palash.googledirectionapi_with_retrofit_mvvm_hilt.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.palash.googledirectionapi_with_retrofit_mvvm_hilt.models.DirectionsResponse
import com.palash.googledirectionapi_with_retrofit_mvvm_hilt.repository.DirectionsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DirectionsViewModel @Inject constructor(private val repository: DirectionsRepository) : ViewModel() {

    val directionViewModel : LiveData<DirectionsResponse> get() = repository.direction

    fun fetchDirection(origin : String, destination : String, apiKey : String){
        viewModelScope.launch {
            repository.getDirections(origin, destination, apiKey)
        }
    }
}