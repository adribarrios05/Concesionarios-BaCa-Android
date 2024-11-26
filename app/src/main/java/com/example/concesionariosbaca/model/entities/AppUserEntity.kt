package com.example.concesionariosbaca.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "appUser")
data class AppUserEntity(
    @PrimaryKey val id: String,
    val username: String,
    val email: String,
    val password: String
)