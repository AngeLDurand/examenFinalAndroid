package com.example.levelup.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.model.ChangePasswordUiState
import com.example.levelup.repository.UserRepository
import com.example.levelup.session.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChangePasswordViewModel(
    private val repository: UserRepository = UserRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChangePasswordUiState())
    val uiState = _uiState.asStateFlow()

    // --- Handlers de campos ---

    fun onPasswordActualChange(valor: String) {
        _uiState.value = _uiState.value.copy(
            passwordActual = valor,
            passwordActualError = null,
            message = null
        )
    }

    fun onPasswordNuevaChange(valor: String) {
        _uiState.value = _uiState.value.copy(
            passwordNueva = valor,
            passwordNuevaError = null,
            message = null
        )
    }

    fun onConfirmarPasswordNuevaChange(valor: String) {
        _uiState.value = _uiState.value.copy(
            confirmarPasswordNueva = valor,
            confirmarPasswordError = null,
            message = null
        )
    }

    // --- Validación ---

    private fun validarFormulario(): Boolean {
        val state = _uiState.value
        var actualError: String? = null
        var nuevaError: String? = null
        var confirmarError: String? = null
        var valido = true

        if (state.passwordActual.length < 8) {
            actualError = "La contraseña actual debe tener al menos 8 caracteres"
            valido = false
        }

        if (state.passwordNueva.length < 8) {
            nuevaError = "La nueva contraseña debe tener al menos 8 caracteres"
            valido = false
        }

        if (state.passwordNueva == state.passwordActual) {
            nuevaError = "La nueva contraseña debe ser distinta a la actual"
            valido = false
        }

        if (state.confirmarPasswordNueva != state.passwordNueva) {
            confirmarError = "Las contraseñas no coinciden"
            valido = false
        }

        _uiState.value = state.copy(
            passwordActualError = actualError,
            passwordNuevaError = nuevaError,
            confirmarPasswordError = confirmarError
        )

        return valido
    }

    // --- Caso de uso: cambiar contraseña ---

    fun cambiarPassword(context: Context) {
        if (!validarFormulario()) return

        val state = _uiState.value

        viewModelScope.launch {
            _uiState.value = state.copy(
                isLoading = true,
                message = null
            )

            val token = SessionManager.getToken(context)
            if (token == null) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    message = "No hay sesión activa"
                )
                return@launch
            }

            val ok = repository.cambiarPassword(
                token = token,
                passwordActual = state.passwordActual,
                passwordNueva = state.passwordNueva
            )

            if (ok) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSuccess = true,
                    message = "Contraseña actualizada correctamente"
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    message = "Error al actualizar la contraseña"
                )
            }

        }
    }
}