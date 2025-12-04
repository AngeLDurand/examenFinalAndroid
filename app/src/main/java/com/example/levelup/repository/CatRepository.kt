package com.example.levelup.repository

import com.example.levelup.remote.CatApiClient
import retrofit2.HttpException
import java.io.IOException

class CatRepository {

    suspend fun obtenerGatoRandomUrl(): String? {
        return try {
            val response = CatApiClient.api.getRandomCat()

            response.firstOrNull()?.url

        } catch (e: IOException) {
            e.printStackTrace()
            null
        } catch (e: HttpException) {
            e.printStackTrace()
            null
        }
    }
}
