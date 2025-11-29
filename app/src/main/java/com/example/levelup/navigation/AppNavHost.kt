package com.example.levelup.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.levelup.session.SessionManager
import com.example.levelup.ui.screens.HomeScreen
import com.example.levelup.ui.screens.LoginScreen
import com.example.levelup.ui.screens.RegisterScreen
import com.example.levelup.ui.screens.WelcomeScreen
import kotlinx.coroutines.launch


@Composable
fun AppNavHost(navController: NavHostController) {

    val actions = remember(navController) {
        NavActions(navController)
    }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.Welcome.route
    ) {
        composable(NavRoutes.Welcome.route) {
            WelcomeScreen(
                onLoginClick = { actions.goToLogin() },
                onRegisterClick = { actions.goToRegister()}
            )
        }

        composable(NavRoutes.Register.route) {
            RegisterScreen(
                onBackClick = {actions.goBack() },
                { actions.goToLogin() }
            )
        }

        composable(NavRoutes.Login.route) {
            LoginScreen(
                onBackClick = { actions.goBack() },
                onLoginSuccess = { token ->
                    val ctx = context
                    scope.launch {
                        SessionManager.saveToken(ctx, token)
                        actions.goToHome()
                    }
                }
            )
        }

        composable(NavRoutes.Home.route) {
            HomeScreen(
                onLogout = {
                    scope.launch {
                        SessionManager.clearToken(context)
                        // Volvemos a Welcome;
                        actions.goToWelcome();

                    }
                }
            )
        }




    }
}