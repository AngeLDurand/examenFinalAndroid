package com.example.levelup.dto

data class CreateOrderRequestDTO(
    val addressId: Int,
    val items: List<OrderItemRequestDTO>
)