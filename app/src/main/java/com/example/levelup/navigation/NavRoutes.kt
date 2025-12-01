package com.example.levelup.navigation

sealed class NavRoutes(val route: String) {
    data object Welcome  : NavRoutes("welcome")
    data object Register : NavRoutes("register")
    data object Login    : NavRoutes("login")
    data object Home     : NavRoutes("home")

}
