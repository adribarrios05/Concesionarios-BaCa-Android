package com.example.concesionariosbaca.data.repository

import android.util.Log
import com.example.concesionariosbaca.data.api.ApiService
import com.example.concesionariosbaca.data.entities.CustomerEntity
import com.example.concesionariosbaca.data.entities.DataStoreManager
import com.example.concesionariosbaca.data.entities.LoginResponse
import com.example.concesionariosbaca.data.entities.LoginUser
import com.example.concesionariosbaca.data.entities.RegisterCustomer
import com.example.concesionariosbaca.data.entities.RegisterCustomerResponse
import com.example.concesionariosbaca.data.entities.RegisterUser
import com.example.concesionariosbaca.data.entities.RegisterUserResponse
import com.example.concesionariosbaca.data.entities.UserEntity
import com.example.concesionariosbaca.data.mapping.toUserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import retrofit2.Response
import javax.inject.Inject

data class RegisterCustomerRequest(
    val data: CustomerData
)

data class CustomerData(
    val name: String,
    val surname: String,
    val dni: String,
    val phone: String,
    val age: String,
    val user: UserId
)

data class UserId(
    val id: Int
)


class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val dataStoreManager: DataStoreManager
) {

    suspend fun registerUser(email: String, password: String, username: String): Response<RegisterUserResponse> {
        return apiService.registerUser(RegisterUser(email, password, username))
    }

    suspend fun registerCustomer(
        token: String,
        name: String,
        surname: String,
        dni: String,
        phone: String,
        age: String,
        userId: Int
    ): Response<RegisterCustomerResponse> {
        val customerRequest = RegisterCustomerRequest(
            data = CustomerData(
                name = name,
                surname = surname,
                dni = dni,
                phone = phone,
                age = age,
                user = UserId(id = userId)
            )
        )
        return apiService.registerCustomer("Bearer $token", customerRequest)
    }

    suspend fun loginUser(identifier: String, password: String): Boolean{
        val response = apiService.login(LoginUser(identifier, password))

        return if (response.isSuccessful && response.body() != null) {
            val token = response.body()!!.jwt
            dataStoreManager.saveToken(token)
            true
        } else {
            false
        }
    }

    suspend fun logoutUser() {
        clearJwtToken()
    }

    suspend fun isUserLoggedIn(): Boolean {
        return getJwtToken().firstOrNull()?.isNotEmpty() ?: false
    }

    suspend fun getUserProfile(token: String): UserEntity? {
        return try {
            val response = apiService.getCurrentUser("Bearer $token")
            if (response.isSuccessful) {
                response.body()?.toUserEntity()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
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