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

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    val jwtToken: LiveData<String?> = authRepository.getJwtToken().asLiveData()

    suspend fun register(
        email: String,
        password: String,
        username: String,
        name: String,
        surname: String,
        dni: String,
        phone: String,
        age: String,
    ): Result<Unit>  {
        return withContext(Dispatchers.IO) {
            try {
                val userResponse = authRepository.registerUser(email, password, username)
                if (userResponse.isSuccessful) {
                    val userRegistered = userResponse.body()
                    if (userRegistered != null) {
                        authRepository.registerCustomer(
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


    fun saveJwtToken(token: String) {
        viewModelScope.launch {
            authRepository.saveJwtToken(token)
        }
    }

    fun clearJwtToken() {
        viewModelScope.launch {
            authRepository.clearJwtToken()
        }
    }
}
