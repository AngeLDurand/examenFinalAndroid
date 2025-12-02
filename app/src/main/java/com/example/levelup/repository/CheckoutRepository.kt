package com.example.levelup.repository


import com.example.levelup.dto.AddressRequestDTO
import com.example.levelup.dto.CreateOrderRequestDTO
import com.example.levelup.dto.OrderItemRequestDTO
import com.example.levelup.model.AddressSummary
import com.example.levelup.model.CartItem
import com.example.levelup.remote.RetrofitClient
import java.io.IOException
import retrofit2.HttpException

class CheckoutRepository {

    suspend fun crearDireccion(
        token: String,
        nombre: String,
        calle: String,
        ciudad: String
    ): Int? {
        return try {
            val request = AddressRequestDTO(nombre, calle, ciudad)
            val response = RetrofitClient.api.crearDireccion(
                authHeader = "Bearer $token",
                request = request
            )

            if (response.isSuccessful) {
                response.body()?.id
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



    suspend fun crearOrden(
        token: String,
        addressId: Int,
        items: List<CartItem>
    ): Boolean {
        return try {
            val dtoItems = items.map {
                OrderItemRequestDTO(
                    productId = it.product.id,
                    cantidad = it.quantity
                )
            }

            val request = CreateOrderRequestDTO(
                addressId = addressId,
                items = dtoItems
            )

            val response = RetrofitClient.api.crearOrden(
                authHeader = "Bearer $token",
                request = request
            )

            response.isSuccessful
        } catch (e: IOException) {
            e.printStackTrace()
            false
        } catch (e: HttpException) {
            e.printStackTrace()
            false
        }
    }

    suspend fun getDirecciones(token: String): List<AddressSummary>? {
        return try {
            val response = RetrofitClient.api.getDirecciones(
                authHeader = "Bearer $token"
            )

            if (response.isSuccessful) {
                response.body()?.map { dto ->
                    AddressSummary(
                        id = dto.id,
                        nombre = dto.nombre,
                        calle = dto.calle,
                        ciudad = dto.ciudad
                    )
                }
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