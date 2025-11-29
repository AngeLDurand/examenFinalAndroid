package com.example.levelup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.model.RegisterErrors
import com.example.levelup.model.RegisterUiState
import com.example.levelup.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: UserRepository = UserRepository()) : ViewModel() {

    // StateFlow con el estado del formulario
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()   // solo lectura desde la UI

    // --- Actualizar campos ---

    fun onNombreChange(valor: String) {
        _uiState.value = _uiState.value.copy(
            nombre = valor,
            errores = _uiState.value.errores.copy(nombre = null) // limpia error al escribir
        )
    }

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

    fun onConfirmarClaveChange(valor: String) {
        _uiState.value = _uiState.value.copy(
            confirmarClave = valor,
            errores = _uiState.value.errores.copy(confirmarClave = null) // limpia error al escribir
        )
    }




    // --- Validación ---

    fun validarFormulario(): Boolean {
        val actual = _uiState.value
        var errores = RegisterErrors()
        var valido = true

        // --- Nombre ---
        if (actual.nombre.isBlank()) {
            errores = errores.copy(nombre = "El nombre es obligatorio")
            valido = false
        }

        // --- Correo ---
        if (actual.correo.isBlank()) {
            errores = errores.copy(correo = "El correo es obligatorio")
            valido = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(actual.correo).matches()) {
            errores = errores.copy(correo = "Formato de correo inválido")
            valido = false
        }

        // --- Contraseña ---
        if (actual.clave.length < 8) {
            errores = errores.copy(clave = "La clave debe tener al menos 8 caracteres")
            valido = false
        }

        // --- Confirmar clave ---
        if (actual.confirmarClave.isBlank()) {
            errores = errores.copy(confirmarClave = "Debes confirmar la contraseña")
            valido = false
        } else if (actual.clave != actual.confirmarClave) {
            errores = errores.copy(confirmarClave = "Las contraseñas no coinciden")
            valido = false
        }

        _uiState.value = actual.copy(errores = errores)
        return valido
    }



    // función pública para registrar en API
    fun registrar(
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        if (!validarFormulario()) return

        val state = _uiState.value

        viewModelScope.launch {
            // mostrar spinner
            _uiState.value = state.copy(isLoading = true)

            val ok = repository.registrarUsuario(
                nombre = state.nombre,
                correo = state.correo,
                clave = state.clave
            )

            if (ok) {
                // Éxito: limpiar estado y seguir
                onSuccess()
            } else {
                // Error: solo quitamos loading, conservamos datos y errores
                _uiState.value = _uiState.value.copy(isLoading = false)
                onError()
            }
        }
    }


    fun limpiarFormulario() {
        _uiState.value = RegisterUiState()
    }

}
