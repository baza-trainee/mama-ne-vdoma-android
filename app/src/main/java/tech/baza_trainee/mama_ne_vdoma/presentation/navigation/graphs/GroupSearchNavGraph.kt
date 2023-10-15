package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.koin.androidx.compose.navigation.koinNavViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.Graphs
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.InitialGroupSearchRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.choose_child.ChooseChildStandaloneScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.choose_child.ChooseChildStandaloneViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.found_group.FoundGroupScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.found_group.FoundGroupsStandaloneViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.set_area.SetAreaForSearchScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.set_area.SetAreaViewModel

fun NavGraphBuilder.firstGroupSearchNavGraph() {
    navigation(
        route = Graphs.FirstGroupSearch.route,
        startDestination = InitialGroupSearchRoutes.ChooseChild.route
    ) {
        composable(InitialGroupSearchRoutes.ChooseChild.route) {
            val chooseChildViewModel: ChooseChildStandaloneViewModel = koinNavViewModel()
            ChooseChildStandaloneScreen(
                screenState = chooseChildViewModel.viewState.collectAsStateWithLifecycle(),
                uiState = chooseChildViewModel.uiState,
                handleEvent = { chooseChildViewModel.handleEvent(it) }
            )
        }
        composable(InitialGroupSearchRoutes.SetArea.route) {
            val setAreaViewModel: SetAreaViewModel = koinNavViewModel()
            SetAreaForSearchScreen(
                screenState = setAreaViewModel.viewState.collectAsStateWithLifecycle(),
                uiState = setAreaViewModel.uiState,
                handleEvent = { setAreaViewModel.handleEvent(it) }
            )
        }
        composable(InitialGroupSearchRoutes.GroupsFound.route) {
            val foundGroupViewModel: FoundGroupsStandaloneViewModel = koinNavViewModel()
            FoundGroupScreen(
                screenState = foundGroupViewModel.viewState.collectAsStateWithLifecycle(),
                uiState = foundGroupViewModel.uiState,
                handleEvent = { foundGroupViewModel.handleEvent(it) }
            )
        }
    }
}