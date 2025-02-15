package com.example.concesionariosbaca.ui.login.data

import androidx.datastore.dataStore
import com.example.concesionariosbaca.data.api.ApiService
import com.example.concesionariosbaca.data.entities.DataStoreManager
import com.example.concesionariosbaca.data.entities.LoginUser
import com.example.concesionariosbaca.data.entities.UpdateProfileRequest
import com.example.concesionariosbaca.data.entities.UserEntity
import com.example.concesionariosbaca.data.mapping.toLoggedInUser
import com.example.concesionariosbaca.data.mapping.toUserEntity
import com.example.concesionariosbaca.ui.login.data.model.LoggedInUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepository @Inject constructor(
    private val apiService: ApiService,
    private val dataStoreManager: DataStoreManager
) {

    private var user: LoggedInUser? = null

    fun getToken(): Flow<String?> {
        return dataStoreManager.token
    }

    suspend fun saveToken(token: String) {

    }

    suspend fun isUserLoggedIn(): Boolean {
        return getToken().firstOrNull()?.isNotEmpty() ?: false
    }

    suspend fun login(username: String, password: String): Result<LoggedInUser> {
        return try {
            val response = apiService.login(LoginUser(username, password))
            if (response.isSuccessful) {
                val loggedInUser = response.body()?.toLoggedInUser()
                    ?: throw Exception("Respuesta vacía del servidor")

                response.body()?.jwt?.let { jwt ->
                    dataStoreManager.saveToken(jwt)
                }

                user = loggedInUser
                Result.Success(loggedInUser)
            } else {
                Result.Error(Exception("Login fallido: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun logout() {
        dataStoreManager.clearToken()
        user = null
    }

    suspend fun getUserProfile(token: String): Result<UserEntity> {
        return try {
            val response = apiService.getCurrentUser("Bearer $token")
            if(response.isSuccessful){
                val user = response.body()?.toUserEntity() ?: throw Exception("Usuario no encontrado")
                Result.Success(user)
            } else{
                Result.Error(Exception("Error al obtener el usuario: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun updateUserProfile(token: String, username: String, email: String): Result<UserEntity>? {
        return null
    }
}
