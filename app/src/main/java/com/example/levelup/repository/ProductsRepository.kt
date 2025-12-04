package com.example.levelup.repository

import android.util.Log
import com.example.levelup.dto.ProductResponseDTO
import com.example.levelup.remote.RetrofitClient
import retrofit2.HttpException
import java.io.IOException

class ProductsRepository {



    suspend fun getProducts(): Result<List<ProductResponseDTO>> {
        return try {
            val response = RetrofitClient.api.getProducts()

            if (response.isSuccessful) {
                val body = response.body() ?: emptyList()
                Result.success(body)
            } else {
                Result.failure(Exception("Error HTTP: ${response.code()}"))
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Result.failure(e)
        } catch (e: HttpException) {
            e.printStackTrace()
            Result.failure(e)
        }
    }



}