package com.example.concesionariosbaca.ui.carDetails

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.concesionariosbaca.data.entities.CarEntity
import com.example.concesionariosbaca.data.repository.AuthRepository
import com.example.concesionariosbaca.data.repository.CarRepository
import dagger.hilt.android.internal.Contexts
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * ViewModel que maneja la lógica de negocio para los detalles de un coche.
 * Se encarga de obtener, mostrar y comprar vehículos.
 */
@HiltViewModel
class CarDetailsViewModel @Inject constructor (
    private val carRepository: CarRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    /** Estado observable que contiene los detalles del coche actual. */
    private val _carDetails = MutableStateFlow<CarEntity?>(null)

    /** Exposición del estado de los detalles como flujo de solo lectura. */
    val carDetails: StateFlow<CarEntity?>
        get() = _carDetails.asStateFlow()

    /**
     * Carga los detalles de un coche específico desde el repositorio.
     * @param carId ID del coche a cargar.
     */
    fun getCarDetails(carId: String) {
        viewModelScope.launch {
            try {
                val car = carRepository.getCar(carId)
                _carDetails.value = car
            } catch (e: Exception) {
                _carDetails.value = null
            }
        }
    }

    /**
     * Carga datos desde la API al almacenamiento local.
     */
    private fun loadLocalCarsFromApi() {
        viewModelScope.launch {
            carRepository.loadLocalCarsFromApi()
        }
    }

    /**
     * Verifica si el usuario está autenticado.
     * @return `true` si está logueado, `false` si no.
     */
    suspend fun isUserLoggedIn(): Boolean {
        return authRepository.isUserLoggedIn()
    }

    /**
     * Ejecuta el proceso de compra de un coche por parte del usuario.
     *
     * @param carId ID del coche a comprar.
     * @param onSuccess Callback que se ejecuta si la compra tiene éxito.
     * @param onFailure Callback con mensaje si ocurre un error.
     */
    fun buyCar(carId: String, onSuccess: (CarEntity) -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val token = authRepository.getJwtToken().firstOrNull()
                if (!token.isNullOrEmpty()) {
                    val userProfile = authRepository.getUserProfile(token)
                    userProfile?.let { user ->
                        val customerId = authRepository.getCustomerIdByUserId(user.id.toIntOrNull() ?: -1)

                        if (customerId != null) {
                            val car = carRepository.getCar(carId)

                            val success = carRepository.updateCarOwner(
                                carId = car.id,
                                customerId = customerId,
                                pictureId = car.pictureId
                            )

                            if (success) {
                                val updatedCar = carRepository.getCar(carId)
                                _carDetails.value = updatedCar
                                onSuccess(updatedCar)
                            } else {
                                onFailure("No se pudo completar la compra")
                            }
                        } else {
                            onFailure("No se pudo obtener el ID del cliente")
                        }
                    }
                } else {
                    onFailure("No se pudo obtener la sesión del usuario")
                }
            } catch (e: Exception) {
                Log.e("CarRepository", "Error al comprar el coche: ${e.message}")
                onFailure("Error inesperado al comprar el coche")
            }
        }
    }
}
