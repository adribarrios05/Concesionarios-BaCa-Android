package com.example.concesionariosbaca.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.concesionariosbaca.data.entities.UserEntity
import com.example.concesionariosbaca.data.mapping.toUserEntity
import com.example.concesionariosbaca.ui.login.data.LoginRepository
import com.example.concesionariosbaca.ui.login.data.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {

    private val _loggedInUser = MutableLiveData<UserEntity?>()
    val loggedInUser: LiveData<UserEntity?> = _loggedInUser

    private val _updateResult = MutableLiveData<Result<Unit>>()
    val updateResult: LiveData<Result<Unit>> = _updateResult

    init {
        viewModelScope.launch {
            val user = loginRepository.getLoggedInUser()?.toUserEntity()
            _loggedInUser.postValue(user)
        }
    }

    fun updateUserProfile(username: String, email: String) {
        viewModelScope.launch {
            try {
                val token = loginRepository.getToken().firstOrNull()
                if (token != null) {
                    val result = loginRepository.updateUserProfile(token, username, email)
                    _updateResult.postValue(result)
                    if (result is Result.Success) {
                        _loggedInUser.postValue(UserEntity(
                            id = "",
                            username = username,
                            email = email
                        ))
                    }
                } else {
                    _updateResult.postValue(Result.Error(Exception("No hay token disponible")))
                }
            } catch (e: Exception) {
                _updateResult.postValue(Result.Error(e))
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            loginRepository.logout()
            _loggedInUser.postValue(null)
        }
    }
}
