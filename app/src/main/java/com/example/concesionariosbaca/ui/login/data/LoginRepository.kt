package com.example.concesionariosbaca.ui.login.data

import com.example.concesionariosbaca.data.api.ApiService
import com.example.concesionariosbaca.data.entities.DataStoreManager
import com.example.concesionariosbaca.data.entities.LoginUser
import com.example.concesionariosbaca.data.entities.UpdateProfileRequest
import com.example.concesionariosbaca.data.mapping.toLoggedInUser
import com.example.concesionariosbaca.ui.login.data.model.LoggedInUser
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepository @Inject constructor(
    private val apiService: ApiService,
    private val dataStoreManager: DataStoreManager
) {

    private var user: LoggedInUser? = null

    suspend fun isUserLoggedIn(): Boolean {
        val token = dataStoreManager.token.firstOrNull()
        return !token.isNullOrEmpty()
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
}
