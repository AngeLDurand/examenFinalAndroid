package com.example.levelup.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.levelup.viewmodel.RegisterViewModel
import kotlinx.coroutines.launch




@Composable
fun RegisterScreen(
    onBackClick: () -> Unit,
    onRegisterSuccess: () -> Unit,
    registerViewModel: RegisterViewModel = viewModel()
){
    val state by registerViewModel.uiState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        registerViewModel.limpiarFormulario()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {

            if (state.isLoading) {
                // SOLO SPINNER MIENTRAS CARGA
                CircularProgressIndicator()
            } else {
                // FORMULARIO NORMAL
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    Text(
                        text = "Crear cuenta",
                        style = MaterialTheme.typography.headlineMedium
                    )

                    // NOMBRE
                    OutlinedTextField(
                        value = state.nombre,
                        onValueChange = registerViewModel::onNombreChange,
                        label = { Text("Nombre") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = state.errores.nombre != null,
                        supportingText = {
                            state.errores.nombre?.let {
                                Text(
                                    text = it,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    )

                    // CORREO
                    OutlinedTextField(
                        value = state.correo,
                        onValueChange = registerViewModel::onCorreoChange,
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

                    // CLAVE
                    OutlinedTextField(
                        value = state.clave,
                        onValueChange = registerViewModel::onClaveChange,
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

                    // CONFIRMAR CLAVE
                    OutlinedTextField(
                        value = state.confirmarClave,
                        onValueChange = registerViewModel::onConfirmarClaveChange,
                        label = { Text("Confirmar contraseña") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        isError = state.errores.confirmarClave != null,
                        supportingText = {
                            state.errores.confirmarClave?.let {
                                Text(
                                    text = it,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // BOTÓN REGISTRARSE
                    Button(
                        onClick = {
                            registerViewModel.registrar(
                                onSuccess = {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Usuario registrado correctamente")
                                        onRegisterSuccess()  // aquí navegas a login
                                    }
                                },
                                onError = {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Error al registrar usuario")
                                    }
                                }
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Registrarse")
                    }

                    // BOTÓN VOLVER
                    TextButton(
                        onClick = {
                            registerViewModel.limpiarFormulario()
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
