package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs.main_host

import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.Graphs
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.SearchScreenRoutes

fun NavGraphBuilder.searchNavGraph() {
    navigation(
        route = Graphs.Host.Search.route,
        startDestination = SearchScreenRoutes.Search.route
    ) {
//        composable(GroupsScreenRoutes.Groups.route) {
//            MainScreen()
//        }
    }
}