package tech.baza_trainee.mama_ne_vdoma.presentation.navigation

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.koin.androidx.compose.navigation.koinNavViewModel
import org.koin.core.parameter.parametersOf
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
                uiState = userCreateViewModel.uiState,
                handleEvent = { userCreateViewModel.handleUserCreateEvent(it) },
                onCreateUser = { email, password -> navController.navigate(CreateUserRoute.VerifyEmail.getDestination(email, password)) },
                onLogin = { navController.navigate(Graphs.Login.route) },
                onBack = { navController.navigate(Graphs.Start.route) }
            )
        }
        composable(
            route = CreateUserRoute.VerifyEmail().route,
            arguments = CreateUserRoute.VerifyEmail.argumentList
        ) { entry ->
            val (email, password) = CreateUserRoute.VerifyEmail.parseArguments(entry)
            val verifyEmailViewModel: VerifyEmailViewModel = koinNavViewModel {
                parametersOf(email, password)
            }
            VerifyEmailScreen(
                screenState = verifyEmailViewModel.viewState.collectAsStateWithLifecycle(),
                uiState = verifyEmailViewModel.uiState,
                title = "Створити профіль",
                handleEvent = { verifyEmailViewModel.handleEvent(it) },
                onLogin = { navController.navigate(Graphs.UserProfile.route) },
                onBack = { navController.popBackStack() }
            )
        }
    }
}