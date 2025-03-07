package com.example.concesionariosbaca.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.concesionariosbaca.data.entities.CarEntity
import com.example.concesionariosbaca.data.entities.CustomerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CarDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(car: CarEntity)

    @Insert
    suspend fun createAll(car: List<CarEntity>)

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