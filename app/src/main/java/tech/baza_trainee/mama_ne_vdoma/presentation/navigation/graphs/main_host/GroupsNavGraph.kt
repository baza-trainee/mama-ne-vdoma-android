package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs.main_host

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.Graphs
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.GroupsScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.create_group.CreateGroupScreen

fun NavGraphBuilder.groupNavGraph() {
    navigation(
        route = Graphs.Host.Groups.route,
        startDestination = GroupsScreenRoutes.Groups.route
    ) {
        composable(GroupsScreenRoutes.Groups.route) {
            CreateGroupScreen()
        }
    }
}