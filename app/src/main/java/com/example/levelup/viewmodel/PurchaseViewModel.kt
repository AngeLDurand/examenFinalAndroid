package com.example.levelup.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.model.PurchaseUiState
import com.example.levelup.repository.OrdersRepository
import com.example.levelup.session.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PurchaseViewModel(
    private val repository: OrdersRepository = OrdersRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(PurchaseUiState())
    val uiState = _uiState.asStateFlow()

    fun loadPurchases(context: Context) {
        viewModelScope.launch {
            _uiState.value = PurchaseUiState(isLoading = true)

            val token = SessionManager.getToken(context)
            if (token == null) {
                _uiState.value = PurchaseUiState(
                    isLoading = false,
                    error = "No hay sesión activa"
                )
                return@launch
            }

            val result = repository.getOrders(token)

            if (result != null) {
                _uiState.value = PurchaseUiState(
                    isLoading = false,
                    purchases = result
                )
            } else {
                _uiState.value = PurchaseUiState(
                    isLoading = false,
                    error = "Error al cargar las compras"
                )
            }
        }
    }
}