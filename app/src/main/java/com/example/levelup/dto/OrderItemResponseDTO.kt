package com.example.levelup.dto

data class OrderItemResponseDTO(
    val productoId: Int,
    val modelo: String,
    val imagenUrl: String,
    val cantidad: Int,
    val precioUnitarioPagado: Long
)