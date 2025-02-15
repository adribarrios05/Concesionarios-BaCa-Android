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

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {


    suspend fun login(username: String, password: String): Boolean {
        return authRepository.loginUser(username, password)
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logoutUser()
        }
    }

    suspend fun isUserLoggedIn(): Boolean {
        return authRepository.isUserLoggedIn()
    }
}
