package com.example.levelup.model

data class ChangePasswordUiState(
    val passwordActual: String = "",
    val passwordNueva: String = "",
    val confirmarPasswordNueva: String = "",
    val passwordActualError: String? = null,
    val passwordNuevaError: String? = null,
    val confirmarPasswordError: String? = null,
    val isLoading: Boolean = false,
    val message: String? = null,
    val isSuccess: Boolean = false
)