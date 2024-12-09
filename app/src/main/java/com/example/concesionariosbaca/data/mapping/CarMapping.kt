package com.example.concesionariosbaca.data.mapping

import com.example.concesionariosbaca.data.entities.CarEntity

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
    val picture: PictureData?,
    val doors: Int,
    val customerId: Int?,
    val createdAt: String?,
    val updatedAt: String?,
    val publishedAt: String?
) {
    val pictureUrl: String?
        get() = mapPictureToUrl(picture)
}

data class PictureData(
    val data: PictureAttributes?
)

data class PictureAttributes(
    val id: Int,
    val attributes: PictureFormats
)

data class PictureFormats(
    val url: String?,
    val small: PictureFormatDetails?,
    val medium: PictureFormatDetails?,
    val thumbnail: PictureFormatDetails?
)

data class PictureFormatDetails(
    val url: String
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

fun mapPictureToUrl(picture: PictureData?): String? {
    return picture?.data?.attributes?.small?.url
        ?: picture?.data?.attributes?.medium?.url
        ?: picture?.data?.attributes?.url
}
fun CarData.toCarEntity(): CarEntity {
    return CarEntity(
        id = this.id.toString(),
        brand = this.attributes.brand,
        model = this.attributes.model,
        horsePower = this.attributes.horsePower,
        description = this.attributes.description,
        price = this.attributes.price,
        color = this.attributes.color,
        type = this.attributes.type,
        plate = this.attributes.plate,
        doors = this.attributes.doors,
        pictureUrl = this.attributes.pictureUrl,
        customerId = this.attributes.customerId
    )
}

