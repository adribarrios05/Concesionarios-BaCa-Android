package com.example.concesionariosbaca.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.concesionariosbaca.data.entities.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert
    suspend fun create(user: UserEntity)

    @Update
    suspend fun update(user: UserEntity)

    @Delete
    suspend fun delete(user: UserEntity)

    @Query("SELECT * FROM user")
    suspend fun readAll(): List<UserEntity>

    @Query("SELECT * FROM user WHERE id = :id")
    suspend fun readOne(id: String): UserEntity

    @Query("SELECT * FROM user")
    fun observeAll(): Flow<UserEntity>

    

}