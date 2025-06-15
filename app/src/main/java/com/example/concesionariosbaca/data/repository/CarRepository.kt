package com.example.concesionariosbaca.data.repository

import android.util.Log
import com.example.concesionariosbaca.data.api.ApiService
import com.example.concesionariosbaca.data.database.CarDao
import com.example.concesionariosbaca.data.entities.CarEntity
import com.example.concesionariosbaca.data.mapping.CarAttributes
import com.example.concesionariosbaca.data.mapping.CarRequest
import com.example.concesionariosbaca.data.mapping.PictureAttributes
import com.example.concesionariosbaca.data.mapping.PictureData
import com.example.concesionariosbaca.data.mapping.PictureFormats
import com.example.concesionariosbaca.data.mapping.toCarEntity
import com.example.concesionariosbaca.data.mapping.toCarRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import javax.inject.Inject
import kotlin.math.log

class CarRepository @Inject constructor(
    private val apiService: ApiService,
    private val carDao: CarDao
) {

    // Obtener todos los coches
    suspend fun getCars(): List<CarEntity> {
        return try {
            val response = apiService.getCars()
            Log.d("CarRepository", "API Response: ${response.body()}")

            if (response.isSuccessful) {
                response.body()?.cars?.map { it.toCarEntity() }?.filter {
                    Log.d("CarRepository", "Filtrando coche: ${it.id}, CustomerID: ${it.customerId}")
                    it.customerId == null
                } ?: emptyList()
            } else {
                Log.e("CarRepository", "Error fetching cars from API: ${response.errorBody()}")
                return carDao.readAll() // Devuelve datos locales en caso de error
            }

        } catch (e: Exception) {
            Log.e("CarRepository", "Exception fetching cars: ${e.message}")
            carDao.readAll() // Devuelve datos locales si ocurre una excepción
        }
    }

    // Obtener un coche específico por su ID
    suspend fun getCar(carId: String): CarEntity {
        return try {
            val response = apiService.getCar(carId)
            if (response.isSuccessful) {
                response.body()?.data?.toCarEntity() ?: carDao.readOne(carId)
            } else {
                Log.e("CarRepository", "Error fetching car from API: ${response.errorBody()}")
                carDao.readOne(carId)
            }
        } catch (e: Exception) {
            Log.e("CarRepository", "Exception fetching car: ${e.message}")
            carDao.readOne(carId)
        }
    }

    // Cargar coches desde la API y almacenarlos localmente
    suspend fun loadLocalCarsFromApi() {
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.getCars()
                if (response.isSuccessful) {
                    response.body()?.cars?.map{ it.toCarEntity() }?.let {
                        carDao.createAll(it)
                        Log.d("CarRepository", "BBDD local cargada con datos de la API")
                    }
                } else {
                    Log.e("CarRepository", "Error fetching cars for local DB: ${response.errorBody()}")
                }
            } catch (e: Exception) {
                Log.e("CarRepository", "Exception updating local DB: ${e.message}")
            }
        }
    }

    suspend fun addCar(car: CarEntity, imageFile: File?) {
        try {
            val carJson = JSONObject().apply {
                put("brand", car.brand)
                put("model", car.model)
                put("horsePower", car.horsePower)
                put("description", car.description)
                put("color", car.color)
                put("type", car.type)
                put("price", car.price)
                put("plate", car.plate)
                put("doors", car.doors)
                put("customerId", car.customerId ?: JSONObject.NULL)
            }.toString()

            val carData = carJson.toRequestBody("application/json".toMediaTypeOrNull())

            val imagePart = imageFile?.let {
                val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), it)
                MultipartBody.Part.createFormData("files.picture", it.name, requestFile)
            }

            val response = apiService.addCar(carData, imagePart)

            if (response.isSuccessful) {
                val createdCar = response.body()?.data?.toCarEntity()
                if (createdCar != null) {
                    Log.d("addCar success", "Coche subido correctamente con ID: ${createdCar.id}")
                    carDao.create(createdCar)
                }
            } else {
                Log.e("addCar error", "Error al subir el coche. Código: ${response.code()}, Respuesta: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.e("addCar exception", "Error en la solicitud: ${e.message}")
        }
    }

    suspend fun updateCarOwner(carId: String, customerId: Int, pictureId: Int?): Boolean {
        Log.d("CarRepository", "PUT carId=$carId con customerId=$customerId y pictureId=$pictureId")

        return try {
            val car = getCar(carId)

            val dataMap = mutableMapOf<String, Any>(
                "brand" to car.brand,
                "model" to car.model,
                "horsePower" to car.horsePower,
                "description" to car.description,
                "price" to car.price,
                "color" to car.color,
                "type" to car.type,
                "plate" to car.plate,
                "doors" to car.doors,
                "customer" to customerId
            )

            if (pictureId != null) {
                dataMap["picture"] = mapOf(
                    "connect" to listOf(mapOf("id" to pictureId))
                )
            }

            val payload = mapOf("data" to dataMap)

            val response = apiService.putCarWithMap(carId, payload)
            Log.d("CarRepository", "PUT response: ${response.code()} - ${response.message()}")
            response.isSuccessful
        } catch (e: Exception) {
            Log.e("CarRepository", "Error en PUT del coche: ${e.message}")
            false
        }
    }


}
