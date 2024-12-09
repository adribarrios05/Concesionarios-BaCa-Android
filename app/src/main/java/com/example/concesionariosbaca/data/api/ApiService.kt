package com.example.concesionariosbaca.data.api

import com.example.concesionariosbaca.data.entities.CustomerEntity
import com.example.concesionariosbaca.data.entities.LoginResponse
import com.example.concesionariosbaca.data.entities.LoginUser
import com.example.concesionariosbaca.data.entities.UserEntity
import com.example.concesionariosbaca.data.mapping.CarResponse
import com.example.concesionariosbaca.data.entities.RegisterCustomer
import com.example.concesionariosbaca.data.entities.RegisterUser
import com.example.concesionariosbaca.data.entities.RegisterUserResponse
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

    @POST("auth/local")
    suspend fun login(@Body loginUser: LoginUser): LoginResponse
}

@Singleton
class ConcesionarioService @Inject constructor() {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.131:1337/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}
