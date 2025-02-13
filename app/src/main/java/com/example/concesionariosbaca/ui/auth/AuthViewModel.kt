package com.example.concesionariosbaca.ui.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.concesionariosbaca.R
import com.example.concesionariosbaca.ui.login.data.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {

    private val _isAuthenticated = MutableLiveData<Boolean>(false)
    val isAuthenticated: LiveData<Boolean> = _isAuthenticated

    init {
        checkAuthState()
    }

    private fun checkAuthState() {
        viewModelScope.launch {
            val token = loginRepository.getToken().firstOrNull()
            _isAuthenticated.postValue(!token.isNullOrEmpty())
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val result = loginRepository.login(username, password)
            if (result is com.example.concesionariosbaca.ui.login.data.Result.Success) {
                Log.d("Auth login", "Login exitoso")
                _isAuthenticated.postValue(true)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            loginRepository.logout()
            _isAuthenticated.postValue(false)
        }
    }
}
