package com.example.concesionariosbaca.data.entities

data class RegisterUser(
    val email: String,
    val password: String,
    val username: String
)

data class RegisterUserResponse(
    val userId: String,
    val email: String,
    val username: String,
    val jwt: String     // Token JWT generado
)

data class RegisterCustomer(
    val name: String,
    val surname: String,
    val dni: String,
    val phone: String,
    val age: String,
)

data class LoginUser(
    val identifier: String,
    val password: String
)

data class LoginResponse(
    val jwt: String,
    val user: UserEntity
)
