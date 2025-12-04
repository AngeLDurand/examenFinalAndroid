package com.example.levelup.viewmodel

import androidx.lifecycle.ViewModel
import com.example.levelup.dto.ProductResponseDTO
import com.example.levelup.model.CartItem
import com.example.levelup.model.CartUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CartViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CartUiState())
    val uiState = _uiState.asStateFlow()

    fun addProduct(product: ProductResponseDTO) {
        val currentItems = _uiState.value.items.toMutableList()
        val index = currentItems.indexOfFirst { it.product.id == product.id }

        if (index >= 0) {
            val item = currentItems[index]
            currentItems[index] = item.copy(quantity = item.quantity + 1)
        } else {
            currentItems.add(CartItem(product = product, quantity = 1))
        }

        _uiState.value = _uiState.value.copy(items = currentItems)
    }

    fun increment(productId: Int) {
        val currentItems = _uiState.value.items.toMutableList()
        val index = currentItems.indexOfFirst { it.product.id == productId }
        if (index >= 0) {
            val item = currentItems[index]
            currentItems[index] = item.copy(quantity = item.quantity + 1)
            _uiState.value = _uiState.value.copy(items = currentItems)
        }
    }

    fun decrement(productId: Int) {
        val currentItems = _uiState.value.items.toMutableList()
        val index = currentItems.indexOfFirst { it.product.id == productId }
        if (index >= 0) {
            val item = currentItems[index]
            val newQuantity = item.quantity - 1
            if (newQuantity <= 0) {
                currentItems.removeAt(index)
            } else {
                currentItems[index] = item.copy(quantity = newQuantity)
            }
            _uiState.value = _uiState.value.copy(items = currentItems)
        }
    }

    fun clearCart() {
        _uiState.value = CartUiState()
    }
}
