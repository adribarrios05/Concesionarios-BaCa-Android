package com.example.concesionariosbaca.model.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.concesionariosbaca.model.entities.AppUserEntity
import com.example.concesionariosbaca.model.entities.CustomerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppUserDao {

    @Insert
    suspend fun create(user: AppUserEntity)

    @Update
    suspend fun update(user: AppUserEntity)

    @Delete
    suspend fun delete(user: AppUserEntity)

    @Query("SELECT * FROM appUser")
    suspend fun readAll(): List<AppUserEntity>

    @Query("SELECT * FROM appUser WHERE id = :id")
    suspend fun readOne(id: String): AppUserEntity

    @Query("SELECT * FROM appUser")
    fun observeAll(): Flow<AppUserEntity>

    

}