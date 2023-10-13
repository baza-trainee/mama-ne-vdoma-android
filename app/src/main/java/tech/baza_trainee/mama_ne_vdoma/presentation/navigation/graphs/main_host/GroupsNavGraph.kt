package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs.main_host

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.koin.androidx.compose.navigation.koinNavViewModel
import org.koin.core.parameter.parametersOf
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.Graphs
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.GroupsScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.choose_child.ChooseChildScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.choose_child.ChooseChildScreenViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.create_group.CreateGroupScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.create_group.CreateGroupScreenViewModel

fun NavGraphBuilder.groupNavGraph() {
    navigation(
        route = Graphs.Host.Groups.route,
        startDestination = GroupsScreenRoutes.Groups.route
    ) {
        composable(GroupsScreenRoutes.ChooseChild.route) {
            val chooseChildScreenViewModel: ChooseChildScreenViewModel = koinNavViewModel()
            ChooseChildScreen(
                screenState = chooseChildScreenViewModel.viewState.collectAsStateWithLifecycle(),
                uiState = chooseChildScreenViewModel.uiState,
                handleEvent = { chooseChildScreenViewModel.handleEvent(it) }
            )
        }
        composable(
            route = GroupsScreenRoutes.CreateGroup().route,
            arguments = GroupsScreenRoutes.CreateGroup.argumentList
        ) { entry ->
            val (child) = GroupsScreenRoutes.CreateGroup.parseArguments(entry)
            val createGroupScreenViewModel: CreateGroupScreenViewModel = koinNavViewModel {
                parametersOf(child)
            }
            CreateGroupScreen(
                screenState = createGroupScreenViewModel.viewState.collectAsStateWithLifecycle(),
                uiState = createGroupScreenViewModel.uiState,
                handleEvent = { createGroupScreenViewModel.handleEvent(it) }
            )
        }
    }
}