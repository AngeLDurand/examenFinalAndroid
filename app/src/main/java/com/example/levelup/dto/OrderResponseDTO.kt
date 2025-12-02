package com.example.levelup.dto

data class OrderResponseDTO(
    val id: Int,
    val fecha: String,
    val total: Long,
    val estado: String,
    val direccionCalle: String,
    val direccionCiudad: String,
    val items: List<OrderItemResponseDTO>
)