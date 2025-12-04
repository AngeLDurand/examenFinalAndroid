package com.example.levelup.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.levelup.model.AddressSummary
import com.example.levelup.model.CartItem
import com.example.levelup.ui.theme.GreenAccent
import com.example.levelup.viewmodel.CartViewModel
import com.example.levelup.viewmodel.OrderViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun OrderSummaryScreen(
    address: AddressSummary,
    cartItems: List<CartItem>,
    cartViewModel: CartViewModel,
    onBackClick: () -> Unit,
    onOrderSuccessNavigate: () -> Unit,
    viewModel: OrderViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val clpFormatter = remember {
        NumberFormat.getCurrencyInstance(Locale("es", "CL")).apply {
            maximumFractionDigits = 0
            minimumFractionDigits = 0
        }
    }

    val total = cartItems.sumOf { it.product.precio * it.quantity }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        when {
            // ==========================
            // 1) PANTALLA DE ÉXITO
            // ==========================
            state.isSuccess -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Compra exitosa",
                        tint = GreenAccent,
                        modifier = Modifier.size(90.dp)
                    )

                    Text(
                        text = "¡Compra realizada con éxito!",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Button(
                        onClick = {

                            viewModel.reset()

                            onOrderSuccessNavigate()
                        }
                    ) {
                        Text("Volver al inicio")
                    }
                }
            }

            else -> {
                // ==========================
                // 2) RESUMEN + PROCESAR PAGO
                // ==========================

                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Text(
                        text = "Resumen de compra",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    // Dirección
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {

                            // 🔹 Encabezado con icono
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = "Ubicación",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    "Dirección de envío",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }

                            // Línea suave
                            HorizontalDivider(thickness = 1.dp, color = Color(0xFFE0E0E0))

                            // 🔹 Contenido de la dirección
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {

                                Text(
                                    text = address.nombre,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color(0xFF333333)
                                )

                                Text(
                                    text = address.calle,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFF666666)
                                )

                                Text(
                                    text = address.ciudad,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFF666666)
                                )
                            }
                        }
                    }


                    // Items
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(cartItems) { item ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AsyncImage(
                                        model = item.product.imagenUrl,
                                        contentDescription = item.product.modelo,
                                        modifier = Modifier.size(48.dp)
                                    )
                                    Column(
                                        modifier = Modifier.weight(1f),
                                        verticalArrangement = Arrangement.spacedBy(2.dp)
                                    ) {
                                        Text(
                                            item.product.modelo,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Text("Cantidad: ${item.quantity}")
                                    }
                                    Text(
                                        text = clpFormatter.format(
                                            item.product.precio * item.quantity
                                        ),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }

                    Divider()

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

                    if (state.message != null) {
                        Text(
                            text = state.message ?: "",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Button(
                        onClick = {
                            viewModel.procesarOrden(
                                context = context,
                                addressId = address.id,
                                items = cartItems
                            ) {

                                cartViewModel.clearCart()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !state.isLoading && cartItems.isNotEmpty()
                    ) {
                        Text("Procesar pago")
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        TextButton(onClick = onBackClick) {
                            Text("Volver")
                        }
                    }
                }
            }
        }
    }
}
