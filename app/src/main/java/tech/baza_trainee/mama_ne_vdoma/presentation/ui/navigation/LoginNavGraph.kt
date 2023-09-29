package tech.baza_trainee.mama_ne_vdoma.presentation.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.EmailConfirmFunc
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.LoginUserFunc
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.NewPasswordFunc
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.RestorePasswordFunc
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.RestoreSuccessFunc
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
        composable("login_screen") {
            val loginViewModel: LoginScreenViewModel = it.sharedViewModel(navController)
            LoginUserFunc(
                loginViewModel,
                { navController.navigate("create_user_screen") },
                { navController.navigate("restore_password_screen") },
                {}
            )
        }
        composable("restore_password_screen") {
            val restorePasswordScreenViewModel: RestorePasswordScreenViewModel = it.sharedViewModel(navController)
            RestorePasswordFunc(
                restorePasswordScreenViewModel,
                { navController.popBackStack() },
                { navController.navigate("email_confirm_screen") },
            )
        }
        composable("email_confirm_screen") {
            EmailConfirmFunc(
                { navController.navigate("new_password_screen") },
                {},
            )
        }
        composable("new_password_screen") {
            val newPasswordScreenViewModel: NewPasswordScreenViewModel = it.sharedViewModel(navController)
            NewPasswordFunc(
                newPasswordScreenViewModel
            ) { navController.navigate("restore_success_screen") }
        }
        composable("restore_success_screen") {
            RestoreSuccessFunc { navController.navigate("start_graph") }
        }
    }
}