package com.example.levelup.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.levelup.ui.layout.HomeTab
import com.example.levelup.ui.layout.LevelUpScaffold
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    onLogout: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(HomeTab.HOME) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    LevelUpScaffold(
        selectedTab = selectedTab,
        onTabSelected = { selectedTab = it },
        onLogoutClick = { showLogoutDialog = true }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {

            // Según la pestaña, mostramos algo distinto
            when (selectedTab) {
                HomeTab.HOME -> HomeContent()
                HomeTab.CART -> CartContent()
                HomeTab.PROFILE -> ProfileContent()
            }
        }


        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                title = { Text("Cerrar sesión") },
                text = { Text("¿Estás seguro de que quieres cerrar sesión?") },
                confirmButton = {
                    TextButton(
                        onClick = {

                            onLogout()
                        }
                    ) {
                        Text("Sí, salir")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showLogoutDialog = false }
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }


    }
}