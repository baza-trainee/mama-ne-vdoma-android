package tech.baza_trainee.mama_ne_vdoma.presentation.navigation

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
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.restore_password.RestorePasswordEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.restore_password.RestorePasswordScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.restore_password.RestorePasswordScreenViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.login.restore_success.RestoreSuccessScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.sharedViewModel

fun NavGraphBuilder.loginNavGraph(
    navController: NavHostController
) {
    navigation(
        route = Graphs.Login.route,
        startDestination = LoginRoutes.Login.route
    ) {
        composable(LoginRoutes.Login.route) {
            val loginViewModel: LoginScreenViewModel = koinNavViewModel()
            LoginUserScreen(
                screenState = loginViewModel.viewState.collectAsStateWithLifecycle(),
                onHandleEvent = { loginViewModel.handleLoginEvent(it) },
                onCreateUser = { navController.navigate(Graphs.CreateUser.route) },
                onRestore = { navController.navigate(LoginRoutes.RestorePassword.route) },
                onLogin = { navController.navigate(Graphs.UserProfile.route) },
                onBack = { navController.navigate(Graphs.Start.route) }
            )
        }
        composable(LoginRoutes.RestorePassword.route) { entry ->
            val restorePasswordScreenViewModel: RestorePasswordScreenViewModel = entry.sharedViewModel(navController)
            RestorePasswordScreen(
                screenState = restorePasswordScreenViewModel.viewState.collectAsStateWithLifecycle(),
                onHandleEvent = { restorePasswordScreenViewModel.handleRestoreEvent(it) },
                onBack = { navController.popBackStack() },
                onRestore = { email -> navController.navigate(LoginRoutes.EmailConfirm.getDestination(email)) },
            )
        }
        composable(
            route = LoginRoutes.EmailConfirm().route,
            arguments = LoginRoutes.EmailConfirm.argumentList
        ) { entry ->
            val (email) = LoginRoutes.EmailConfirm.parseArguments(entry)
            val restorePasswordScreenViewModel: RestorePasswordScreenViewModel = entry.sharedViewModel(navController)
            EmailConfirmScreen(
                email = email,
                onLogin = {
                    navController.navigate(LoginRoutes.VerifyEmail.getDestination(email, ""))
                },
                onSendAgain = {
                    restorePasswordScreenViewModel.handleRestoreEvent(RestorePasswordEvent.SendEmail)
                },
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
                title = "Відновлення паролю",
                onHandleEvent = { verifyEmailViewModel.handleEvent(it) },
                onSuccess = { otp ->
                    navController.navigate(LoginRoutes.NewPassword.getDestination(email, otp)) }
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
                onHandleEvent = { newPasswordScreenViewModel.handleNewPasswordEvent(it) },
            ) { navController.navigate(LoginRoutes.RestoreSuccess.route) }
        }
        composable(LoginRoutes.RestoreSuccess.route) {
            RestoreSuccessScreen { navController.navigate(LoginRoutes.Login.route) }
        }
    }
}