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
        route = "create_user_graph",
        startDestination = CreateUserRoute.CreateUser.route
    ) {
        composable(CreateUserRoute.CreateUser.route) { entry ->
            val userCreateViewModel: UserCreateViewModel = entry.sharedViewModel(navController)
            CreateUserScreen(
                screenState = userCreateViewModel.userCreateViewState.collectAsStateWithLifecycle(),
                email = userCreateViewModel.email,
                password = userCreateViewModel.password,
                confirmPassword = userCreateViewModel.confirmPassword,
                onHandleEvent = { userCreateViewModel.handleUserCreateEvent(it) },
                onCreateUser = { navController.navigate(CreateUserRoute.VerifyEmail.route) }, //temp need replace with logic
                onLogin = { navController.navigate("login_graph") }
            )
        }
        composable(CreateUserRoute.VerifyEmail.route) { entry ->
            val userCreateViewModel: UserCreateViewModel = entry.sharedViewModel(navController)
            VerifyEmailScreen(
                screenState = userCreateViewModel.verifyEmailViewState.collectAsStateWithLifecycle(),
                otp = userCreateViewModel.otp,
                onHandleEvent = { userCreateViewModel.handleVerifyEmailEvent(it) },
                onVerify = { navController.navigate("user_profile_graph") }
            )
        }
    }
}