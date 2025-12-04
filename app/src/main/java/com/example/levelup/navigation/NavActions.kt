package com.example.levelup.navigation

import androidx.navigation.NavController

class NavActions(private val navController: NavController) {

    fun goToRegister() {
        navController.navigate(NavRoutes.Register.route)
    }

    fun goToWelcome() {
        navController.navigate(NavRoutes.Welcome.route) {
            popUpTo(0)
            launchSingleTop = true
        }
    }

    fun goToLogin() {
        navController.navigate(NavRoutes.Login.route)
    }

    fun goToHome() {
        navController.navigate(NavRoutes.Home.route)
    }

    fun goBack() {
        navController.popBackStack()
    }
}