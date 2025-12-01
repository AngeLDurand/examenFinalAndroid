package com.example.levelup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.model.ProductsUiState
import com.example.levelup.repository.ProductsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductsViewModel(
    private val repository: ProductsRepository = ProductsRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        cargarProductos()
    }

    fun cargarProductos() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            val result = repository.getProducts()

            result
                .onSuccess { lista ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        productos = lista
                    )
                }
                .onFailure {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "No se pudieron cargar los productos"
                    )
                }
        }
    }

    fun seleccionarCategoria(categoria: String?) {
        _uiState.value = _uiState.value.copy(
            categoriaSeleccionada = categoria
        )
    }



}