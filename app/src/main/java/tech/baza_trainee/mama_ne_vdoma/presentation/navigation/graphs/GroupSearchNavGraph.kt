package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.Graphs
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.GroupSearchRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.search_group.ChooseChildScreen

fun NavGraphBuilder.groupSearchNavGraph(
    navHostController: NavHostController
) {
    navigation(
        route = Graphs.GroupSearch.route,
        startDestination = GroupSearchRoutes.ChooseChild.route
    ) {
        composable(GroupSearchRoutes.ChooseChild.route) {
            ChooseChildScreen()
        }
    }
}