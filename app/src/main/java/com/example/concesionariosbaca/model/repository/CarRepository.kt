package com.example.concesionariosbaca.model.repository

import com.example.concesionariosbaca.model.api.ApiService
import com.example.concesionariosbaca.model.database.CarDao
import com.example.concesionariosbaca.model.entities.CarEntity
import javax.inject.Inject

class CarRepository @Inject constructor(
    private val apiService: ApiService,
    private val carDao: CarDao
){

    suspend fun getCars(): List<CarEntity> {
        val localCars = carDao.readAll()

        return localCars.ifEmpty {
            val apiCars = apiService.getCars()
            if (apiCars.isEmpty()) {
                return emptyList()
            }
            apiCars
        }
    }

    suspend fun loadLocalCarsFromApi() {
        val carsFromApi = apiService.getCars()
        if (carsFromApi.isNotEmpty()) {
            carDao.createAll(carsFromApi)
        }
    }

}