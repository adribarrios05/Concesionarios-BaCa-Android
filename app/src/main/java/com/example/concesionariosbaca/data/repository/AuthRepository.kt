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
            //Log.d("LogIn", "Login exitoso")
            dataStoreManager.saveToken(token)
            true
        } else {
            //Log.e("LogIn", "Error el el login")
            false
        }
    }

    suspend fun logoutUser() {
        clearJwtToken()
        Log.d("Logout", "Sesión cerrada correctamente")
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

    suspend fun getCustomerIdByUserId(userId: Int): Int? {
        return try {
            val response = apiService.getCustomerByUserId(userId)
            if (response.isSuccessful) {
                val responseBody = response.body()
                Log.d("AuthRepository", "Respuesta completa: $responseBody")

                val customerId = responseBody?.data?.firstOrNull()?.id
                if (customerId != null) {
                    Log.d("AuthRepository", "Customer ID encontrado: $customerId")
                } else {
                    Log.e("AuthRepository", "No se encontró ningún Customer ID para el User ID: $userId")
                }

                customerId
            } else {
                Log.e("AuthRepository", "Error en la API: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error al obtener el Customer ID: ${e.message}")
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