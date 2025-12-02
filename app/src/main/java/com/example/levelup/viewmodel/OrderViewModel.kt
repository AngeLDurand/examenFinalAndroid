package com.example.levelup.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.model.CartItem
import com.example.levelup.model.OrderUiState
import com.example.levelup.repository.CheckoutRepository
import com.example.levelup.session.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrderViewModel(
    private val repository: CheckoutRepository = CheckoutRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrderUiState())
    val uiState = _uiState.asStateFlow()

    fun procesarOrden(
        context: Context,
        addressId: Int,
        items: List<CartItem>,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _uiState.value = OrderUiState(isLoading = true)

            val token = SessionManager.getToken(context)
            if (token == null) {
                _uiState.value = OrderUiState(
                    isLoading = false,
                    message = "No hay sesión activa"
                )
                return@launch
            }

            val ok = repository.crearOrden(
                token = token,
                addressId = addressId,
                items = items
            )

            if (ok) {
                _uiState.value = OrderUiState(
                    isLoading = false,
                    isSuccess = true
                )
                onSuccess()
            } else {
                _uiState.value = OrderUiState(
                    isLoading = false,
                    message = "Error al procesar el pago"
                )
            }
        }
    }

    fun reset() {
        _uiState.value = OrderUiState()
    }
}