package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs.main_host

import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.Graphs
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.SettingsScreenRoutes

fun NavGraphBuilder.settingsNavGraph() {
    navigation(
        route = Graphs.HostNested.Settings.route,
        startDestination = SettingsScreenRoutes.Settings.route
    ) {
//        composable(SettingsScreenRoutes.Settings.route) {
//            MainScreen()
//        }
    }
}