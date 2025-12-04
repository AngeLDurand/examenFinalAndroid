package com.example.levelup.model

data class OrderUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val message: String? = null
)