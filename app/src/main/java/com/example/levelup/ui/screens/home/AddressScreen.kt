
package com.example.levelup.ui.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.levelup.model.AddressSummary
import com.example.levelup.viewmodel.AddressViewModel

@Composable
fun AddressScreen(
    onBackClick: () -> Unit,
    onAddressSelected: (AddressSummary) -> Unit,
    viewModel: AddressViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Cargar direcciones al entrar a la pantalla
    LaunchedEffect(Unit) {
        viewModel.cargarDirecciones(context)
    }

    // id de dirección seleccionada (para resaltar la card)
    var selectedId by remember { mutableStateOf<Int?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Dirección de envío",
                style = MaterialTheme.typography.headlineSmall
            )

            // ==========================
            // 1) Lista de direcciones guardadas
            // ==========================
            Text(
                text = "Elige una dirección",
                style = MaterialTheme.typography.titleMedium
            )

            if (state.isLoadingAddresses) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (state.addresses.isEmpty()) {
                Text(
                    text = "No tienes direcciones guardadas.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.addresses) { addr ->
                        val isSelected = selectedId == addr.id

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedId = addr.id
                                },
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = if (isSelected) 6.dp else 2.dp
                            ),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = BorderStroke(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected)
                                    MaterialTheme.colorScheme.primary
                                else
                                    Color(0xFFE0E0E0)
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
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
                                        text = addr.nombre,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                                Text(addr.calle, style = MaterialTheme.typography.bodyMedium)
                                Text(addr.ciudad, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }

            // Botón usar dirección seleccionada
            Button(
                onClick = {
                    val addr = state.addresses.firstOrNull { it.id == selectedId }
                    if (addr != null) {
                        onAddressSelected(addr)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedId != null
            ) {
                Text("Usar esta dirección")
            }

            // ==========================
            // 2) Toggle formulario nueva dirección
            // ==========================

            OutlinedButton(
                onClick = { viewModel.toggleFormVisible() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    if (state.isFormVisible)
                        "Cerrar formulario"
                    else
                        "Crear nueva dirección"
                )
            }

            // ==========================
            // 3) Formulario condicional
            // ==========================

            if (state.isFormVisible) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    OutlinedTextField(
                        value = state.nombre,
                        onValueChange = viewModel::onNombreChange,
                        label = { Text("Nombre de la dirección (ej: Casa Principal)") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = state.nombreError != null,
                        supportingText = {
                            state.nombreError?.let {
                                Text(text = it, color = MaterialTheme.colorScheme.error)
                            }
                        }
                    )

                    OutlinedTextField(
                        value = state.calle,
                        onValueChange = viewModel::onCalleChange,
                        label = { Text("Calle y número") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = state.calleError != null,
                        supportingText = {
                            state.calleError?.let {
                                Text(text = it, color = MaterialTheme.colorScheme.error)
                            }
                        }
                    )

                    OutlinedTextField(
                        value = state.ciudad,
                        onValueChange = viewModel::onCiudadChange,
                        label = { Text("Ciudad") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = state.ciudadError != null,
                        supportingText = {
                            state.ciudadError?.let {
                                Text(text = it, color = MaterialTheme.colorScheme.error)
                            }
                        }
                    )

                    Button(
                        onClick = {
                            viewModel.crearDireccion(
                                context = context,
                                onSuccess = { nueva ->
                                    viewModel.resetForm()
                                    selectedId = nueva.id
                                    onAddressSelected(nueva)
                                }
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Guardar dirección")
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                TextButton(onClick = onBackClick) {
                    Text("Volver")
                }
            }

            state.message?.let { msg ->
                Text(
                    text = msg,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
