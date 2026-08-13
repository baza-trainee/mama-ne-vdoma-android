package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs.main_host

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import androidx.navigation.navigation
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.Graphs
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.HostScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.functions.asStateWithLifecycle
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.host.HostScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.host.HostViewModel

fun NavGraphBuilder.hostNavGraph() {
    navigation<Graphs.Host>(
        startDestination = HostScreenRoutes.Host()
    ) {
        composable<HostScreenRoutes.Host> { entry ->
            val (page) = entry.toRoute<HostScreenRoutes.Host>()
            val hostViewModel: HostViewModel = koinViewModel {
                parametersOf(page)
            }
            HostScreen(
                navigator = hostViewModel.screenNavigator,
                screenState = hostViewModel.viewState.asStateWithLifecycle(),
                uiState = hostViewModel.uiState.asStateWithLifecycle(),
                handleEvent = { hostViewModel.handleEvent(it) }
            )
        }
    }
}