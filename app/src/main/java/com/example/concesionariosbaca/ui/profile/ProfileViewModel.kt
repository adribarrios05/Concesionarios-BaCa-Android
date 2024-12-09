package com.example.concesionariosbaca.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.concesionariosbaca.ui.login.data.LoginRepository
import com.example.concesionariosbaca.ui.login.data.model.LoggedInUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.concesionariosbaca.ui.login.data.Result
import kotlinx.coroutines.flow.firstOrNull


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {

    val loggedInUser: LiveData<LoggedInUser?> = liveData {
        emit(loginRepository.getLoggedInUser())
    }

    suspend fun isUserLoggedIn(): Boolean {
        return loginRepository.getToken().firstOrNull() != null
    }

    fun updateUserProfile(username: String, email: String): LiveData<Result<Unit>> {
        val resultLiveData = MutableLiveData<Result<Unit>>()

        viewModelScope.launch {
            try {
                val token = loginRepository.getToken().firstOrNull()

                if (token != null) {
                    val result = loginRepository.updateUserProfile(token, username, email)
                    resultLiveData.postValue(result)
                } else {
                    resultLiveData.postValue(Result.Error(Exception("No hay token disponible")))
                }
            } catch (e: Exception) {
                resultLiveData.postValue(Result.Error(e))
            }
        }
        return resultLiveData
    }

    fun logout() {
        viewModelScope.launch {
            loginRepository.logout()
        }
    }
}

