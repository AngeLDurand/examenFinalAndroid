package com.example.levelup.model

data class LoginUiState(
    val correo: String = "",
    val clave: String = "",
    val errores: LoginErrors = LoginErrors(),
    val isLoading: Boolean = false
)