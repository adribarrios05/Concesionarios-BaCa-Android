package com.example.concesionariosbaca.data.local

import androidx.room.Database
import com.example.concesionariosbaca.data.local.car.CarEntity
import com.example.concesionariosbaca.data.local.customer.CustomerEntity

@Database(
    entities =[CarEntity::class,
              CustomerEntity::class],
    version = 1
)
abstract class ConcesionarioDatabase {
}