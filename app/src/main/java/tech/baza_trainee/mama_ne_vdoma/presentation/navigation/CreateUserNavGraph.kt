package tech.baza_trainee.mama_ne_vdoma.presentation.navigation

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.CreateUserRoute
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.CreateUserScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.VerifyEmailScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.vm.UserCreateViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.sharedViewModel

fun NavGraphBuilder.createUserNavGraph(
    navController: NavHostController
) {
    navigation(
        route = Graphs.CreateUser.route,
        startDestination = CreateUserRoute.CreateUser.route
    ) {
        composable(CreateUserRoute.CreateUser.route) { entry ->
            val userCreateViewModel: UserCreateViewModel = entry.sharedViewModel(navController)
            CreateUserScreen(
                screenState = userCreateViewModel.userCreateViewState.collectAsStateWithLifecycle(),
                onHandleEvent = { userCreateViewModel.handleUserCreateEvent(it) },
                onCreateUser = { navController.navigate(CreateUserRoute.VerifyEmail.route) },
                onLogin = { navController.navigate(Graphs.Login.route) },
                onBack = { navController.navigate(Graphs.Start.route) }
            )
        }
        composable(CreateUserRoute.VerifyEmail.route) { entry ->
            val userCreateViewModel: UserCreateViewModel = entry.sharedViewModel(navController)
            VerifyEmailScreen(
                screenState = userCreateViewModel.verifyEmailViewState.collectAsStateWithLifecycle(),
                onHandleEvent = { userCreateViewModel.handleVerifyEmailEvent(it) },
                onSuccess = { navController.navigate(Graphs.UserProfile.route) }
            )
        }
    }
}