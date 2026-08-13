package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import androidx.navigation.navigation
import androidx.credentials.CredentialManager
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.Graphs
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.LoginRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.functions.asStateWithLifecycle
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.verify_email.VerifyEmailScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.verify_email.VerifyEmailViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.email_confirm.EmailConfirmScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.login.LoginUserScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.login.LoginViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.new_password.NewPasswordScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.new_password.NewPasswordScreenViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.restore_password.RestorePasswordScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.restore_password.RestorePasswordScreenViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.restore_success.RestoreSuccessScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.sharedViewModel

fun NavGraphBuilder.loginNavGraph(
    navHostController: NavHostController
) {
    navigation<Graphs.Login>(
        startDestination = LoginRoutes.Login
    ) {
        composable<LoginRoutes.Login> {
            val loginViewModel: LoginViewModel = koinViewModel()
            val credentialManager: CredentialManager = koinInject()
            LoginUserScreen(
                credentialManager = credentialManager,
                screenState = loginViewModel.viewState.asStateWithLifecycle(),
                uiState = loginViewModel.uiState.asStateWithLifecycle(),
                handleEvent = { loginViewModel.handleLoginEvent(it) }
            )
        }
        composable<LoginRoutes.RestorePassword> { entry ->
            val restorePasswordScreenViewModel: RestorePasswordScreenViewModel =
                entry.sharedViewModel(navHostController)
            RestorePasswordScreen(
                screenState = restorePasswordScreenViewModel.viewState.asStateWithLifecycle(),
                uiState = restorePasswordScreenViewModel.uiState.asStateWithLifecycle(),
                handleEvent = { restorePasswordScreenViewModel.handleRestoreEvent(it) }
            )
        }
        composable<LoginRoutes.EmailConfirm> { entry ->
            val (email, password) = entry.toRoute<LoginRoutes.EmailConfirm>()
            val restorePasswordScreenViewModel: RestorePasswordScreenViewModel =
                entry.sharedViewModel(navHostController)
            EmailConfirmScreen(
                uiState = restorePasswordScreenViewModel.uiState.asStateWithLifecycle(),
                email = email,
                password = password,
                handleEvent = { restorePasswordScreenViewModel.handleRestoreEvent(it) }
            )
        }
        composable<LoginRoutes.VerifyEmail> {
            val verifyEmailViewModel: VerifyEmailViewModel = koinViewModel()
            VerifyEmailScreen(
                screenState = verifyEmailViewModel.viewState.asStateWithLifecycle(),
                uiState = verifyEmailViewModel.uiState.asStateWithLifecycle(),
                title = R.string.title_restore_password,
                handleEvent = { verifyEmailViewModel.handleEvent(it) }
            )
        }
        composable<LoginRoutes.NewPassword> {
            val newPasswordScreenViewModel: NewPasswordScreenViewModel = koinViewModel()
            NewPasswordScreen(
                screenState = newPasswordScreenViewModel.viewState.asStateWithLifecycle(),
                uiState = newPasswordScreenViewModel.uiState.asStateWithLifecycle(),
                handleEvent = { newPasswordScreenViewModel.handleNewPasswordEvent(it) }
            )
        }
        composable<LoginRoutes.RestoreSuccess> {
            RestoreSuccessScreen { navHostController.navigate(Graphs.Start) }
        }
    }
}