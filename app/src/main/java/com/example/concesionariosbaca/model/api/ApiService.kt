package com.example.concesionariosbaca.model.api

import com.example.concesionariosbaca.model.entities.AppUserEntity
import com.example.concesionariosbaca.model.entities.CarEntity
import com.example.concesionariosbaca.model.entities.CustomerEntity
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import javax.inject.Inject
import javax.inject.Singleton

interface ApiService {
    @GET("cars")
    suspend fun getCars(): List<CarEntity>

    @GET("car")
    suspend fun getCar(): CarEntity

    @GET("customer")
    suspend fun getCustomer(id: String): List<CustomerEntity>

    @GET("app-user")
    suspend fun getUser(id: String): List<AppUserEntity>
}

@Singleton
class ConcesionarioService @Inject constructor() {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://localhost:1337/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}
