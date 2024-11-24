package com.example.concesionariosbaca.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.concesionariosbaca.model.entities.AppUserEntity
import com.example.concesionariosbaca.model.entities.CarEntity
import com.example.concesionariosbaca.model.entities.CustomerEntity

@Database(
    entities =[CarEntity::class,
              CustomerEntity::class,
              AppUserEntity::class],
    version = 1
)
abstract class ConcesionarioDatabase: RoomDatabase() {
    abstract fun carDao(): CarDao
    abstract fun customerDao(): CustomerDao
    abstract fun appUserDao(): AppUserDao
}