package com.example.concesionariosbaca.model.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.concesionariosbaca.model.entities.CarEntity
import com.example.concesionariosbaca.model.entities.AppUserEntity
import com.example.concesionariosbaca.model.entities.CustomerEntity

import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerDao {
    @Insert
    suspend fun create(customer: CustomerEntity)

    @Update
    suspend fun update(customer: CustomerEntity)

    @Delete
    suspend fun delete(customer: CustomerEntity)

    @Query("SELECT * FROM customer")
    suspend fun readAll(): List<CustomerEntity>

    @Query("SELECT * FROM customer WHERE id = :id")
    suspend fun readOne(id: String): CustomerEntity

    @Query("SELECT * FROM customer")
    fun observeAll(): Flow<CustomerEntity>

    @Query("SELECT * FROM customer LEFT OUTER JOIN car ON customer.id = car.customerId")
    suspend fun readOwnerOfCar():Map<CustomerEntity, List<CarEntity>>

    @Query("SELECT * FROM customer LEFT OUTER JOIN appUser ON customer.userId = appUser.id")
    suspend fun readUserOfCustomer(): Map<CustomerEntity, AppUserEntity>
}