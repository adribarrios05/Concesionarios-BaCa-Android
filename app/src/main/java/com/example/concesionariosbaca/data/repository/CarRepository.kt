package com.example.concesionariosbaca.data.repository

import android.util.Log
import com.example.concesionariosbaca.data.api.ApiService
import com.example.concesionariosbaca.data.database.CarDao
import com.example.concesionariosbaca.data.entities.CarEntity
import com.example.concesionariosbaca.data.mapping.CarRequest
import com.example.concesionariosbaca.data.mapping.toCarEntity
import com.example.concesionariosbaca.data.mapping.toCarRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
                response.body()?.cars?.map { it.toCarEntity() } ?: emptyList()
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

    suspend fun addCar(car: CarEntity) {
        try {
            Log.d("addCar car", "Este es el coche que se enviará a Strapi: ${car.toCarRequest()}")

            val response = apiService.addCar(car.toCarRequest())

            if (response.isSuccessful) {
                response.body()?.data?.toCarEntity()?.let { createdCar ->
                    Log.d("addCar success", "Coche subido correctamente con ID: ${createdCar.id}")
                    carDao.create(createdCar)
                } ?: throw Exception("Error: Strapi no devolvió un ID válido")
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("addCar error", "Error al subir el coche. Código: ${response.code()}, Respuesta: $errorBody")
                throw Exception("Error al subir el coche. Código: ${response.code()}, Respuesta: $errorBody")
            }
        } catch (e: Exception) {
            Log.e("addCar exception", "Error en la solicitud: ${e.message}")
            throw Exception("Error en la solicitud: ${e.message}")
        }
    }

}
