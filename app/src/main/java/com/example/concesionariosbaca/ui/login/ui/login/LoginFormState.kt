package com.example.concesionariosbaca.ui.login.ui.login

/**
 * Estado de validación del formulario de inicio de sesión.
 *
 * @property usernameError ID del string de error del nombre de usuario, si aplica.
 * @property passwordError ID del string de error de contraseña, si aplica.
 * @property isDataValid Indica si el formulario es válido.
 */
data class LoginFormState(
    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)
