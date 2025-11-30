package com.example.levelup.remote

import com.example.levelup.dto.LoginRequestDTO
import com.example.levelup.dto.LoginResponseDTO
import com.example.levelup.dto.RegisterRequestDTO
import retrofit2.Response
import retrofit2.http.Body
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
}