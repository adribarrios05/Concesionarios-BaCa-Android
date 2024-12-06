package com.example.concesionariosbaca.model.api

import com.example.concesionariosbaca.model.entities.CustomerEntity
import com.example.concesionariosbaca.model.entities.UserEntity
import com.example.concesionariosbaca.model.mapping.CarResponse
import com.example.concesionariosbaca.model.entities.RegisterCustomer
import com.example.concesionariosbaca.model.entities.RegisterUser
import com.example.concesionariosbaca.model.entities.RegisterUserResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import javax.inject.Inject
import javax.inject.Singleton


interface ApiService {
    @GET("cars?populate=picture")
    suspend fun getCars(): CarResponse

    @GET("car/{id}")
    suspend fun getCar(@Path("id") id: String): CarResponse

    @GET("customers")
    suspend fun getCustomers(): List<CustomerEntity>

    @GET("customer/{id}")
    suspend fun getCustomer(id: String): CustomerEntity

    @GET("user")
    suspend fun getUser(id: String): List<UserEntity>

    @POST("auth/local/register")
    suspend fun registerUser(@Body registerUser: RegisterUser): RegisterUserResponse

    @POST("customers")
    suspend fun registerCustomer(
        @Header("Authorization") token: String,
        @Body registerCustomer: RegisterCustomer
    )

}

@Singleton
class ConcesionarioService @Inject constructor() {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://172.31.22.157:1337/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}
