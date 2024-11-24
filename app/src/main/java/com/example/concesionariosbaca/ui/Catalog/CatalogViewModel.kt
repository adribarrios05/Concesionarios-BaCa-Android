package com.example.concesionariosbaca.ui.Catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.concesionariosbaca.model.entities.CarEntity
import com.example.concesionariosbaca.model.repository.CarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val carRepository: CarRepository
): ViewModel() {

    fun getCars() = liveData(Dispatchers.IO){
        try{
            val cars = carRepository.getCars()
            emit(cars)
        } catch (e: Exception){
            emit(emptyList<CarEntity>())
        }
    }

    fun loadLocalCarsFromApi() {
        viewModelScope.launch {
            carRepository.loadLocalCarsFromApi()
        }
    }
}