package com.example.concesionariosbaca.model.mapping

data class CarResponse(
    val data: List<CarData>,
    val meta: Meta
)

data class CarData(
    val id: Int,
    val attributes: CarAttributes
)

data class CarAttributes(
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
    val customerId: Int?,
    val createdAt: String?,
    val updatedAt: String?,
    val publishedAt: String?
)

data class Meta(
    val pagination: Pagination
)

data class Pagination(
    val page: Int,
    val pageSize: Int,
    val pageCount: Int,
    val total: Int
)
