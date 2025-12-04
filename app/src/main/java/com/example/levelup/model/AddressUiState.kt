package com.example.levelup.model

data class AddressUiState(
    val nombre: String = "",
    val calle: String = "",
    val ciudad: String = "",
    val nombreError: String? = null,
    val calleError: String? = null,
    val ciudadError: String? = null,
    val isLoading: Boolean = false,
    val message: String? = null,
    val addresses: List<AddressSummary> = emptyList(),
    val isLoadingAddresses: Boolean = false,
    val isFormVisible: Boolean = false
)