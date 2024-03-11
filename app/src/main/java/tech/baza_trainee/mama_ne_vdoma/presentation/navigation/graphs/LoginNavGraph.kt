package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs

import androidx.compose.runtime.collectAsState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.google.android.gms.auth.api.identity.SignInClient
import org.koin.androidx.compose.navigation.koinNavViewModel
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
    navigation(
        route = Graphs.Login.route,
        startDestination = LoginRoutes.Login.route
    ) {
        composable(LoginRoutes.Login.route) {
            val loginViewModel: LoginViewModel = koinNavViewModel()
            val oneTapClient: SignInClient = koinInject()
            LoginUserScreen(
                oneTapClient = oneTapClient,
                screenState = loginViewModel.viewState.asStateWithLifecycle(),
                uiState = loginViewModel.uiState.collectAsState(),
                handleEvent = { loginViewModel.handleLoginEvent(it) }
            )
        }
        composable(LoginRoutes.RestorePassword.route) { entry ->
            val restorePasswordScreenViewModel: RestorePasswordScreenViewModel = entry.sharedViewModel(navHostController)
            RestorePasswordScreen(
                screenState = restorePasswordScreenViewModel.viewState.asStateWithLifecycle(),
                uiState = restorePasswordScreenViewModel.uiState.collectAsState(),
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
                uiState = restorePasswordScreenViewModel.uiState.collectAsState(),
                email = email,
                password = password,
                handleEvent = { restorePasswordScreenViewModel.handleRestoreEvent(it) }
            )
        }
        composable(LoginRoutes.VerifyEmail.route) {
            val verifyEmailViewModel: VerifyEmailViewModel = koinNavViewModel()
            VerifyEmailScreen(
                screenState = verifyEmailViewModel.viewState.asStateWithLifecycle(),
                uiState = verifyEmailViewModel.uiState.collectAsState(),
                title = R.string.title_restore_password,
                handleEvent = { verifyEmailViewModel.handleEvent(it) }
            )
        }
        composable(LoginRoutes.NewPassword.route) {
            val newPasswordScreenViewModel: NewPasswordScreenViewModel = koinNavViewModel()
            NewPasswordScreen(
                screenState = newPasswordScreenViewModel.viewState.asStateWithLifecycle(),
                uiState = newPasswordScreenViewModel.uiState.collectAsState(),
                handleEvent = { newPasswordScreenViewModel.handleNewPasswordEvent(it) }
            )
        }
        composable(LoginRoutes.RestoreSuccess.route) {
            RestoreSuccessScreen { navHostController.navigate(Graphs.Start.route) }
        }
    }
}