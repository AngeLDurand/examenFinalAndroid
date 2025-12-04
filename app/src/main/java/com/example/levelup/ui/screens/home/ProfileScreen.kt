package com.example.levelup.ui.screens.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.OpenDocument
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.levelup.ui.components.ProfileImage
import com.example.levelup.viewmodel.ProfileViewModel
import java.io.File

@Composable
fun ProfileScreen(
    onChangePasswordClick: () -> Unit,
    onViewOrdersClick: () -> Unit = {},
    viewModel: ProfileViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) { viewModel.cargarPerfil(context) }

    var cameraImageUri by remember { mutableStateOf<Uri?>(null) }

    // ---- Launchers ----
    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success && cameraImageUri != null) {
                viewModel.actualizarFotoPerfil(context, cameraImageUri!!)
            }
        }

    val requestCameraPermissionLauncher =
        rememberLauncherForActivityResult(RequestPermission()) { granted ->
            if (granted) {
                val uri = createImageUri(context)
                cameraImageUri = uri
                cameraLauncher.launch(uri)
            }
        }

    val galleryLauncher =
        rememberLauncherForActivityResult(OpenDocument()) { uri: Uri? ->
            if (uri != null) {
                val cr = context.contentResolver
                try {
                    cr.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                } catch (_: SecurityException) {}
                viewModel.actualizarFotoPerfil(context, uri)
            }
        }


    // ============================
    //      CONTENIDO PANTALLA
    // ============================

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.TopCenter
    ) {

        when {
            state.isLoading -> CircularProgressIndicator()

            state.error != null -> Text(
                text = state.error ?: "Error desconocido",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error
            )

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {

                    // ======================================================
                    // CARD DE PERFIL (FONDO BLANCO)
                    // ======================================================
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.large,
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)   // ⭐ fondo blanco
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {

                            Text(
                                text = "Perfil de usuario",
                                style = MaterialTheme.typography.titleLarge
                            )

                            ProfileImage(
                                imageUri = state.imageUri,
                                size = 120.dp
                            )

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = state.nombre,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = state.correo,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Divider(
                                modifier = Modifier.padding(vertical = 8.dp),
                                thickness = 0.5.dp,
                                color = MaterialTheme.colorScheme.outlineVariant
                            )

                            // Botones de foto dentro de la misma card
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        galleryLauncher.launch(arrayOf("image/*"))
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Elegir desde galería")
                                }

                                OutlinedButton(
                                    onClick = {
                                        val hasPermission =
                                            ContextCompat.checkSelfPermission(
                                                context,
                                                Manifest.permission.CAMERA
                                            ) == PackageManager.PERMISSION_GRANTED

                                        if (hasPermission) {
                                            val uri = createImageUri(context)
                                            cameraImageUri = uri
                                            cameraLauncher.launch(uri)
                                        } else {
                                            requestCameraPermissionLauncher.launch(
                                                Manifest.permission.CAMERA
                                            )
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Tomar foto de perfil")
                                }

                                OutlinedButton(
                                    onClick = {
                                        viewModel.elegirFotoRandomDeGato(context)
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Elegir foto random de gato 🐱")
                                }



                            }
                        }
                    }

                    // ======================================================
                    // CARD DE ACCIONES DE CUENTA (FONDO BLANCO)
                    // ======================================================
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.large,
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {

                            Text(
                                text = "Acciones de cuenta",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Button(
                                onClick = onChangePasswordClick,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Cambiar contraseña")
                            }

                            OutlinedButton(
                                onClick = onViewOrdersClick,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Ver mis compras")
                            }
                        }
                    }
                }
            }
        }
    }
}


private fun createImageUri(context: Context): Uri {
    val imagesDir = File(
        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
        "profile"
    )
    if (!imagesDir.exists()) {
        imagesDir.mkdirs()
    }

    val imageFile = File.createTempFile(
        "profile_", ".jpg", imagesDir
    )

    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        imageFile
    )
}
