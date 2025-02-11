package com.example.concesionariosbaca.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.concesionariosbaca.ui.login.data.LoginRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {

    private val _isAuthenticated = MutableLiveData<Boolean>()
    val isAuthenticated: LiveData<Boolean> = _isAuthenticated

    init {
        viewModelScope.launch {
            val token = loginRepository.getToken().firstOrNull()
            _isAuthenticated.postValue(token != null)
        }
    }

    fun updateAuthState(isLoggedIn: Boolean) {
        _isAuthenticated.postValue(isLoggedIn)
    }
}
