package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs.main_host

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.koin.androidx.compose.navigation.koinNavViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.Graphs
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.MainScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.main.MainScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.main.MainViewModel

fun NavGraphBuilder.mainNavGraph() {
    navigation(
        route = Graphs.HostNested.Main.route,
        startDestination = MainScreenRoutes.Main.route
    ) {
        composable(MainScreenRoutes.Main.route) {
            val mainViewModel: MainViewModel = koinNavViewModel()
            MainScreen(
                screenState = mainViewModel.viewState.collectAsStateWithLifecycle(),
                handleEvent = { mainViewModel.handleEvent(it) }
            )
        }
    }
}