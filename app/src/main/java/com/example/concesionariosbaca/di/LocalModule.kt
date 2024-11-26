package com.example.concesionariosbaca.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.concesionariosbaca.model.database.ConcesionarioDatabase
import com.example.concesionariosbaca.model.database.CarDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ConcesionarioDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            ConcesionarioDatabase::class.java,
            "concesionarios_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideCarDao(database: ConcesionarioDatabase): CarDao {
        return database.carDao()
    }
}
