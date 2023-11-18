package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.google.android.gms.auth.api.identity.SignInClient
import org.koin.androidx.compose.navigation.koinNavViewModel
import org.koin.compose.koinInject
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.CreateUserRoute
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.Graphs
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.functions.asStateWithLifecycle
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.verify_email.VerifyEmailScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.verify_email.VerifyEmailViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.create.UserCreateScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.create.UserCreateViewModel

fun NavGraphBuilder.createUserNavGraph() {
    navigation(
        route = Graphs.CreateUser.route,
        startDestination = CreateUserRoute.CreateUser.route
    ) {
        composable(CreateUserRoute.CreateUser.route) {
            val userCreateViewModel: UserCreateViewModel = koinNavViewModel()
            val oneTapClient: SignInClient = koinInject()
            UserCreateScreen(
                oneTapClient = oneTapClient,
                screenState = userCreateViewModel.viewState.asStateWithLifecycle(),
                uiState = userCreateViewModel.uiState,
                handleEvent = { userCreateViewModel.handleUserCreateEvent(it) }
            )
        }
        composable(CreateUserRoute.VerifyEmail.route) {
            val verifyEmailViewModel: VerifyEmailViewModel = koinNavViewModel()
            VerifyEmailScreen(
                screenState = verifyEmailViewModel.viewState.asStateWithLifecycle(),
                uiState = verifyEmailViewModel.uiState,
                title = "Створити профіль",
                handleEvent = { verifyEmailViewModel.handleEvent(it) }
            )
        }
    }
}