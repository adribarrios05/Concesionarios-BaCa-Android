package com.example.concesionariosbaca.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.concesionariosbaca.data.entities.CarEntity
import com.example.concesionariosbaca.data.entities.CustomerEntity
import com.example.concesionariosbaca.data.entities.UserEntity

@Database(
    entities =[CarEntity::class,
              CustomerEntity::class,
              UserEntity::class],
    version = 2,

)
abstract class ConcesionarioDatabase: RoomDatabase() {
    abstract fun carDao(): CarDao
    abstract fun customerDao(): CustomerDao
    abstract fun userDao(): UserDao
}