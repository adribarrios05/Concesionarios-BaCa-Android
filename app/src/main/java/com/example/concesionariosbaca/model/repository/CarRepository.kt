package com.example.concesionariosbaca.model.repository

import com.example.concesionariosbaca.model.api.ApiService
import com.example.concesionariosbaca.model.database.CarDao
import com.example.concesionariosbaca.model.entities.CarEntity
import com.example.concesionariosbaca.model.mapping.CarData
import com.example.concesionariosbaca.model.mapping.CarResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
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
                mapCarDataToEntity(apiResponse.data.first())
            } else {
                carDao.readOne(carId)
            }
        } catch (e: Exception){
            carDao.readOne(carId)
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
                }
            } catch (e: Exception) {
                // TODO() manejar la excepción
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