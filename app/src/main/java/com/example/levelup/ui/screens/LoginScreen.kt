package com.example.levelup.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.levelup.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onBackClick: () -> Unit,
    onLoginSuccess: (String) -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // 👇 limpiar el formulario al primer renderizado de LoginScreen
    LaunchedEffect(Unit) {
        viewModel.limpiarFormulario()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {

            if (state.isLoading) {
                // 🔄 Spinner mientras se hace login
                CircularProgressIndicator()
            } else {
                // 🧾 Formulario de login
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    Text(
                        text = "Iniciar sesión",
                        style = MaterialTheme.typography.headlineMedium
                    )

                    // CORREO
                    OutlinedTextField(
                        value = state.correo,
                        onValueChange = viewModel::onCorreoChange,
                        label = { Text("Correo electrónico") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = state.errores.correo != null,
                        supportingText = {
                            state.errores.correo?.let {
                                Text(
                                    text = it,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    )

                    // CONTRASEÑA
                    OutlinedTextField(
                        value = state.clave,
                        onValueChange = viewModel::onClaveChange,
                        label = { Text("Contraseña") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        isError = state.errores.clave != null,
                        supportingText = {
                            state.errores.clave?.let {
                                Text(
                                    text = it,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    )

                    // BOTÓN LOGIN
                    Button(
                        onClick = {
                            viewModel.login(
                                onSuccess = { token ->
                                    scope.launch {
                                        // Spinner sigue activo mientras se muestra el snackbar
                                        snackbarHostState.showSnackbar("Login exitoso")
                                        onLoginSuccess(token)   // navegas a Home
                                    }
                                },
                                onError = {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Correo o contraseña incorrectos")
                                    }
                                }
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Entrar")
                    }

                    // BOTÓN VOLVER
                    TextButton(
                        onClick = {
                            viewModel.limpiarFormulario()
                            onBackClick()
                        }
                    ) {
                        Text("Volver")
                    }
                }
            }
        }
    }
}
