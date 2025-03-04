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

@HiltViewModel
class CarDetailsViewModel @Inject constructor (
    private val carRepository: CarRepository,
    private val authRepository: AuthRepository
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

    suspend fun isUserLoggedIn(): Boolean {
        return authRepository.isUserLoggedIn()
    }

    fun buyCar(carId: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val token = authRepository.getJwtToken().firstOrNull()
                if (!token.isNullOrEmpty()) {
                    val userProfile = authRepository.getUserProfile(token)
                    userProfile?.let { user ->
                        Log.d("CarDetailsViewModel", "UserId: ${user.id}")

                        val customerId = authRepository.getCustomerIdByUserId(user.id.toIntOrNull() ?: -1)

                        if (customerId != null) {
                            val success = carRepository.updateCarOwner(carId, customerId)
                            if (success) {
                                _carDetails.value = carRepository.getCar(carId)
                                onSuccess()
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
                Log.e("CarRepository", "Error, no se ha podido comprar el coche: ${e.message}")
                onFailure("Error inesperado al comprar el coche")
            }
        }
    }


}