package com.example.concesionariosbaca.data.entities

data class RegisterUser(
    val email: String,
    val password: String,
    val username: String
)

data class RegisterUserResponse(
    val user: UserResponse,
    val jwt: String     // Token JWT generado
)

data class RegisterCustomer(
    val name: String,
    val surname: String,
    val dni: String,
    val phone: String,
    val age: String,
    val userId: Int
)

data class RegisterCustomerResponse(
    val customer: RegisterCustomer
)

data class LoginUser(
    val identifier: String,
    val password: String
)

data class LoginResponse(
    val jwt: String,
    val user: UserResponse
)

data class UserResponse(
    val id: String,
    val username: String,
    val email: String,
    val profilePicture: String?)

data class UpdateProfileRequest(
    val username: String,
    val email: String
)
