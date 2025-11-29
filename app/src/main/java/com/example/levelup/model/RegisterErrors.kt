package com.example.levelup.model

data class RegisterErrors(
    val nombre: String? = null,
    val correo: String? = null,
    val clave: String? = null,
    val confirmarClave: String? = null
)