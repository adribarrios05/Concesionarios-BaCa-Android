package com.example.concesionariosbaca.ui.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.concesionariosbaca.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * ViewModel encargado de gestionar el registro de nuevos usuarios y clientes.
 */
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    /** Token JWT actual, observado desde DataStore. */
    val jwtToken: LiveData<String?> = authRepository.getJwtToken().asLiveData()

    /**
     * Registra al usuario y luego al cliente asociado.
     *
     * @return Resultado del registro completo (usuario + cliente).
     */
    suspend fun register(
        email: String,
        password: String,
        username: String,
        name: String,
        surname: String,
        dni: String,
        phone: String,
        age: String,
    ): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val userResponse = authRepository.registerUser(email, password, username)
                if (userResponse.isSuccessful) {
                    val userRegistered = userResponse.body()
                    if (userRegistered != null) {
                        val customerResponse = authRepository.registerCustomer(
                            token = userRegistered.jwt,
                            name = name,
                            surname = surname,
                            dni = dni,
                            phone = phone,
                            age = age,
                            userId = userRegistered.user.id.toInt()
                        )
                        Result.success(Unit)
                    } else {
                        Result.failure(Exception("Error: respuesta del servidor nula"))
                    }
                } else {
                    Result.failure(Exception("Error: ${userResponse.errorBody()?.string()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    /** Guarda el token JWT en DataStore. */
    fun saveJwtToken(token: String) {
        viewModelScope.launch {
            authRepository.saveJwtToken(token)
        }
    }

    /** Elimina el token JWT de DataStore. */
    fun clearJwtToken() {
        viewModelScope.launch {
            authRepository.clearJwtToken()
        }
    }
}

