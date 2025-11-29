package com.example.levelup.repository

import com.example.levelup.model.LoginRequest
import com.example.levelup.model.RegisterRequest
import com.example.levelup.remote.RetrofitClient
import retrofit2.HttpException
import java.io.IOException

class UserRepository {

    suspend fun registrarUsuario(nombre: String, correo: String, clave: String): Boolean {
        return try {
            val request = RegisterRequest(nombre, correo, clave)
            val response = RetrofitClient.api.registrarUsuario(request)

            println(">>> REGISTRO: code=${response.code()}, message=${response.message()}")

            response.isSuccessful
        } catch (e: IOException) {
            e.printStackTrace()
            println(">>> REGISTRO: IOException = ${e.message}")
            false
        } catch (e: HttpException) {
            e.printStackTrace()
            println(">>> REGISTRO: HttpException = ${e.message}")
            false
        }
    }



    suspend fun login(correo: String, clave: String): String? {
        return try {
            val request = LoginRequest(correo, clave)
            val response = RetrofitClient.api.login(request)
            if (response.isSuccessful) {
                println(">>> LOGIN: code=${response.code()}, message=${response.message()}")
                response.body()?.token
            } else {
                println(">>> LOGIN: ERROR isSuccessful = false")
                null
            }
        } catch (e: IOException) {
            println(">>> LOGIN: IOException = ${e.message}")
            e.printStackTrace()
            null
        } catch (e: HttpException) {
            println(">>> LOGIN: HttpException = ${e.message}")
            e.printStackTrace()
            null
        }
    }
}