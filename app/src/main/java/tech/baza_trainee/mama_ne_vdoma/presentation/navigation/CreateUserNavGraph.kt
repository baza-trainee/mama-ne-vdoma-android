package tech.baza_trainee.mama_ne_vdoma.presentation.navigation

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.koin.androidx.compose.navigation.koinNavViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.CreateUserRoute
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.Graphs
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.verify_email.VerifyEmailScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.verify_email.VerifyEmailViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.CreateUserScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.create_user.UserCreateViewModel

fun NavGraphBuilder.createUserNavGraph(
    navController: NavHostController
) {
    navigation(
        route = Graphs.CreateUser.route,
        startDestination = CreateUserRoute.CreateUser.route
    ) {
        composable(CreateUserRoute.CreateUser.route) {
            val userCreateViewModel: UserCreateViewModel = koinNavViewModel()
            CreateUserScreen(
                screenState = userCreateViewModel.viewState.collectAsStateWithLifecycle(),
                onHandleEvent = { userCreateViewModel.handleUserCreateEvent(it) },
                onCreateUser = { navController.navigate(CreateUserRoute.VerifyEmail.route) },
                onLogin = { navController.navigate(Graphs.Login.route) },
                onBack = { navController.navigate(Graphs.Start.route) }
            )
        }
        composable(CreateUserRoute.VerifyEmail.route) {
            val verifyEmailViewModel: VerifyEmailViewModel = koinNavViewModel()
            VerifyEmailScreen(
                screenState = verifyEmailViewModel.viewState.collectAsStateWithLifecycle(),
                title = "Створити профіль",
                onHandleEvent = { verifyEmailViewModel.handleEvent(it) },
                onSuccess = { navController.navigate(Graphs.UserProfile.route) }
            )
        }
    }
}