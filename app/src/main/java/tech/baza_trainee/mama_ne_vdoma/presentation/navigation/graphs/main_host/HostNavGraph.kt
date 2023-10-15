package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs.main_host

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.koin.androidx.compose.navigation.koinNavViewModel
import org.koin.core.parameter.parametersOf
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.Graphs
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.HostScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.host.HostScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.host.HostViewModel

fun NavGraphBuilder.hostNavGraph() {
    navigation(
        route = Graphs.Host.route,
        startDestination = HostScreenRoutes.Host().route
    ) {
        composable(
            route = HostScreenRoutes.Host().route,
            arguments = HostScreenRoutes.Host.argumentList
        ) { entry ->
            val (page) = HostScreenRoutes.Host.parseArguments(entry)
            val hostViewModel: HostViewModel = koinNavViewModel {
                parametersOf(page)
            }
            HostScreen(
                navigator = hostViewModel.screenNavigator,
                screenState = hostViewModel.viewState.collectAsStateWithLifecycle(),
                handleEvent = { hostViewModel.handleEvent(it) }
            )
        }
    }
}