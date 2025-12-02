package com.example.levelup.repository

import com.example.levelup.dto.OrderResponseDTO
import com.example.levelup.remote.RetrofitClient
import retrofit2.HttpException
import java.io.IOException

class OrdersRepository {

    suspend fun getOrders(token: String): List<OrderResponseDTO>? {
        return try {
            val response = RetrofitClient.api.getOrders(
                authHeader = "Bearer $token"
            )
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } catch (e: HttpException) {
            e.printStackTrace()
            null
        }
    }
}
