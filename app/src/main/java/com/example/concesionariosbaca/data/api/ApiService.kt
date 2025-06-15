package com.example.concesionariosbaca.data.api

import com.example.concesionariosbaca.data.entities.*
import com.example.concesionariosbaca.data.mapping.CarListResponse
import com.example.concesionariosbaca.data.mapping.CarRequest
import com.example.concesionariosbaca.data.mapping.CarResponse
import com.example.concesionariosbaca.data.mapping.CustomerResponse
import com.example.concesionariosbaca.data.repository.RegisterCustomerRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import javax.inject.Inject
import javax.inject.Singleton

interface ApiService {

    // Obtener todos los coches con imágenes relacionadas
    @GET("cars?populate[customer]=*&populate[picture]=*")
    suspend fun getCars(): Response<CarListResponse>


    // Obtener detalles de un coche por ID
    @GET("car/{id}")
    suspend fun getCar(@Path("id") id: String): Response<CarResponse>

    // Añadir coche
    @Multipart
    @POST("cars")
    suspend fun addCar(
        @Part("data") carData: RequestBody,
        @Part image: MultipartBody.Part?
    ): Response<CarResponse>

    //Actualizar coche
    @PUT("cars/{id}")
    suspend fun updateCar(
        @Path("id") carId: String,
        @Body request: CarRequest
    ): Response<Unit>

    @PUT("cars/{id}")
    suspend fun putCarWithMap(
        @Path("id") carId: String,
        @Body body: Map<String, @JvmSuppressWildcards Any?>
    ): Response<Unit>



    // Obtener todos los clientes
    @GET("customers")
    suspend fun getCustomers(): Response<List<CustomerEntity>>

    // Obtener detalles de un cliente por ID
    @GET("customer/{id}")
    suspend fun getCustomer(@Path("id") id: String): Response<CustomerEntity>

    @GET("customers")
    suspend fun getCustomerByUserId(@Query("filters[user][id]") userId: Int): Response<CustomerResponse>


    // Obtener detalles de un usuario por ID
    @GET("user/{id}")
    suspend fun getUser(@Path("id") id: String): Response<UserEntity>

    // Registrar un nuevo usuario
    @POST("auth/local/register")
    suspend fun registerUser(@Body registerUser: RegisterUser): Response<RegisterUserResponse>

    // Registrar un cliente relacionado al usuario
    @POST("customers")
    suspend fun registerCustomer(
        @Header("Authorization") token: String,
        @Body registerCustomer: RegisterCustomerRequest
    ): Response<RegisterCustomerResponse>

    // Iniciar sesión
    @POST("auth/local")
    suspend fun login(@Body request: LoginUser): Response<LoginResponse>

    @GET("users/me")
    suspend fun getCurrentUser(
        @Header("Authorization") token: String
    ): Response<UserResponse>

    @PUT("users/me")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Body updateRequest: UpdateProfileRequest
    ): Response<Unit>

    @Multipart
    @POST("upload")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part
    ): Response<List<StrapiUploadResponse>>

}


@Singleton
class ConcesionarioService @Inject constructor() {
    private val apiUrl = "https://concesionarios-baca-service.onrender.com/api/"
    private val apiLocalUrl = "http://192.168.1.127:1337/api/"
    val apiService: ApiService = Retrofit.Builder()
        .baseUrl(apiUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)
}

data class StrapiUploadResponse(
    val id: Int,
    val url: String
)
