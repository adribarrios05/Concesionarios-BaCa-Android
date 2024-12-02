package com.example.concesionariosbaca.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.concesionariosbaca.model.database.ConcesionarioDatabase
import com.example.concesionariosbaca.model.database.CarDao
import com.example.concesionariosbaca.model.entities.CarEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @OptIn(DelicateCoroutinesApi::class)
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ConcesionarioDatabase {
        return Room.databaseBuilder(
            context,
            ConcesionarioDatabase::class.java,
            "concesionarios_db"
        ).build()
    }

    @Provides
    fun provideCarDao(database: ConcesionarioDatabase): CarDao {
        return database.carDao()
    }
}
