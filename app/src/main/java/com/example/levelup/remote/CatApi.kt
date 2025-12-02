
package com.example.levelup.remote

import retrofit2.http.GET
import retrofit2.http.Query


data class CatImageResponse(
    val id: String,
    val url: String
)

interface CatApi {

    @GET("images/search")
    suspend fun getRandomCat(
        @Query("limit") limit: Int = 1
    ): List<CatImageResponse>
}
