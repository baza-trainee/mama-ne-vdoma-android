package tech.baza_trainee.mama_ne_vdoma.presentation.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.navigation.routes.CreateUserRoute
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
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.navigateWithArgs
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.sharedViewModel

fun NavGraphBuilder.createUserNavGraph(
    navController: NavHostController
) {

    navigation(
        route = "create_user_graph",
        startDestination = CreateUserRoute.CreateUser.route
    ) {
        composable(CreateUserRoute.CreateUser.route) {
            val userCreateViewModel: UserCreateViewModel = it.sharedViewModel(navController)
            CreateUserFunc(
                userCreateViewModel,
                { navController.navigate(CreateUserRoute.UserInfo.route) }, //temp need replace with logic
                { navController.navigate("login_screen") }
            )
        }
        composable(CreateUserRoute.UserInfo.route) {
            val userSettingsViewModel: UserSettingsViewModel = it.sharedViewModel(navController)
            UserInfoFunc(
                userSettingsViewModel,
                { navController.navigate(CreateUserRoute.UserLocation.route) },
                { navController.navigate(CreateUserRoute.ImageCrop.route) }
            )
        }
        composable(CreateUserRoute.ImageCrop.route) {
            val userSettingsViewModel: UserSettingsViewModel = it.sharedViewModel(navController)
            ImageCropFunc(
                userSettingsViewModel
            ) { navController.navigate(CreateUserRoute.UserInfo.route) }
        }
        composable(CreateUserRoute.UserLocation.route) {
            val userSettingsViewModel: UserSettingsViewModel = it.sharedViewModel(navController)
            UserLocationFunc(userSettingsViewModel) {
                navController.navigateWithArgs(CreateUserRoute.ChildInfo.route)
            }
        }
        composable(CreateUserRoute.ChildInfo.route) {
            val userSettingsViewModel: UserSettingsViewModel = it.sharedViewModel(navController)
            ChildInfoFunc(
                userSettingsViewModel,
                { navController.navigate(CreateUserRoute.ChildSchedule.route,) },
                { navController.popBackStack() }
            )
        }
        composable(CreateUserRoute.ChildSchedule.route) {
            val userSettingsViewModel: UserSettingsViewModel = it.sharedViewModel(navController)
            ChildScheduleFunc(
                userSettingsViewModel,
                { navController.navigate(CreateUserRoute.ChildrenInfo.route) },
                { navController.popBackStack() }
            )
        }
        composable(CreateUserRoute.ChildrenInfo.route) { entry ->
            val userSettingsViewModel: UserSettingsViewModel = entry.sharedViewModel(navController)
            ChildrenInfoFunc(
                userSettingsViewModel,
                { navController.navigate(CreateUserRoute.ParentSchedule.route) },
                { navController.popBackStack() },
                { navController.navigate(CreateUserRoute.ChildInfo.route) }
            )
        }
        composable(CreateUserRoute.ParentSchedule.route) {
            val userSettingsViewModel: UserSettingsViewModel = it.sharedViewModel(navController)
            ParentScheduleFunc(
                userSettingsViewModel,
                {  },
                { navController.popBackStack() }
            )
        }
    }
}