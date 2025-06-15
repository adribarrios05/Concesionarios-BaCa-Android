package com.example.concesionariosbaca.ui.login.ui.login

/**
 * Resultado de la autenticación: éxito (con detalles del usuario) o error (con mensaje).
 */
data class LoginResult(
    val success: LoggedInUserView? = null,
    val error: Int? = null
)
