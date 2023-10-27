package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.koin.androidx.compose.navigation.koinNavViewModel
import org.koin.core.parameter.parametersOf
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.Graphs
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.LoginRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.verify_email.VerifyEmailScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.verify_email.VerifyEmailViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.email_confirm.EmailConfirmScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.login.LoginScreenViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.login.LoginUserScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.new_password.NewPasswordScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.new_password.NewPasswordScreenViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.restore_password.RestorePasswordScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.restore_password.RestorePasswordScreenViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.restore_success.RestoreSuccessScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.sharedViewModel

fun NavGraphBuilder.loginNavGraph(
    navHostController: NavHostController
) {
    navigation(
        route = Graphs.Login.route,
        startDestination = LoginRoutes.Login.route
    ) {
        composable(LoginRoutes.Login.route) {
            val loginViewModel: LoginScreenViewModel = koinNavViewModel()
            LoginUserScreen(
                screenState = loginViewModel.viewState.collectAsStateWithLifecycle(),
                uiState = loginViewModel.uiState,
                handleEvent = { loginViewModel.handleLoginEvent(it) }
            )
        }
        composable(LoginRoutes.RestorePassword.route) { entry ->
            val restorePasswordScreenViewModel: RestorePasswordScreenViewModel = entry.sharedViewModel(navHostController)
            RestorePasswordScreen(
                screenState = restorePasswordScreenViewModel.viewState.collectAsStateWithLifecycle(),
                uiState = restorePasswordScreenViewModel.uiState,
                handleEvent = { restorePasswordScreenViewModel.handleRestoreEvent(it) }
            )
        }
        composable(
            route = LoginRoutes.EmailConfirm().route,
            arguments = LoginRoutes.EmailConfirm.argumentList
        ) { entry ->
            val (email, password) = LoginRoutes.EmailConfirm.parseArguments(entry)
            val restorePasswordScreenViewModel: RestorePasswordScreenViewModel = entry.sharedViewModel(navHostController)
            EmailConfirmScreen(
                email = email,
                password = password,
                handleEvent = { restorePasswordScreenViewModel.handleRestoreEvent(it) }
            )
        }
        composable(
            route = LoginRoutes.VerifyEmail().route,
            arguments = LoginRoutes.VerifyEmail.argumentList
        ) { entry ->
            val (email, password) = LoginRoutes.VerifyEmail.parseArguments(entry)
            val verifyEmailViewModel: VerifyEmailViewModel = koinNavViewModel {
                parametersOf(true, email, password)
            }
            VerifyEmailScreen(
                screenState = verifyEmailViewModel.viewState.collectAsStateWithLifecycle(),
                uiState = verifyEmailViewModel.uiState,
                title = "Відновлення паролю",
                handleEvent = { verifyEmailViewModel.handleEvent(it) }
            )
        }
        composable(
            route = LoginRoutes.NewPassword().route,
            arguments = LoginRoutes.NewPassword.argumentList
            ) { entry ->
            val (email) = LoginRoutes.NewPassword.parseArguments(entry)
            val newPasswordScreenViewModel: NewPasswordScreenViewModel = koinNavViewModel {
                parametersOf(email)
            }
            NewPasswordScreen(
                screenState = newPasswordScreenViewModel.viewState.collectAsStateWithLifecycle(),
                uiState = newPasswordScreenViewModel.uiState,
                handleEvent = { newPasswordScreenViewModel.handleNewPasswordEvent(it) }
            )
        }
        composable(LoginRoutes.RestoreSuccess.route) {
            RestoreSuccessScreen { navHostController.navigate(Graphs.Start.route) }
        }
    }
}