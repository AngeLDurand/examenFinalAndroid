package com.example.levelup.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.levelup.dto.OrderResponseDTO
import com.example.levelup.dto.OrderItemResponseDTO
import com.example.levelup.viewmodel.PurchaseViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun PurchaseHistoryScreen(
    onBackClick: () -> Unit,
    viewModel: PurchaseViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadPurchases(context)
    }

    val clp = remember {
        NumberFormat.getCurrencyInstance(Locale("es", "CL")).apply {
            minimumFractionDigits = 0
            maximumFractionDigits = 0
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        when {
            state.isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

            state.error != null -> {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(state.error ?: "Error desconocido", color = MaterialTheme.colorScheme.error)
                    Button(onClick = { viewModel.loadPurchases(context) }) {
                        Text("Reintentar")
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        TextButton(onClick = onBackClick) { Text("Volver") }
                    }
                }
            }

            state.purchases.isEmpty() -> {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Aún no tienes compras registradas.")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        TextButton(onClick = onBackClick) { Text("Volver") }
                    }
                }
            }

            else -> {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Mis compras", style = MaterialTheme.typography.headlineSmall)

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.purchases) { order ->
                            PurchaseCard(order, clp)
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        TextButton(onClick = onBackClick) { Text("Volver") }
                    }
                }
            }
        }
    }
}

@Composable
fun PurchaseCard(order: OrderResponseDTO, clp: NumberFormat) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("Orden #${order.id}", style = MaterialTheme.typography.titleMedium)
                    Text(order.fecha.take(10))
                }
                Text(
                    order.estado,
                    color = when (order.estado.uppercase()) {
                        "PAGADO" -> Color(0xFF2E7D32)
                        else -> Color.Gray
                    }
                )
            }

            Text("Enviado a:", style = MaterialTheme.typography.bodyMedium)
            Text("${order.direccionCalle}, ${order.direccionCiudad}")

            Divider()

            order.items.forEach {
                OrderItemRow(it, clp)
            }

            Divider()

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Total pagado")
                Text(clp.format(order.total), color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
fun OrderItemRow(item: OrderItemResponseDTO, clp: NumberFormat) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(model = item.imagenUrl, contentDescription = item.modelo, modifier = Modifier.size(40.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(item.modelo, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text("Cantidad: ${item.cantidad}")
        }

        Text(clp.format(item.precioUnitarioPagado * item.cantidad))
    }
}
