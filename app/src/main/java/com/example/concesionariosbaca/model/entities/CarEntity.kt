package com.example.concesionariosbaca.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "car")
data class CarEntity(
    @PrimaryKey val id: String,
    val brand: String,
    val model: String,
    val price: Double,
    val description: String,
    val doors: Int,
    val horsePower: Int,
    val color: String,
    val type: String,
    val customerId: Int?
)
