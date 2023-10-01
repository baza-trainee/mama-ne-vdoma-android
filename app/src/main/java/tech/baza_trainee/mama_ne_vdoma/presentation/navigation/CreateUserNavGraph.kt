package tech.baza_trainee.mama_ne_vdoma.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.CreateUserRoute
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.ChildInfoScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.ChildScheduleScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.ChildrenInfoScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.CreateUserScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.ImageCropScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.ParentScheduleScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.UserInfoScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.UserLocationScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.VerifyEmailScreen
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
            CreateUserScreen(
               viewModel =  userCreateViewModel,
                onCreateUser = { navController.navigate(CreateUserRoute.VerifyEmail.route) }, //temp need replace with logic
                onLogin = { navController.navigate("login_screen") }
            )
        }
        composable(CreateUserRoute.VerifyEmail.route) {
            val userCreateViewModel: UserCreateViewModel = it.sharedViewModel(navController)
            VerifyEmailScreen(
                viewModel = userCreateViewModel,
                onVerify = { navController.navigate(CreateUserRoute.UserInfo.route) }
            )
        }
        composable(CreateUserRoute.UserInfo.route) {
            val userSettingsViewModel: UserSettingsViewModel = it.sharedViewModel(navController)
            UserInfoScreen(
                viewModel = userSettingsViewModel,
                onCreateUser = { navController.navigate(CreateUserRoute.UserLocation.route) },
                onEditPhoto = { navController.navigate(CreateUserRoute.ImageCrop.route) }
            )
        }
        composable(CreateUserRoute.ImageCrop.route) {
            val userSettingsViewModel: UserSettingsViewModel = it.sharedViewModel(navController)
            ImageCropScreen(
                viewModel = userSettingsViewModel
            ) { navController.navigate(CreateUserRoute.UserInfo.route) }
        }
        composable(CreateUserRoute.UserLocation.route) {
            val userSettingsViewModel: UserSettingsViewModel = it.sharedViewModel(navController)
            UserLocationScreen(
                viewModel = userSettingsViewModel
            ) {
                navController.navigateWithArgs(CreateUserRoute.ChildInfo.route)
            }
        }
        composable(CreateUserRoute.ChildInfo.route) {
            val userSettingsViewModel: UserSettingsViewModel = it.sharedViewModel(navController)
            ChildInfoScreen(
                viewModel = userSettingsViewModel,
                onNext = { navController.navigate(CreateUserRoute.ChildSchedule.route,) },
                onBack = { navController.popBackStack() }
            )
        }
        composable(CreateUserRoute.ChildSchedule.route) {
            val userSettingsViewModel: UserSettingsViewModel = it.sharedViewModel(navController)
            ChildScheduleScreen(
                userSettingsViewModel,
                { navController.navigate(CreateUserRoute.ChildrenInfo.route) },
                { navController.popBackStack() }
            )
        }
        composable(CreateUserRoute.ChildrenInfo.route) { entry ->
            val userSettingsViewModel: UserSettingsViewModel = entry.sharedViewModel(navController)
            ChildrenInfoScreen(
                viewModel = userSettingsViewModel,
                onNext = { navController.navigate(CreateUserRoute.ParentSchedule.route) },
                onBack = { navController.popBackStack() },
                onEdit = { navController.navigate(CreateUserRoute.ChildInfo.route) }
            )
        }
        composable(CreateUserRoute.ParentSchedule.route) {
            val userSettingsViewModel: UserSettingsViewModel = it.sharedViewModel(navController)
            ParentScheduleScreen(
                userSettingsViewModel,
                {  },
                { navController.popBackStack() }
            )
        }
    }
}