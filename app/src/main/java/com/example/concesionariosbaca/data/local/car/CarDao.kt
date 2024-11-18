package com.example.concesionariosbaca.data.local.car

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

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

}