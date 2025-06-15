package com.example.concesionariosbaca.ui.login.data

/**
 * Representa el resultado de una operación: éxito o error.
 */
sealed class Result<out T : Any> {

    /** Resultado exitoso con datos. */
    data class Success<out T : Any>(val data: T) : Result<T>()

    /** Resultado con error y excepción asociada. */
    data class Error(val exception: Exception) : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }

    /**
     * Ejecuta una acción si el resultado es `Success`.
     */
    fun <T : Any> Result<T>.onSuccess(action: (T) -> Unit): Result<T> {
        if (this is Result.Success) action(data)
        return this
    }

    /**
     * Ejecuta una acción si el resultado es `Error`.
     */
    fun <T : Any> Result<T>.onError(action: (Exception) -> Unit): Result<T> {
        if (this is Result.Error) action(exception)
        return this
    }
}
