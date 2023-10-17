package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs.main_host

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.koin.androidx.compose.navigation.koinNavViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.Graphs
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.SettingsScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.main.ProfileSettingsScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.main.ProfileSettingsViewModel

fun NavGraphBuilder.settingsNavGraph() {
    navigation(
        route = Graphs.HostNested.Settings.route,
        startDestination = SettingsScreenRoutes.Settings.route
    ) {
        composable(SettingsScreenRoutes.Settings.route) {
            val viewModel: ProfileSettingsViewModel = koinNavViewModel()
            ProfileSettingsScreen(
                screenState = viewModel.viewState.collectAsStateWithLifecycle(),
                uiState = viewModel.uiState,
                handleEvent = {viewModel.handleEvent(it) }
            )
        }
    }
}