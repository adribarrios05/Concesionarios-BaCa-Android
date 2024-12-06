package com.example.concesionariosbaca.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.concesionariosbaca.model.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    fun register(
        email: String,
        password: String,
        username: String,
        name: String,
        surname: String,
        dni: String,
        phone: String,
        age: String,
        carRentId: Int?
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
                carRentId = carRentId,
                userId = userRegistered.userId.toInt()
            )

            emit(Result.success(customer))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

}