package com.example.concesionariosbaca.ui.login.data

import com.example.concesionariosbaca.data.api.ApiService
import com.example.concesionariosbaca.data.entities.LoginResponse
import com.example.concesionariosbaca.data.entities.LoginUser
import com.example.concesionariosbaca.data.mapping.toLoggedInUser
import com.example.concesionariosbaca.ui.login.data.model.LoggedInUser
import retrofit2.Response
import java.io.IOException
/**
 * Fuente de datos para la autenticación de usuarios.
 * Utiliza `ApiService` para realizar llamadas de red al backend.
 */
class LoginDataSource(private val apiService: ApiService) {

    /**
     * Intenta iniciar sesión con las credenciales proporcionadas.
     *
     * @param username Nombre de usuario o email.
     * @param password Contraseña.
     * @return Resultado de la operación, ya sea `Success` o `Error`.
     */
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

    /** Cierra sesión localmente (si fuera necesario implementar lógica adicional). */
    fun logout() {}
}

