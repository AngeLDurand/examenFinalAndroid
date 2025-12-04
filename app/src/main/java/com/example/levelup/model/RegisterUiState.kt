package com.example.levelup.model

data class RegisterUiState(
    val nombre: String = "ab",
    val correo: String = "s@gmail.com",
    val clave: String = "",
    val confirmarClave: String = "",
    val errores: RegisterErrors = RegisterErrors(),
    val isLoading: Boolean = false
)