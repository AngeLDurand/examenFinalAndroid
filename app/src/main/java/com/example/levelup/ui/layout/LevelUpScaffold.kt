package com.example.levelup.ui.layout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.levelup.ui.theme.DarkGray

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
                modifier = Modifier.shadow(
                    elevation = 12.dp,
                    shape = RectangleShape,
                    clip = false
                ),

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
                containerColor = Color.White,
                contentColor = Color.Black,
                tonalElevation = 0.dp,
                modifier = Modifier.shadow(
                    elevation = 12.dp,
                    shape = RectangleShape,
                    clip = false
                )

            ) {

                val customColors = NavigationBarItemDefaults.colors(
                    indicatorColor = DarkGray,
                    selectedIconColor = Color.White,

                )
                NavigationBarItem(
                    selected = selectedTab == HomeTab.PRODUCTS,
                    onClick = { onTabSelected(HomeTab.PRODUCTS) },
                    icon = { Icon(Icons.Filled.Menu, contentDescription = "Inicio") },
                    label = { Text("PRODUCTS") },
                    colors = customColors

                )
                NavigationBarItem(
                    selected = selectedTab == HomeTab.CART,
                    onClick = { onTabSelected(HomeTab.CART) },
                    icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito") },
                    label = { Text("Carrito") },
                    colors = customColors
                )
                NavigationBarItem(
                    selected = selectedTab == HomeTab.PROFILE,
                    onClick = { onTabSelected(HomeTab.PROFILE) },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Tú") },
                    label = { Text("Tú") },
                    colors = customColors
                )
            }
        },
        content = content
    )
}
