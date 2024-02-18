package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs.main_host

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.koin.androidx.compose.navigation.koinNavViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.Graphs
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.MainScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.functions.asStateWithLifecycle
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.main.main.MainScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.main.main.MainViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.main.notifications.NotificationScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.main.notifications.NotificationsViewModel

fun NavGraphBuilder.mainNavGraph() {
    navigation(
        route = Graphs.HostNested.Main.route,
        startDestination = MainScreenRoutes.Main.route
    ) {
        composable(MainScreenRoutes.Main.route) {
            val mainViewModel: MainViewModel = koinNavViewModel()
            MainScreen(
                screenState = mainViewModel.viewState.asStateWithLifecycle(),
                handleEvent = { mainViewModel.handleEvent(it) }
            )
        }
        composable(MainScreenRoutes.Notifications.route) {
            val viewModel: NotificationsViewModel = koinNavViewModel()
            NotificationScreen(
                screenState = viewModel.viewState.asStateWithLifecycle(),
                uiState = viewModel.uiState,
                handleEvent = viewModel::handleEvent
            )
        }
    }
}