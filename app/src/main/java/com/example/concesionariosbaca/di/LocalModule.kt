package com.example.concesionariosbaca.di

import android.content.Context
import androidx.room.Room
import com.example.concesionariosbaca.data.database.ConcesionarioDatabase
import com.example.concesionariosbaca.data.database.CarDao
import com.example.concesionariosbaca.data.database.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.DelicateCoroutinesApi
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
        )
            //.fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideCarDao(database: ConcesionarioDatabase): CarDao {
        return database.carDao()
    }

    @Provides
    fun provideUserDao(database: ConcesionarioDatabase): UserDao {
        return database.userDao()
    }
}
