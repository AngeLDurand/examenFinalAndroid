
package com.example.levelup.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.model.AddressSummary
import com.example.levelup.model.AddressUiState
import com.example.levelup.repository.CheckoutRepository
import com.example.levelup.session.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddressViewModel(
    private val repository: CheckoutRepository = CheckoutRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddressUiState())
    val uiState = _uiState.asStateFlow()

    // ====== Campos del formulario ======

    fun onNombreChange(valor: String) {
        _uiState.value = _uiState.value.copy(
            nombre = valor,
            nombreError = null,
            message = null
        )
    }

    fun onCalleChange(valor: String) {
        _uiState.value = _uiState.value.copy(
            calle = valor,
            calleError = null,
            message = null
        )
    }

    fun onCiudadChange(valor: String) {
        _uiState.value = _uiState.value.copy(
            ciudad = valor,
            ciudadError = null,
            message = null
        )
    }

    // ====== Cargar direcciones existentes ======

    fun cargarDirecciones(context: Context) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoadingAddresses = true,
                message = null
            )

            val token = SessionManager.getToken(context)
            if (token == null) {
                _uiState.value = _uiState.value.copy(
                    isLoadingAddresses = false,
                    message = "No hay sesión activa"
                )
                return@launch
            }

            val lista = repository.getDirecciones(token)

            _uiState.value = _uiState.value.copy(
                isLoadingAddresses = false,
                addresses = lista ?: emptyList()
            )
        }
    }

    // ====== Mostrar / ocultar formulario ======

    fun toggleFormVisible() {
        _uiState.value = _uiState.value.copy(
            isFormVisible = !_uiState.value.isFormVisible,
            message = null
        )
    }

    // ====== Validación y creación ======

    private fun validar(): Boolean {
        val state = _uiState.value
        var nombreError: String? = null
        var calleError: String? = null
        var ciudadError: String? = null
        var ok = true

        if (state.nombre.isBlank()) {
            nombreError = "El nombre es obligatorio"
            ok = false
        }
        if (state.calle.isBlank()) {
            calleError = "La calle es obligatoria"
            ok = false
        }
        if (state.ciudad.isBlank()) {
            ciudadError = "La ciudad es obligatoria"
            ok = false
        }

        _uiState.value = state.copy(
            nombreError = nombreError,
            calleError = calleError,
            ciudadError = ciudadError
        )
        return ok
    }

    fun resetForm() {
        _uiState.value = _uiState.value.copy(
            nombre = "",
            calle = "",
            ciudad = "",
            nombreError = null,
            calleError = null,
            ciudadError = null,
            message = null
        )
    }

    fun crearDireccion(
        context: Context,
        onSuccess: (AddressSummary) -> Unit
    ) {
        if (!validar()) return

        val state = _uiState.value

        viewModelScope.launch {
            _uiState.value = state.copy(isLoading = true, message = null)

            val token = SessionManager.getToken(context)
            if (token == null) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    message = "No hay sesión activa"
                )
                return@launch
            }

            val id = repository.crearDireccion(
                token = token,
                nombre = state.nombre,
                calle = state.calle,
                ciudad = state.ciudad
            )

            if (id != null) {
                val nueva = AddressSummary(
                    id = id,
                    nombre = state.nombre,
                    calle = state.calle,
                    ciudad = state.ciudad
                )

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    message = "Dirección creada correctamente",
                    // la agregamos a la lista
                    addresses = _uiState.value.addresses + nueva,
                    isFormVisible = false
                )

                onSuccess(nueva)
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    message = "Error al crear la dirección"
                )
            }
        }
    }
}
