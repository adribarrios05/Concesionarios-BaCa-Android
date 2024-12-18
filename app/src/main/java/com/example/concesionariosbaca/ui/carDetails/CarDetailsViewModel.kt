package com.example.concesionariosbaca.ui.carDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.concesionariosbaca.data.entities.CarEntity
import com.example.concesionariosbaca.data.repository.CarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarDetailsViewModel @Inject constructor (
    private val carRepository: CarRepository
): ViewModel() {

    private val _carDetails = MutableStateFlow<CarEntity?>(null)
    val carDetails: StateFlow<CarEntity?>
        get() = _carDetails.asStateFlow()

    fun getCarDetails(carId: String) {
        viewModelScope.launch {
            try {
                val car = carRepository.getCar(carId)
                _carDetails.value = car
            } catch (e: Exception){
                _carDetails.value = null
            }
        }
    }

    private fun loadLocalCarsFromApi() {
        viewModelScope.launch {
            carRepository.loadLocalCarsFromApi()
        }
    }
}