package com.example.levelup.model

import com.example.levelup.dto.ProductResponseDTO

data class ProductsUiState(
    val productos: List<ProductResponseDTO> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val categoriaSeleccionada: String? = null
)