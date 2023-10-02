package tech.baza_trainee.mama_ne_vdoma.presentation.navigation

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
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
        route = "login_graph",
        startDestination = "login_screen"
    ) {
        composable("login_screen") { entry ->
            val loginViewModel: LoginScreenViewModel = entry.sharedViewModel(navController)
            LoginUserScreen(
                screenState = loginViewModel.viewState.collectAsStateWithLifecycle(),
                email = loginViewModel.email,
                password = loginViewModel.password,
                onHandleEvent = { loginViewModel.handleLoginEvent(it) },
                onCreateUser = { navController.navigate("create_user_screen") },
                onRestore = { navController.navigate("restore_password_screen") },
                onLogin = { navController.navigate("user_profile_graph") }
            )
        }
        composable("restore_password_screen") {
            val restorePasswordScreenViewModel: RestorePasswordScreenViewModel = it.sharedViewModel(navController)
            RestorePasswordScreen(
                viewModel = restorePasswordScreenViewModel,
                onBack = { navController.popBackStack() },
                onRestore = { navController.navigate("email_confirm_screen") },
            )
        }
        composable("email_confirm_screen") {
            EmailConfirmScreen(
                onLogin = { navController.navigate("new_password_screen") },
                onSendAgain = {},
            )
        }
        composable("new_password_screen") {
            val newPasswordScreenViewModel: NewPasswordScreenViewModel = it.sharedViewModel(navController)
            NewPasswordScreen(
                viewModel = newPasswordScreenViewModel
            ) { navController.navigate("restore_success_screen") }
        }
        composable("restore_success_screen") {
            RestoreSuccessScreen { navController.navigate("start_graph") }
        }
    }
}