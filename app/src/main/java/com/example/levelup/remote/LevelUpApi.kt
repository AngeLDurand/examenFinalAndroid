package com.example.levelup.remote

import com.example.levelup.dto.AddressRequestDTO
import com.example.levelup.dto.AddressResponseDTO
import com.example.levelup.dto.ChangePasswordRequestDTO
import com.example.levelup.dto.CreateOrderRequestDTO
import com.example.levelup.dto.LoginRequestDTO
import com.example.levelup.dto.LoginResponseDTO
import com.example.levelup.dto.OrderResponseDTO
import com.example.levelup.dto.ProductResponseDTO
import com.example.levelup.dto.RegisterRequestDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST

interface LevelUpApi {

    @POST("users/register")
    suspend fun registrarUsuario(
        @Body request: RegisterRequestDTO
    ): Response<Void>

    @POST("users/login")
    suspend fun login(
        @Body request: LoginRequestDTO
    ): Response<LoginResponseDTO>

    @PATCH("users/cambiar-password")
    suspend fun cambiarPassword(
        @Header("Authorization") authHeader: String,
        @Body request: ChangePasswordRequestDTO
    ): Response<Void>


    @GET("products")
    suspend fun getProducts(): Response<List<ProductResponseDTO>>


    @POST("addresses")
    suspend fun crearDireccion(
        @Header("Authorization") authHeader: String,
        @Body request: AddressRequestDTO
    ): Response<AddressResponseDTO>

    @GET("addresses")
    suspend fun getDirecciones(
        @Header("Authorization") authHeader: String
    ): Response<List<AddressResponseDTO>>

    @POST("orders")
    suspend fun crearOrden(
        @Header("Authorization") authHeader: String,
        @Body request: CreateOrderRequestDTO
    ): Response<Void>

    @GET("orders")
    suspend fun getOrders(
        @Header("Authorization") authHeader: String
    ): Response<List<OrderResponseDTO>>



}