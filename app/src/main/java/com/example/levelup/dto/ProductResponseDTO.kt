package com.example.levelup.dto

data class ProductResponseDTO (
    val id: Int,
    val modelo: String,
    val descripcion: String,
    val precio: Long,
    val categoria: String,
    val marca: String,
    val imagenUrl: String
)

