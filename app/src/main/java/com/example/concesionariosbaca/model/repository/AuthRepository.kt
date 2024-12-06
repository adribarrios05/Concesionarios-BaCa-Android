package com.example.concesionariosbaca.model.repository

import com.example.concesionariosbaca.model.api.ApiService
import com.example.concesionariosbaca.model.entities.CustomerEntity
import com.example.concesionariosbaca.model.entities.RegisterCustomer
import com.example.concesionariosbaca.model.entities.RegisterUser
import com.example.concesionariosbaca.model.entities.RegisterUserResponse
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val apiService: ApiService,
) {

    suspend fun registerUser(email: String, password: String, username: String): RegisterUserResponse {
        return apiService.registerUser(RegisterUser(email, password, username))
    }

    suspend fun registerCustomer(token: String, name: String, surname: String, dni: String, phone: String,
                                 age: String, carRentId: Int?, userId: Int?) {
        apiService.registerCustomer(
            "Bearer $token",
            RegisterCustomer(name, surname, dni, phone, age, carRentId, userId)
        )
    }
}