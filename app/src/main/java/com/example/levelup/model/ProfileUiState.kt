package com.example.levelup.model

import android.net.Uri

data class ProfileUiState(
    val nombre: String = "",
    val correo: String = "",
    val imageUri: Uri? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)
