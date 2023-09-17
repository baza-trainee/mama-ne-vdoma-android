package com.example.mama_ne_vdoma.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.mama_ne_vdoma.ui.screens.create_user.RestorePasswordFunc
import com.example.mama_ne_vdoma.ui.screens.login.LoginUserFunc

fun NavGraphBuilder.loginNavGraph(
    navController: NavHostController
) {
    navigation(
        route = "login_graph",
        startDestination = "login_screen"
    ) {
        composable("login_screen") {
            LoginUserFunc(
                { navController.navigate("create_user_screen") },
                { navController.navigate("restore_password_screen") },
                {},
                {},
                {}
            )
        }
        composable("restore_password_screen") {
            RestorePasswordFunc(
                { navController.popBackStack() },
                {},
            )
        }
    }
}