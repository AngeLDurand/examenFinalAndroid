package com.example.levelup.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.levelup.ui.theme.GreenAccent
import com.example.levelup.viewmodel.ChangePasswordViewModel

@Composable
fun ChangePasswordScreen(
    onBackClick: () -> Unit,
    viewModel: ChangePasswordViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {

        // ---- 1. LOADER ----
        if (state.isLoading) {
            CircularProgressIndicator()
            return@Box
        }

        // ---- 2. ÉXITO (UI especial) ----
        if (state.isSuccess) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                // Ícono ✔ VERDE MATERIAL REAL
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Éxito",
                    tint = GreenAccent,
                    modifier = Modifier.size(90.dp)
                )

                // Texto centrado completamente
                Text(
                    text = state.message ?: "Contraseña actualizada",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                TextButton(
                    onClick = onBackClick
                ) {
                    Text(
                        "Volver",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            return@Box
        }


        // ---- 3. FORMULARIO NORMAL ----
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Cambiar contraseña",
                style = MaterialTheme.typography.headlineMedium
            )

            OutlinedTextField(
                value = state.passwordActual,
                onValueChange = viewModel::onPasswordActualChange,
                label = { Text("Contraseña actual") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                isError = state.passwordActualError != null,
                supportingText = {
                    state.passwordActualError?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                    }
                }
            )

            OutlinedTextField(
                value = state.passwordNueva,
                onValueChange = viewModel::onPasswordNuevaChange,
                label = { Text("Nueva contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                isError = state.passwordNuevaError != null,
                supportingText = {
                    state.passwordNuevaError?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                    }
                }
            )

            OutlinedTextField(
                value = state.confirmarPasswordNueva,
                onValueChange = viewModel::onConfirmarPasswordNuevaChange,
                label = { Text("Confirmar nueva contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                isError = state.confirmarPasswordError != null,
                supportingText = {
                    state.confirmarPasswordError?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                    }
                }
            )

            Button(
                onClick = { viewModel.cambiarPassword(context) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Actualizar contraseña")
            }



            TextButton(
                onClick = onBackClick
            ) {
                Text("Volver")
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

