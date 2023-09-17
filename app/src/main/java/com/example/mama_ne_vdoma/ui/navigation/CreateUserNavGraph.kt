package com.example.mama_ne_vdoma.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.mama_ne_vdoma.ui.screens.create_user.CreateUserFunc

fun NavGraphBuilder.createUserNavGraph(
    navController: NavHostController
) {
    navigation(
        route = "create_user_graph",
        startDestination = "create_user_screen"
    ) {
        composable("create_user_screen") {
            CreateUserFunc(
                {},
                {},
                {},
                { navController.navigate("login_screen") }
            )
        }
    }
}