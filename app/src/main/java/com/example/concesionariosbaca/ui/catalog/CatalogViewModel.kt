package com.example.concesionariosbaca.ui.catalog

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
class CatalogViewModel @Inject constructor(
    private val carRepository: CarRepository
): ViewModel() {

    private val _catalog = MutableStateFlow<List<CarEntity>?>(null)
    val catalog: StateFlow<List<CarEntity>?>
        get() = _catalog.asStateFlow()

    init{
        loadLocalCarsFromApi()
        getCars()
    }

    private fun getCars() {
        viewModelScope.launch {
            try {
                val cars = carRepository.getCars().filter { it.customerId == null }
                _catalog.value = cars
            } catch (e: Exception) {
                _catalog.value = emptyList()
            }
        }
    }

    private fun loadLocalCarsFromApi() {
        viewModelScope.launch {
            carRepository.loadLocalCarsFromApi()
        }
    }
}