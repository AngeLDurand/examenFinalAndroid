// File: ProfileViewModel.kt
package com.example.levelup.viewmodel

import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.model.ProfileUiState
import com.example.levelup.session.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import android.content.Context
import android.net.Uri

class ProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()


    fun cargarPerfil(context: Context) {
        viewModelScope.launch {
            try {
                val token = SessionManager.getToken(context)
                val savedImageUri = SessionManager.getProfileImageUri(context)

                if (token == null) {
                    _uiState.value = ProfileUiState(
                        isLoading = false,
                        error = "No se encontró token de sesión"
                    )
                    return@launch
                }

                val (nombre, correo) = decodeJwt(token)

                _uiState.value = ProfileUiState(
                    nombre = nombre ?: "",
                    correo = correo ?: "",
                    imageUri = savedImageUri?.let { Uri.parse(it) },
                    isLoading = false,
                    error = null
                )

            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = ProfileUiState(
                    isLoading = false,
                    error = "Error al leer el token"
                )
            }
        }
    }



    fun actualizarFotoPerfil(context: Context, uri: Uri) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(imageUri = uri)
            SessionManager.saveProfileImageUri(context, uri.toString())
        }
    }


    private fun decodeJwt(token: String): Pair<String?, String?> {
        return try {
            val parts = token.split(".")
            if (parts.size < 2) return Pair(null, null)

            val payload = parts[1]

            val decodedBytes = Base64.decode(
                payload,
                Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP
            )
            val json = JSONObject(String(decodedBytes))

            val nombre = json.optString("nombre", null)
            val correo = json.optString("sub", null)

            Pair(nombre, correo)
        } catch (e: Exception) {
            e.printStackTrace()
            Pair(null, null)
        }
    }
}
