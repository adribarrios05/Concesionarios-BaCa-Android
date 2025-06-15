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

/**
 * ViewModel encargado de gestionar el catálogo de coches disponibles.
 * Utiliza Hilt para la inyección de dependencias.
 */
@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val carRepository: CarRepository
): ViewModel() {

    /** Estado que contiene la lista de coches disponibles (sin cliente asociado). */
    private val _catalog = MutableStateFlow<List<CarEntity>?>(null)

    /** Flujo de solo lectura que expone el catálogo al UI. */
    val catalog: StateFlow<List<CarEntity>?>
        get() = _catalog.asStateFlow()

    init {
        loadLocalCarsFromApi()
        getCars()
    }

    /**
     * Recupera los coches sin cliente desde el repositorio
     * y los almacena en el flujo de estado.
     */
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

    /**
     * Carga los datos de los coches desde una API al almacenamiento local.
     */
    private fun loadLocalCarsFromApi() {
        viewModelScope.launch {
            carRepository.loadLocalCarsFromApi()
        }
    }
}
