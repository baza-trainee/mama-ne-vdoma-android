package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs.main_host

import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.Graphs
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.GroupsScreenRoutes

fun NavGraphBuilder.groupNavGraph() {
    navigation(
        route = Graphs.Host.Groups.route,
        startDestination = GroupsScreenRoutes.Groups.route
    ) {
//        composable(GroupsScreenRoutes.Groups.route) {
//            MainScreen()
//        }
    }
}