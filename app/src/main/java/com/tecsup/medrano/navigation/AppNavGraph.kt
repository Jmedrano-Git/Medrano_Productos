package com.tecsup.medrano.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.medrano.ui.screens.AddEditProductScreen
import com.tecsup.medrano.ui.screens.HomeScreen
import com.tecsup.medrano.ui.screens.LoginScreen
import com.tecsup.medrano.ui.screens.RegisterScreen
import com.tecsup.medrano.ui.viewmodel.AuthViewModel
import com.tecsup.medrano.ui.viewmodel.ProductViewModel

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object AddEditProduct : Screen("add_edit_product/{productId}") {
        fun createRoute(productId: String?): String = "add_edit_product/${productId ?: "new"}"
    }
}

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
        modifier = modifier
    ) {
        composable(Screen.Login.route) {
            val authVm: AuthViewModel = viewModel()
            LoginScreen(
                authViewModel = authVm,
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(Screen.Register.route) {
            val authVm: AuthViewModel = viewModel()
            RegisterScreen(
                authViewModel = authVm,
                onRegisterSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Home.route) {
            val productVm: ProductViewModel = viewModel()
            val authVm: AuthViewModel = viewModel()

            HomeScreen(
                productViewModel = productVm,
                onLogout = {
                    authVm.logout {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }
                },
                onAddProduct = {
                    navController.navigate(Screen.AddEditProduct.createRoute(null))
                },
                onEditProduct = { productId ->
                    navController.navigate(Screen.AddEditProduct.createRoute(productId))
                }
            )
        }

        composable(
            route = Screen.AddEditProduct.route,
            arguments = listOf(
                navArgument("productId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")
            val isEdit = productId != null && productId != "new"
            val productVm: ProductViewModel = viewModel()

            AddEditProductScreen(
                productViewModel = productVm,
                productId = if (isEdit) productId else null,
                onSaveSuccess = { navController.popBackStack() },
                onCancel = { navController.popBackStack() }
            )
        }
    }
}
