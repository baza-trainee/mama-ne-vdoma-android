package tech.baza_trainee.mama_ne_vdoma.presentation.navigation

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
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
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.restore_password.RestorePasswordEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.restore_password.RestorePasswordScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.restore_password.RestorePasswordScreenViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.restore_success.RestoreSuccessScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.sharedViewModel

fun NavGraphBuilder.loginNavGraph(
    screenNavigator: ScreenNavigator?
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
                handleEvent = { loginViewModel.handleLoginEvent(it) },
                onCreateUser = { screenNavigator?.navigate(Graphs.CreateUser) },
                onRestore = { screenNavigator?.navigate(LoginRoutes.RestorePassword) },
                onLogin = { screenNavigator?.navigate(Graphs.UserProfile) },
                onBack = { screenNavigator?.navigate(Graphs.Start) }
            )
        }
        composable(LoginRoutes.RestorePassword.route) { entry ->
            val restorePasswordScreenViewModel: RestorePasswordScreenViewModel = entry.sharedViewModel(screenNavigator)
            RestorePasswordScreen(
                screenState = restorePasswordScreenViewModel.viewState.collectAsStateWithLifecycle(),
                uiState = restorePasswordScreenViewModel.uiState,
                handleEvent = { restorePasswordScreenViewModel.handleRestoreEvent(it) },
                onBack = { screenNavigator?.goBack() },
                onRestore = { email -> screenNavigator?.navigate(LoginRoutes.EmailConfirm.getDestination(email)) },
            )
        }
        composable(
            route = LoginRoutes.EmailConfirm().route,
            arguments = LoginRoutes.EmailConfirm.argumentList
        ) { entry ->
            val (email) = LoginRoutes.EmailConfirm.parseArguments(entry)
            val restorePasswordScreenViewModel: RestorePasswordScreenViewModel = entry.sharedViewModel(screenNavigator)
            EmailConfirmScreen(
                email = email,
                onLogin = {
                    screenNavigator?.navigate(LoginRoutes.VerifyEmail.getDestination(email, ""))
                },
                onSendAgain = {
                    restorePasswordScreenViewModel.handleRestoreEvent(RestorePasswordEvent.SendEmail)
                }
            )
        }
        composable(
            route = LoginRoutes.VerifyEmail().route,
            arguments = LoginRoutes.VerifyEmail.argumentList
        ) { entry ->
            val (email, password) = LoginRoutes.VerifyEmail.parseArguments(entry)
            val verifyEmailViewModel: VerifyEmailViewModel = koinNavViewModel {
                parametersOf(email, password)
            }
            VerifyEmailScreen(
                screenState = verifyEmailViewModel.viewState.collectAsStateWithLifecycle(),
                uiState = verifyEmailViewModel.uiState,
                title = "Відновлення паролю",
                handleEvent = { verifyEmailViewModel.handleEvent(it) },
                onRestore = { otp ->
                    screenNavigator?.navigate(LoginRoutes.NewPassword.getDestination(email, otp)) },
                onBack = { screenNavigator?.navigate(LoginRoutes.Login) }
            )
        }
        composable(
            route = LoginRoutes.NewPassword().route,
            arguments = LoginRoutes.NewPassword.argumentList
            ) { entry ->
            val (email, otp) = LoginRoutes.NewPassword.parseArguments(entry)
            val newPasswordScreenViewModel: NewPasswordScreenViewModel = koinNavViewModel {
                parametersOf(email, otp)
            }
            NewPasswordScreen(
                screenState = newPasswordScreenViewModel.viewState.collectAsStateWithLifecycle(),
                uiState = newPasswordScreenViewModel.uiState,
                handleEvent = { newPasswordScreenViewModel.handleNewPasswordEvent(it) },
                onRestore = { screenNavigator?.navigate(LoginRoutes.RestoreSuccess) },
                onBack = { screenNavigator?.navigate(LoginRoutes.Login) }
            )
        }
        composable(LoginRoutes.RestoreSuccess.route) {
            RestoreSuccessScreen { screenNavigator?.navigate(LoginRoutes.Login) }
        }
    }
}