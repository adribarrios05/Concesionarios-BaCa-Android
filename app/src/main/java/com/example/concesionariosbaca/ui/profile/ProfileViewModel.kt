package com.example.concesionariosbaca.ui.profile

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.concesionariosbaca.data.entities.UserEntity
import com.example.concesionariosbaca.data.mapping.toUserEntity
import com.example.concesionariosbaca.data.repository.AuthRepository
import com.example.concesionariosbaca.ui.login.data.LoginRepository
import com.example.concesionariosbaca.ui.login.data.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loggedInUser = MutableLiveData<UserEntity?>()
    val loggedInUser: LiveData<UserEntity?> = _loggedInUser

    init {
        viewModelScope.launch {
            val token = authRepository.getJwtToken().firstOrNull()
            if (!token.isNullOrEmpty()) {
                _loggedInUser.postValue(authRepository.getUserProfile(token))
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logoutUser()
            _loggedInUser.postValue(null)
        }
    }
}

