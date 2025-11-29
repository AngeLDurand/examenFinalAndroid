package com.example.levelup.remote

import com.example.levelup.model.LoginRequest
import com.example.levelup.model.LoginResponse
import com.example.levelup.model.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LevelUpApi {

    @POST("users/register")
    suspend fun registrarUsuario(
        @Body request: RegisterRequest
    ): Response<Void>

    @POST("users/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>
}