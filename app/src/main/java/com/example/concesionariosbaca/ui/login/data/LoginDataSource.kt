package com.example.concesionariosbaca.ui.login.data

import com.example.concesionariosbaca.data.api.ApiService
import com.example.concesionariosbaca.data.entities.LoginResponse
import com.example.concesionariosbaca.data.entities.LoginUser
import com.example.concesionariosbaca.data.mapping.toLoggedInUser
import com.example.concesionariosbaca.ui.login.data.model.LoggedInUser
import retrofit2.Response
import java.io.IOException

class LoginDataSource(private val apiService: ApiService) {

    suspend fun login(username: String, password: String): Result<LoggedInUser> {
        return try {
            val response: Response<LoginResponse> = apiService.login(LoginUser(username, password))
            if (response.isSuccessful) {
                val user = response.body()?.toLoggedInUser()
                    ?: return Result.Error(IOException("Usuario no encontrado"))
                Result.Success(user)
            } else {
                Result.Error(Exception("Error de autenticación: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Error(IOException("Error de red", e))
        }
    }

    fun logout() {
        // Lógica para revocar la autenticación (limpieza local)
    }
}
