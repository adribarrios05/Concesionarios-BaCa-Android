package com.example.concesionariosbaca.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.concesionariosbaca.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    val jwtToken: LiveData<String?> = authRepository.getJwtToken().asLiveData()

    fun register(
        email: String,
        password: String,
        username: String,
        name: String,
        surname: String,
        dni: String,
        phone: String,
        age: String,
    ) = liveData {
        try {
            val userRegistered = authRepository.registerUser(email, password, username)

            val customer = authRepository.registerCustomer(
                token = userRegistered.jwt,
                name = name,
                surname = surname,
                dni = dni,
                phone = phone,
                age = age,
                carRentId = null,
                userId = userRegistered.userId.toInt()
            )

            emit(Result.success(customer))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun saveJwtToken(token: String) {
        viewModelScope.launch {
            authRepository.saveJwtToken(token)
        }
    }

    fun clearJwtToken() {
        viewModelScope.launch {
            authRepository.clearJwtToken()
        }
    }
}