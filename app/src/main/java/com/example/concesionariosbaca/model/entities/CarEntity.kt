package com.example.concesionariosbaca.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "car")
data class CarEntity(
    @PrimaryKey val id: String,
    val brand: String,
    val model: String,
    val horsePower: Int,
    val description: String,
    val color: String,
    val type: String,
    val price: Double,
    val plate: String,
    val pictureUrl: String?,
    val doors: Int,
    val customerId: Int?
)
