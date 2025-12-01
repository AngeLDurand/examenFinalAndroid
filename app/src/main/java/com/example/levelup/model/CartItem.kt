package com.example.levelup.model

import com.example.levelup.dto.ProductResponseDTO

data class CartItem(
    val product: ProductResponseDTO,
    val quantity: Int
)