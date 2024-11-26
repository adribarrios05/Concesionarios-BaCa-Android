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
        val apiCars = apiService.getCars()

        return apiCars.ifEmpty {
            val localCars = carDao.readAll()
            if (localCars.isEmpty()) {
                return emptyList()
            }
            localCars
        }
    }

    suspend fun loadLocalCarsFromApi() {
        val carsFromApi = apiService.getCars()
        if (carsFromApi.isNotEmpty()) {
            carDao.createAll(carsFromApi)
        }
    }

}