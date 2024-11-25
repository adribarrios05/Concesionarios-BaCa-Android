package com.example.concesionariosbaca.ui.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.concesionariosbaca.model.entities.CarEntity
import com.example.concesionariosbaca.model.repository.CarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val carRepository: CarRepository
): ViewModel() {

    private val _uiState = MutableStateFlow<List<CarEntity>?>(null)
    val uiState: StateFlow<List<CarEntity>?>
        get() = _uiState.asStateFlow()

    init{
        getCars()
    }

    private fun getCars() {
        viewModelScope.launch {
            try{
            val cars = carRepository.getCars()
            _uiState.value = cars
            } catch (e: Exception){
                _uiState.value = emptyList()
            }
        }
    }

    fun loadLocalCarsFromApi() {
        viewModelScope.launch {
            carRepository.loadLocalCarsFromApi()
        }
    }
}