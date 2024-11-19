package com.example.concesionariosbaca.data.local.car

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.concesionariosbaca.data.local.customer.CustomerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CarDao {

    @Insert
    suspend fun create(car: CarEntity)

    @Update
    suspend fun update(car: CarEntity)

    @Delete
    suspend fun delete(car: CarEntity)

    @Query("SELECT * FROM car")
    suspend fun readAll(): List<CarEntity>

    @Query("SELECT * FROM car WHERE id = :id")
    suspend fun readOne(id: String): CarEntity

    @Query("SELECT * FROM car")
    fun observeAll(): Flow<CarEntity>

    @Query("SELECT * FROM car JOIN customer ON car.customerId = customer.id")
    suspend fun readCustomerCars():Map<CarEntity, CustomerEntity>

}