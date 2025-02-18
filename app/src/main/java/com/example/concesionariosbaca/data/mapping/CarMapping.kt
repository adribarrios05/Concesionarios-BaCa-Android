package com.example.concesionariosbaca.data.mapping

import com.example.concesionariosbaca.data.entities.CarEntity
import com.google.gson.*
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import kotlinx.serialization.Serializer
import java.lang.reflect.Type

data class CarResponse(
    val data: CarData,
    val meta: Meta
)

data class CarListResponse(
     @SerializedName("data") val cars: List<CarData>
)

data class CarRequest(
    val data: CarAttributes
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

fun CarEntity.toCarRequest(): CarRequest {
    return CarRequest(
        data = CarAttributes(
            brand = this.brand,
            model = this.model,
            horsePower = this.horsePower,
            description = this.description,
            price = this.price,
            color = this.color,
            type = this.type,
            plate = this.plate,
            doors = this.doors,
            picture = pictureUrl?.let { PictureData(PictureAttributes(0, PictureFormats(it, null, null, null))) },
            customerId = this.customerId,
        )
    )
}

