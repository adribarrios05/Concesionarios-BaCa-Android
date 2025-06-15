package com.example.concesionariosbaca.ui.login.data.model

/**
 * Datos del usuario autenticado que se almacenan tras el login.
 *
 * @property userId ID único del usuario.
 * @property username Nombre de usuario.
 * @property email Correo electrónico del usuario.
 * @property profilePicture URL de la foto de perfil (opcional).
 */
data class LoggedInUser(
    val userId: String,
    val username: String,
    val email: String,
    val profilePicture: String?
)
