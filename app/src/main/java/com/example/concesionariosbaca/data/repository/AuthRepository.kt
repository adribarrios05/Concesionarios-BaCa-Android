package com.example.concesionariosbaca.data.repository

import com.example.concesionariosbaca.data.api.ApiService
import com.example.concesionariosbaca.data.entities.DataStoreManager
import com.example.concesionariosbaca.data.entities.LoginResponse
import com.example.concesionariosbaca.data.entities.LoginUser
import com.example.concesionariosbaca.data.entities.RegisterCustomer
import com.example.concesionariosbaca.data.entities.RegisterUser
import com.example.concesionariosbaca.data.entities.RegisterUserResponse
import kotlinx.coroutines.flow.Flow
import org.intellij.lang.annotations.Identifier
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val dataStoreManager: DataStoreManager
) {

    suspend fun registerUser(email: String, password: String, username: String): RegisterUserResponse {
        return apiService.registerUser(RegisterUser(email, password, username))
    }

    suspend fun registerCustomer(token: String, name: String, surname: String, dni: String, phone: String,
                                 age: String) {
        apiService.registerCustomer(
            "Bearer $token",
            RegisterCustomer(name, surname, dni, phone, age)
        )
    }

    suspend fun loginUser(identifier: String, password: String): LoginResponse{
        return apiService.login(LoginUser(identifier, password))
    }

    suspend fun saveJwtToken(jwt: String) {
        dataStoreManager.saveToken(jwt)
    }

    fun getJwtToken(): Flow<String?> {
        return dataStoreManager.token
    }

    suspend fun clearJwtToken() {
        dataStoreManager.clearToken()
    }
}