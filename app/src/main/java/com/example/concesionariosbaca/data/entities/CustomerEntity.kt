package com.example.concesionariosbaca.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customer")
data class CustomerEntity(
    @PrimaryKey val id: String,
    val name: String,
    val surname: String,
    val dni: String,
    val phone: String,
    val age: String,
    val carRentId: Int?,
    val userId: Int?
)


