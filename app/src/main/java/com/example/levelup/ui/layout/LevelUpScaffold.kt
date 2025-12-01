package com.example.levelup.ui.layout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

import com.example.levelup.ui.theme.LightGray


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelUpScaffold(
    selectedTab: HomeTab,
    onTabSelected: (HomeTab) -> Unit,
    onLogoutClick: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(

                title = {
                    AsyncImage(
                        model = "https://levelup-gamer-assets-prod.s3.us-east-1.amazonaws.com/logoLevelUpTopBarAndroid.webp",
                        contentDescription = "Logo LevelUp Gamer",
                        modifier = Modifier
                            .height(40.dp),
                        contentScale = ContentScale.Fit
                    )
                },
                actions = {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        IconButton(onClick = onLogoutClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = "Cerrar sesión"
                            )
                        }
                        Text(
                            text = "Cerrar sesión",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = LightGray,
                contentColor = Color.Black,
                tonalElevation = 0.dp


            ) {
                NavigationBarItem(
                    selected = selectedTab == HomeTab.HOME,
                    onClick = { onTabSelected(HomeTab.HOME) },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio") },
                    colors = NavigationBarItemDefaults.colors(indicatorColor =Color.White)




                )
                NavigationBarItem(
                    selected = selectedTab == HomeTab.CART,
                    onClick = { onTabSelected(HomeTab.CART) },
                    icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito") },
                    label = { Text("Carrito") },
                    colors = NavigationBarItemDefaults.colors(indicatorColor =Color.White)
                )
                NavigationBarItem(
                    selected = selectedTab == HomeTab.PROFILE,
                    onClick = { onTabSelected(HomeTab.PROFILE) },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Tú") },
                    label = { Text("Tú") },
                    colors = NavigationBarItemDefaults.colors(indicatorColor =Color.White)
                )
            }
        },
        content = content
    )
}
