package com.example.levelup.dto


data class ChangePasswordRequestDTO(
    val passwordActual: String,
    val passwordNueva: String
)