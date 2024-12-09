package com.example.concesionariosbaca.ui.login.data

sealed class Result<out T : Any> {

    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }

    fun <T : Any> Result<T>.onSuccess(action: (T) -> Unit): Result<T> {
        if (this is Result.Success) action(data)
        return this
    }

    fun <T : Any> Result<T>.onError(action: (Exception) -> Unit): Result<T> {
        if (this is Result.Error) action(exception)
        return this
    }
}