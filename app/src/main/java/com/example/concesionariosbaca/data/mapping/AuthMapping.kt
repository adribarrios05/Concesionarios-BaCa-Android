package com.example.concesionariosbaca.data.mapping

import com.example.concesionariosbaca.data.entities.LoginResponse
import com.example.concesionariosbaca.data.entities.UserEntity
import com.example.concesionariosbaca.data.entities.UserResponse
import com.example.concesionariosbaca.ui.login.data.model.LoggedInUser

fun LoginResponse.toLoggedInUser(): LoggedInUser {
    return LoggedInUser(
        userId = user.id,
        username = user.username,
        email = user.email,
        profilePicture = user.profilePicture
    )
}

fun UserResponse.toLoggedInUser(): LoggedInUser {
    return LoggedInUser(
        userId = id,
        username = username,
        email = email,
        profilePicture = profilePicture
    )
}

fun LoggedInUser.toUserEntity(): UserEntity {
    return UserEntity(
        id= this.userId,
        username = this.username,
        email = this.email
    )
}

fun UserResponse.toUserEntity(): UserEntity {
    return UserEntity(
        id = this.id,
        username = this.username,
        email = this.email
    )
}
