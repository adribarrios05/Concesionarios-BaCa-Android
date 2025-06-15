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

/**
 * Repositorio principal para gestionar el inicio de sesión del usuario.
 * Coordina llamadas a la API y almacenamiento local con DataStore.
 */
@Singleton
class LoginRepository @Inject constructor(
    private val apiService: ApiService,
    private val dataStoreManager: DataStoreManager
) {

    private var user: LoggedInUser? = null

    /** Devuelve un flujo con el token JWT almacenado. */
    fun getToken(): Flow<String?> = dataStoreManager.token

    /** Guarda el token JWT (por implementar). */
    suspend fun saveToken(token: String) {}

    /** Indica si el usuario tiene sesión iniciada. */
    suspend fun isUserLoggedIn(): Boolean =
        getToken().firstOrNull()?.isNotEmpty() ?: false

    /**
     * Inicia sesión con usuario y contraseña.
     *
     * @return Resultado con el usuario autenticado o error.
     */
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

    /** Elimina el token de sesión y borra el usuario en memoria. */
    suspend fun logout() {
        dataStoreManager.clearToken()
        user = null
    }

    /**
     * Obtiene el perfil del usuario actual a partir del token JWT.
     */
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

    /** Función pendiente para actualizar el perfil del usuario. */
    suspend fun updateUserProfile(token: String, username: String, email: String): Result<UserEntity>? {
        return null
    }
}
