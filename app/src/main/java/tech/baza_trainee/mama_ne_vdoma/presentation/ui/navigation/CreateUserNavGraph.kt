package tech.baza_trainee.mama_ne_vdoma.presentation.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.ChildNameFunc
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.ChildScheduleFunc
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.CreateUserFunc
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.EnterPhoneFunc
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.UserLocationFunc
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.vm.UserCreateViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.vm.UserSettingsViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.sharedViewModel

fun NavGraphBuilder.createUserNavGraph(
    navController: NavHostController
) {
    navigation(
        route = "create_user_graph",
        startDestination = "create_user_screen"
    ) {
        composable("create_user_screen") {
            val userCreateViewModel: UserCreateViewModel = it.sharedViewModel(navController)
            CreateUserFunc(
                userCreateViewModel,
                { navController.navigate("enter_phone_screen") }, //temp need replace with logic
                { navController.navigate("login_screen") }
            )
        }
        composable("enter_phone_screen") {
            val userSettingsViewModel: UserSettingsViewModel = it.sharedViewModel(navController)
            EnterPhoneFunc(
                userSettingsViewModel
            ) { navController.navigate("user_location_screen") }
        }
        composable("user_location_screen") {
            val userSettingsViewModel: UserSettingsViewModel = it.sharedViewModel(navController)
            UserLocationFunc(userSettingsViewModel) {
                navController.navigate("child_name_screen")
            }
        }
        composable("child_name_screen") {
            ChildNameFunc(
                { navController.navigate("child_schedule_screen") },
                { navController.popBackStack() }
            )
        }
        composable("child_schedule_screen") {
            ChildScheduleFunc(
                {  },
                { navController.popBackStack() }
            )
        }
    }
}