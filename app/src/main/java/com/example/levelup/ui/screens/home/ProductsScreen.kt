// File: ProductsScreen.kt
package com.example.levelup.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.levelup.dto.ProductResponseDTO
import com.example.levelup.model.CartItem
import com.example.levelup.ui.theme.GreenAccent
import com.example.levelup.ui.theme.LightGray
import com.example.levelup.viewmodel.ProductsViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ProductsScreen(
    viewModel: ProductsViewModel = viewModel(),
    cartItems: List<CartItem>,
    onAddToCart: (ProductResponseDTO) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    // filtrado simple por categoría
    val productosFiltrados = remember(state.productos, state.categoriaSeleccionada) {
        state.categoriaSeleccionada?.let { cat ->
            state.productos.filter { it.categoria == cat }
        } ?: state.productos
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        when {
            state.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            state.error != null -> {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = state.error ?: "Error desconocido",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                    Button(onClick = { viewModel.cargarProductos() }) {
                        Text("Reintentar")
                    }
                }
            }

            else -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Text(
                        text = "Productos",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    // ----- Filtros por categoría -----
                    val categorias = state.productos
                        .map { it.categoria }
                        .distinct()

                    if (categorias.isNotEmpty()) {
                        CategoryFilterRow(
                            categorias = categorias,
                            categoriaSeleccionada = state.categoriaSeleccionada,
                            onCategoriaClick = { viewModel.seleccionarCategoria(it) }
                        )
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(productosFiltrados) { product ->
                            val quantityInCart = cartItems
                                .firstOrNull { it.product.id == product.id }
                                ?.quantity ?: 0

                            ProductCard(
                                product = product,
                                quantityInCart = quantityInCart,
                                onAddToCart = { onAddToCart(product) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryFilterRow(
    categorias: List<String>,
    categoriaSeleccionada: String?,
    onCategoriaClick: (String?) -> Unit
) {


    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Chip "Todos"
        item {
            AssistChip(
                onClick = { onCategoriaClick(null) },
                label = { Text("Todos") },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = if (categoriaSeleccionada == null) LightGray else Color.White,
                     labelColor = if (categoriaSeleccionada == null) Color.White else LightGray
                )
            )
        }

        // Chips por categoría
        items(categorias) { cat ->
            AssistChip(
                onClick = { onCategoriaClick(cat) },
                label = { Text(cat) },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = if (categoriaSeleccionada == cat) LightGray else Color.White,
                    labelColor = if (categoriaSeleccionada == cat) Color.White else LightGray
                )
            )
        }
    }
}

@Composable
private fun ProductCard(
    product: ProductResponseDTO,
    quantityInCart: Int,
    onAddToCart: () -> Unit
) {

    // Formateador CLP sin decimales
    val clpFormatter = remember {
        NumberFormat.getCurrencyInstance(Locale("es", "CL")).apply {
            maximumFractionDigits = 0
            minimumFractionDigits = 0
        }
    }
    val precioFormateado = clpFormatter.format(product.precio)

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            AsyncImage(
                model = product.imagenUrl,
                contentDescription = product.modelo,
                modifier = Modifier.size(72.dp)
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = product.modelo,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = product.descripcion,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "${product.marca} • ${product.categoria}",
                    style = MaterialTheme.typography.labelSmall
                )

                Text(
                    text = precioFormateado,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))

                if (quantityInCart > 0) {

                    OutlinedButton(
                        onClick = { },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = false
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "En carrito",
                            tint = GreenAccent
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("En carrito ($quantityInCart)")
                    }
                } else {

                    Button(
                        onClick = onAddToCart,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Agregar al carrito")
                    }
                }
            }




            }
        }
    }

