package com.example.mama_ne_vdoma.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.mama_ne_vdoma.ui.screens.create_user.CreateUserFunc
import com.example.mama_ne_vdoma.ui.screens.create_user.EnterPhoneFunc

fun NavGraphBuilder.createUserNavGraph(
    navController: NavHostController
) {
    navigation(
        route = "create_user_graph",
        startDestination = "create_user_screen"
    ) {
        composable("create_user_screen") {
            CreateUserFunc(
                { navController.navigate("enter_phone_screen") }, //temp need replace with logic
                { navController.navigate("login_screen") }
            )
        }
        composable("enter_phone_screen") {
            EnterPhoneFunc {}
        }
    }
}