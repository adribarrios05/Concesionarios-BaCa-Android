package com.example.concesionariosbaca.data.repository

import android.util.Log
import com.example.concesionariosbaca.data.api.ApiService
import com.example.concesionariosbaca.data.database.CarDao
import com.example.concesionariosbaca.data.entities.CarEntity
import com.example.concesionariosbaca.data.mapping.CarData
import com.example.concesionariosbaca.data.mapping.CarResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CarRepository @Inject constructor(
    private val apiService: ApiService,
    private val carDao: CarDao
){

    suspend fun getCars(): List<CarEntity> {
        return try {
            val apiResponse: CarResponse = apiService.getCars()
            if (apiResponse.data.isNotEmpty()) {
                apiResponse.data.map { carData ->
                    mapCarDataToEntity(carData)
                }
            } else {
                carDao.readAll() // Si la API está vacía, devuelve datos locales
            }
        } catch (e: Exception) {
            carDao.readAll() // En caso de error, devuelve datos locales
        }
    }

    suspend fun getCar(carId: String): CarEntity {
        return try {
            val apiResponse: CarResponse = apiService.getCar(carId)
            if(apiResponse.data.isNotEmpty()){
                val car = mapCarDataToEntity(apiResponse.data.first())
                Log.d("CarRepository", "Car fetched from API: $car")
                car
            } else {
                carDao.readOne(carId).also {
                    Log.d("CarRepository", "Car fetched from local DB: $it")
                }
            }
        } catch (e: Exception){
            carDao.readOne(carId).also {
                Log.e("CarRepository", "Error fetching car: ${e.message}")
            }
        }

    }


    suspend fun loadLocalCarsFromApi() {
        withContext(Dispatchers.IO) {
            try {
                val apiResponse: CarResponse = apiService.getCars()
                if (apiResponse.data.isNotEmpty()) {
                    val carEntities = apiResponse.data.map { carData ->
                        mapCarDataToEntity(carData)
                    }
                    carDao.createAll(carEntities) // Guarda en la base local
                } else {

                }
            } catch (e: Exception) {
                Log.e("CarRepository", "Error loading cars from API: ${e.message}")
            }
        }
    }
    private fun mapCarDataToEntity(carData: CarData): CarEntity {
        return CarEntity(
            id = carData.id.toString(),
            brand = carData.attributes.brand,
            model = carData.attributes.model,
            horsePower = carData.attributes.horsePower,
            description = carData.attributes.description,
            color = carData.attributes.color,
            type = carData.attributes.type,
            price = carData.attributes.price,
            plate = carData.attributes.plate,
            pictureUrl = carData.attributes.pictureUrl,
            doors = carData.attributes.doors,
            customerId = carData.attributes.customerId

        )
    }
}