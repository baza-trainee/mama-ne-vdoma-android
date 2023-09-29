package tech.baza_trainee.mama_ne_vdoma.presentation.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.ChildInfoFunc
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.ChildScheduleFunc
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.ChildrenInfoFunc
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.CreateUserFunc
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.ImageCropFunc
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.ParentScheduleFunc
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.UserInfoFunc
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.UserLocationFunc
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.vm.UserCreateViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.vm.UserSettingsViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.sharedViewModel

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
                { navController.navigate("user_info_screen") }, //temp need replace with logic
                { navController.navigate("login_screen") }
            )
        }
        composable("user_info_screen") {
            val userSettingsViewModel: UserSettingsViewModel = it.sharedViewModel(navController)
            UserInfoFunc(
                userSettingsViewModel,
                { navController.navigate("user_location_screen") },
                { navController.navigate("image_crop_screen") }
            )
        }
        composable("image_crop_screen") {
            val userSettingsViewModel: UserSettingsViewModel = it.sharedViewModel(navController)
            ImageCropFunc(
                userSettingsViewModel
            ) { navController.navigate("user_info_screen") }
        }
        composable("user_location_screen") {
            val userSettingsViewModel: UserSettingsViewModel = it.sharedViewModel(navController)
            UserLocationFunc(userSettingsViewModel) {
                navController.navigate("child_name_screen")
            }
        }
        composable("child_name_screen") {
            val userSettingsViewModel: UserSettingsViewModel = it.sharedViewModel(navController)
            ChildInfoFunc(
                userSettingsViewModel,
                { navController.navigate("child_schedule_screen") },
                { navController.popBackStack() }
            )
        }
        composable("child_schedule_screen") {
            val userSettingsViewModel: UserSettingsViewModel = it.sharedViewModel(navController)
            ChildScheduleFunc(
                userSettingsViewModel,
                { navController.navigate("child_info_screen") },
                { navController.popBackStack() }
            )
        }
        composable("child_info_screen") {
            val userSettingsViewModel: UserSettingsViewModel = it.sharedViewModel(navController)
            ChildrenInfoFunc(
                userSettingsViewModel,
                { navController.navigate("parent_schedule_screen") },
                { navController.popBackStack() }
            )
        }
        composable("parent_schedule_screen") {
            val userSettingsViewModel: UserSettingsViewModel = it.sharedViewModel(navController)
            ParentScheduleFunc(
                userSettingsViewModel,
                {  },
                { navController.popBackStack() }
            )
        }
    }
}