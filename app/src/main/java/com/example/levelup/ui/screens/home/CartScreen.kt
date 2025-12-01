package com.example.levelup.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.levelup.viewmodel.CartViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CartScreen(
    cartViewModel: CartViewModel,
    onCheckoutClick: () -> Unit
) {
    val state by cartViewModel.uiState.collectAsState()

    // formateador CLP
    val clpFormatter = remember {
        NumberFormat.getCurrencyInstance(Locale("es", "CL")).apply {
            maximumFractionDigits = 0
            minimumFractionDigits = 0
        }
    }

    val total = state.items.sumOf { it.product.precio * it.quantity }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        if (state.items.isEmpty()) {
            Text(
                text = "Tu carrito está vacío",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Text(
                    text = "Carrito",
                    style = MaterialTheme.typography.headlineSmall
                )

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.items) { item ->
                        CartItemRow(
                            nombre = item.product.modelo,
                            imageUrl = item.product.imagenUrl,
                            precioUnitario = clpFormatter.format(item.product.precio),
                            cantidad = item.quantity,
                            subtotal = clpFormatter.format(item.product.precio * item.quantity),
                            onIncrement = { cartViewModel.increment(item.product.id) },
                            onDecrement = { cartViewModel.decrement(item.product.id) }
                        )
                    }
                }

                Divider()

                // Total + botón pagar
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total", style = MaterialTheme.typography.titleMedium)
                        Text(
                            text = clpFormatter.format(total),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Button(
                        onClick = onCheckoutClick,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Continuar con el pago")
                    }
                }
            }
        }
    }
}

@Composable
private fun CartItemRow(
    nombre: String,
    imageUrl: String,
    precioUnitario: String,
    cantidad: Int,
    subtotal: String,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                model = imageUrl,
                contentDescription = nombre,
                modifier = Modifier.size(64.dp)
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = nombre,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "Precio unitario: $precioUnitario",
                    style = MaterialTheme.typography.bodySmall
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(onClick = onDecrement, contentPadding = PaddingValues(4.dp)) {
                        Text("-")
                    }
                    Text(text = cantidad.toString())
                    OutlinedButton(onClick = onIncrement, contentPadding = PaddingValues(4.dp)) {
                        Text("+")
                    }
                }
            }

            Text(
                text = subtotal,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
