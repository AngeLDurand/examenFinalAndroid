package com.example.levelup.model

import com.example.levelup.dto.OrderResponseDTO

data class PurchaseUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val purchases: List<OrderResponseDTO> = emptyList()
)