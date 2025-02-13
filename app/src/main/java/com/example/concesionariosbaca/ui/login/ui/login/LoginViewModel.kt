package com.example.concesionariosbaca.ui.login.ui.login

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.concesionariosbaca.ui.login.data.LoginRepository
import com.example.concesionariosbaca.ui.login.data.Result
import com.example.concesionariosbaca.ui.login.data.model.LoggedInUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {

    private val _loginResult = MutableLiveData<Result<LoggedInUser>>()
    val loginResult: LiveData<Result<LoggedInUser>> = _loginResult

    private val _isLoggedIn = MutableLiveData<Boolean>(false)
    private val isLoggedIn: LiveData<Boolean> = _isLoggedIn

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val result = loginRepository.login(username, password)
            _loginResult.postValue(result)
        }
    }

    fun logout() {
        viewModelScope.launch {
            loginRepository.logout()
            _isLoggedIn.postValue(false)
        }
    }
}
