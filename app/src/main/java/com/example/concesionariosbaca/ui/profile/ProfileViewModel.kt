package com.example.concesionariosbaca.ui.profile

import androidx.lifecycle.LifecycleOwner
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

    private val _isAuthenticated = MutableLiveData<Boolean>(false)
    val isAuthenticated: LiveData<Boolean> = _isAuthenticated

    init {
        viewModelScope.launch {
            val user = loginRepository.getLoggedInUser()?.toUserEntity()
            _loggedInUser.postValue(user)
            _isAuthenticated.postValue(user != null)
        }
    }

    fun observeAuthState(lifecycleOwner: LifecycleOwner, onAuthStateChanged: (Boolean) -> Unit) {
        isAuthenticated.observe(lifecycleOwner) { isLoggedIn ->
            onAuthStateChanged(isLoggedIn)
        }
    }

    fun updateUserProfile(username: String, email: String) {
        viewModelScope.launch {
            val token = loginRepository.getToken().firstOrNull()
            if (token != null) {
                val result = loginRepository.updateUserProfile(token, username, email)
                if (result is Result.Success) {
                    _loggedInUser.postValue(UserEntity(id = "", username, email))
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            loginRepository.logout()
            _loggedInUser.postValue(null)
            _isAuthenticated.postValue(false)
        }
    }
}
