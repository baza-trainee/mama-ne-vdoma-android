package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs

import androidx.credentials.CredentialManager
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.CreateUserRoute
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.Graphs
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.functions.asStateWithLifecycle
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.verify_email.VerifyEmailScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.verify_email.VerifyEmailViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.create.UserCreateScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.create.UserCreateViewModel

fun NavGraphBuilder.createUserNavGraph() {
    navigation<Graphs.CreateUser>(
        startDestination = CreateUserRoute.CreateUser
    ) {
        composable<CreateUserRoute.CreateUser> {
            val userCreateViewModel: UserCreateViewModel = koinViewModel()
            val credentialManager: CredentialManager = koinInject()
            UserCreateScreen(
                credentialManager = credentialManager,
                screenState = userCreateViewModel.viewState.asStateWithLifecycle(),
                uiState = userCreateViewModel.uiState.asStateWithLifecycle(),
                handleEvent = { userCreateViewModel.handleUserCreateEvent(it) }
            )
        }
        composable<CreateUserRoute.VerifyEmail> {
            val verifyEmailViewModel: VerifyEmailViewModel = koinViewModel()
            VerifyEmailScreen(
                screenState = verifyEmailViewModel.viewState.asStateWithLifecycle(),
                uiState = verifyEmailViewModel.uiState.asStateWithLifecycle(),
                title = R.string.title_create_user_profile,
                handleEvent = { verifyEmailViewModel.handleEvent(it) }
            )
        }
    }
}
