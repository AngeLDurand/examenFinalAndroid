package com.example.levelup.model

data class RegisterUiState(
    val nombre: String = "",
    val correo: String = "",
    val clave: String = "",
    val confirmarClave: String = "",
    val errores: RegisterErrors = RegisterErrors(),
    val isLoading: Boolean = false
)