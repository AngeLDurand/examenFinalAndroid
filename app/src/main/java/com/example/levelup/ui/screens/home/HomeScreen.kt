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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.levelup.model.AddressSummary
import com.example.levelup.ui.layout.CartStep
import com.example.levelup.ui.layout.HomeTab
import com.example.levelup.ui.layout.LevelUpScaffold
import com.example.levelup.ui.layout.ProfileSection
import com.example.levelup.viewmodel.CartViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    onLogout: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(HomeTab.PRODUCTS) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var profileSection by remember { mutableStateOf(ProfileSection.PROFILE_MAIN) }
    val cartViewModel: CartViewModel = viewModel()
    val cartState by cartViewModel.uiState.collectAsState()
    var cartStep by remember { mutableStateOf(CartStep.LIST) }
    var selectedAddress by remember { mutableStateOf<AddressSummary?>(null) }



    LevelUpScaffold(
        selectedTab = selectedTab,
        onTabSelected = { tab ->
            selectedTab = tab
            if (tab == HomeTab.PROFILE) {
                profileSection = ProfileSection.PROFILE_MAIN
            }
            if (tab == HomeTab.CART && cartStep == CartStep.SUMMARY && selectedAddress == null) {
                cartStep = CartStep.LIST
            }
        },
        onLogoutClick = { showLogoutDialog = true }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {

            when (selectedTab) {
                HomeTab.PRODUCTS -> ProductsScreen(
                    cartItems = cartState.items,
                    onAddToCart = { producto ->
                        cartViewModel.addProduct(producto)
                    }
                )

                HomeTab.CART -> {
                    when (cartStep) {
                        CartStep.LIST -> CartScreen(
                            cartViewModel = cartViewModel,
                            onCheckoutClick = {
                                cartStep = CartStep.ADDRESS
                            }
                        )

                        CartStep.ADDRESS -> AddressScreen(
                            onBackClick = { cartStep = CartStep.LIST },
                            onAddressSelected = { addressSummary ->
                                selectedAddress = addressSummary
                                cartStep = CartStep.SUMMARY
                            }
                        )


                        CartStep.SUMMARY -> {
                            val addr = selectedAddress
                            if (addr != null) {
                                OrderSummaryScreen(
                                    address = addr,
                                    cartItems = cartState.items,
                                    cartViewModel = cartViewModel,
                                    onBackClick = { cartStep = CartStep.ADDRESS },
                                    onOrderSuccessNavigate = {
                                        selectedAddress = null
                                        cartStep = CartStep.LIST
                                        selectedTab = HomeTab.PRODUCTS
                                    }
                                )
                            } else {
                                cartStep = CartStep.LIST
                            }
                        }
                    }
                }

                HomeTab.PROFILE -> {
                    when (profileSection) {
                        ProfileSection.PROFILE_MAIN -> ProfileScreen(
                            onChangePasswordClick = { profileSection = ProfileSection.CHANGE_PASSWORD },
                            onViewOrdersClick = { profileSection = ProfileSection.PURCHASE_HISTORY }
                        )

                        ProfileSection.CHANGE_PASSWORD -> ChangePasswordScreen(
                            onBackClick = { profileSection = ProfileSection.PROFILE_MAIN }
                        )

                        ProfileSection.PURCHASE_HISTORY -> PurchaseHistoryScreen(
                            onBackClick = { profileSection = ProfileSection.PROFILE_MAIN }
                        )
                    }
                }



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