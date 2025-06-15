package com.example.concesionariosbaca.ui.login.ui.login

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.concesionariosbaca.data.entities.DataStoreManager
import com.example.concesionariosbaca.data.repository.AuthRepository
import com.example.concesionariosbaca.ui.login.data.LoginRepository
import com.example.concesionariosbaca.ui.login.data.Result
import com.example.concesionariosbaca.ui.login.data.model.LoggedInUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel que maneja la lógica de autenticación del usuario.
 * Conecta con el repositorio de autenticación para iniciar/cerrar sesión.
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    /**
     * Intenta iniciar sesión con las credenciales proporcionadas.
     * @return true si la autenticación fue exitosa.
     */
    suspend fun login(username: String, password: String): Boolean {
        return authRepository.loginUser(username, password)
    }

    /** Cierra sesión del usuario actual. */
    fun logout() {
        viewModelScope.launch {
            authRepository.logoutUser()
        }
    }

    /** Verifica si hay una sesión de usuario activa. */
    suspend fun isUserLoggedIn(): Boolean {
        return authRepository.isUserLoggedIn()
    }
}

