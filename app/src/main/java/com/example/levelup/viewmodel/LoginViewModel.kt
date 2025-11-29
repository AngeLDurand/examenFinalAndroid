package com.example.levelup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.model.LoginErrors
import com.example.levelup.model.LoginUiState
import com.example.levelup.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: UserRepository = UserRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun onCorreoChange(valor: String) {
        _uiState.value = _uiState.value.copy(
            correo = valor,
            errores = _uiState.value.errores.copy(correo = null)
        )
    }

    fun onClaveChange(valor: String) {
        _uiState.value = _uiState.value.copy(
            clave = valor,
            errores = _uiState.value.errores.copy(clave = null)
        )
    }

    private fun validarFormulario(): Boolean {
        val actual = _uiState.value
        var errores = LoginErrors()
        var valido = true

        if (actual.correo.isBlank()) {
            errores = errores.copy(correo = "El correo es obligatorio")
            valido = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(actual.correo).matches()) {
            errores = errores.copy(correo = "Formato de correo inválido")
            valido = false
        }

        if (actual.clave.length < 8) {
            errores = errores.copy(clave = "La clave debe tener al menos 8 caracteres")
            valido = false
        }

        _uiState.value = actual.copy(errores = errores)
        return valido
    }

    fun login(
        onSuccess: (String) -> Unit,   // nos interesa el token
        onError: () -> Unit
    ) {
        if (!validarFormulario()) return

        val state = _uiState.value

        viewModelScope.launch {
            // 🔄 empezamos: mostrar spinner
            _uiState.value = state.copy(isLoading = true)

            val token = repository.login(
                correo = state.correo,
                clave = state.clave
            )

            if (token != null) {
                // Éxito
                onSuccess(token)
            } else {
                // Error: quitamos loading y mostramos formulario de nuevo
                _uiState.value = _uiState.value.copy(isLoading = false)
                onError()
            }
        }
    }

    fun limpiarFormulario() {
        _uiState.value = LoginUiState()
    }
}
