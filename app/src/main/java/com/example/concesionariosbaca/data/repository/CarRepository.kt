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

    suspend fun updateCarOwner(carId: String, customerId: Int): Boolean {
        return try {
            // Obtener el coche actual antes de actualizar
            val car = getCar(carId)

            // Guardar la imagen antes de modificar el coche
            val existingPicture = car.toCarRequest().data.picture ?: car.pictureUrl?.let {
                PictureData(
                    data = PictureAttributes(
                        id = 0,
                        attributes = PictureFormats(
                            url = it,
                            small = null,
                            medium = null,
                            thumbnail = null
                        )
                    )
                )
            }

            // Mantener la imagen y actualizar solo customerId
            val updatedCarRequest = car.toCarRequest().copy(
                data = car.toCarRequest().data.copy(
                    customer = customerId,
                    picture = existingPicture // Restaurar la imagen guardada previamente
                )
            )

            Log.d("CarRepository", "Enviando solicitud a Strapi para actualizar coche: $updatedCarRequest")

            // Enviar actualización a Strapi
            val response = apiService.updateCar(carId, updatedCarRequest)

            Log.d("CarRepository", "Response Code: ${response.code()} - ${response.message()} - Body: ${response.errorBody()?.string()}")

            response.isSuccessful
        } catch (e: Exception) {
            Log.e("CarRepository", "Error al actualizar el coche con el cliente: ${e.message}")
            false
        }
    }
}
