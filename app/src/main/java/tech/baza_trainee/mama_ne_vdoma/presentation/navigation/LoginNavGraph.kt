package tech.baza_trainee.mama_ne_vdoma.presentation.navigation

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.LoginRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.EmailConfirmScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.LoginUserScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.NewPasswordScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.RestorePasswordScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.RestoreSuccessScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.vm.LoginScreenViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.vm.NewPasswordScreenViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.vm.RestorePasswordScreenViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.sharedViewModel

fun NavGraphBuilder.loginNavGraph(
    navController: NavHostController
) {
    navigation(
        route = Graphs.Login.route,
        startDestination = LoginRoutes.Login.route
    ) {
        composable(LoginRoutes.Login.route) { entry ->
            val loginViewModel: LoginScreenViewModel = entry.sharedViewModel(navController)
            LoginUserScreen(
                screenState = loginViewModel.viewState.collectAsStateWithLifecycle(),
                email = loginViewModel.email,
                password = loginViewModel.password,
                onHandleEvent = { loginViewModel.handleLoginEvent(it) },
                onCreateUser = { navController.navigate(Graphs.CreateUser.route) },
                onRestore = { navController.navigate(LoginRoutes.RestorePassword.route) },
                onLogin = { navController.navigate(Graphs.UserProfile.route) },
                onBack = { navController.navigate(Graphs.Start.route) }
            )
        }
        composable(LoginRoutes.RestorePassword.route) {
            val restorePasswordScreenViewModel: RestorePasswordScreenViewModel = it.sharedViewModel(navController)
            RestorePasswordScreen(
                viewModel = restorePasswordScreenViewModel,
                onBack = { navController.popBackStack() },
                onRestore = { navController.navigate(LoginRoutes.EmailConfirm.route) },
            )
        }
        composable(LoginRoutes.EmailConfirm.route) {
            EmailConfirmScreen(
                onLogin = { navController.navigate(LoginRoutes.NewPassword.route) },
                onSendAgain = {},
            )
        }
        composable(LoginRoutes.NewPassword.route) {
            val newPasswordScreenViewModel: NewPasswordScreenViewModel = it.sharedViewModel(navController)
            NewPasswordScreen(
                viewModel = newPasswordScreenViewModel
            ) { navController.navigate(LoginRoutes.RestoreSuccess.route) }
        }
        composable(LoginRoutes.RestoreSuccess.route) {
            RestoreSuccessScreen { navController.navigate(LoginRoutes.Login.route) }
        }
    }
}