package com.example.concesionariosbaca.ui.login.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.concesionariosbaca.R
import com.example.concesionariosbaca.ui.login.data.LoginRepository
import com.example.concesionariosbaca.ui.login.data.Result
import com.example.concesionariosbaca.ui.login.data.model.LoggedInUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult
    val isLoggedIn: Flow<Boolean> = loginRepository.getToken().map { token ->
        !token.isNullOrEmpty()
    }

    suspend fun login(username: String, password: String): Result<LoggedInUser> {
        return loginRepository.login(username, password)

        /*viewModelScope.launch {
            val result = loginRepository.login(username, password)
            _loginResult.value = when (result) {
                is Result.Success -> {
                    LoginResult(success = LoggedInUserView(username = result.data.username))
                }
                is Result.Error -> {
                    LoginResult(error = R.string.login_failed)
                }
            }
        }*/
    }

    fun isLoggedIn(): Boolean {
        return loginRepository.isLoggedIn
    }

    fun logout() {
        viewModelScope.launch {
            loginRepository.logout()
        }
    }
}
