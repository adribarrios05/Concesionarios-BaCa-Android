package com.example.concesionariosbaca.ui.login.data

import com.example.concesionariosbaca.data.api.ApiService
import com.example.concesionariosbaca.data.entities.DataStoreManager
import com.example.concesionariosbaca.data.entities.LoginUser
import com.example.concesionariosbaca.data.entities.UpdateProfileRequest
import com.example.concesionariosbaca.data.entities.UserEntity
import com.example.concesionariosbaca.data.mapping.toLoggedInUser
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

    val isLoggedIn: Boolean
        get() = user != null

    suspend fun login(username: String, password: String): Result<LoggedInUser> {
        return try {
            val response = apiService.login(LoginUser(username, password))
            if (response.isSuccessful) {
                val loggedInUser = response.body()?.toLoggedInUser()
                    ?: throw Exception("Respuesta vacía del servidor")

                response.body()?.jwt?.let { jwt ->
                    dataStoreManager.saveToken(jwt)
                }

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
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
    }

    fun getToken(): Flow<String?> {
        return dataStoreManager.token
    }

    suspend fun getLoggedInUser(): LoggedInUser? {
        val token = dataStoreManager.token.firstOrNull() ?: return null

        return try {
            val response = apiService.getCurrentUser("Bearer $token")
            if (response.isSuccessful) {
                response.body()?.toLoggedInUser()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateUserProfile(token: String, username: String, email: String): Result<Unit> {
        return try {
            val updateRequest = UpdateProfileRequest(username, email)
            val response = apiService.updateProfile("Bearer $token", updateRequest)

            if (response.isSuccessful) {
                user = user?.copy(username = username, email = email) // Actualiza el usuario en memoria
                Result.Success(Unit)
            } else {
                Result.Error(Exception("Error actualizando el perfil: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
