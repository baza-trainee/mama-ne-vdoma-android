package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs.main_host

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.koin.androidx.compose.navigation.koinNavViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.Graphs
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.GroupsScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.functions.asStateWithLifecycle
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.my_groups.MyGroupsScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.my_groups.MyGroupsViewModel

fun NavGraphBuilder.groupNavGraph() {
    navigation(
        route = Graphs.HostNested.Groups.route,
        startDestination = GroupsScreenRoutes.Groups.route
    ) {
        composable(GroupsScreenRoutes.Groups.route) {
            val myGroupsViewModel: MyGroupsViewModel = koinNavViewModel()
            MyGroupsScreen(
                screenState = myGroupsViewModel.viewState.asStateWithLifecycle(),
                uiState = myGroupsViewModel.uiState,
                handleEvent = { myGroupsViewModel.handleEvent(it) }
            )
        }
    }
}