package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.koin.androidx.compose.navigation.koinNavViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.Graphs
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.GroupSearchRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.choose_child.ChooseChildScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.set_area.SetAreaForSearchScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.set_area.SetAreaViewModel

fun NavGraphBuilder.groupSearchNavGraph() {
    navigation(
        route = Graphs.GroupSearch.route,
        startDestination = GroupSearchRoutes.ChooseChild.route
    ) {
        composable(GroupSearchRoutes.ChooseChild.route) {
            ChooseChildScreen()
        }
        composable(GroupSearchRoutes.SetArea.route) {
            val setAreaViewModel: SetAreaViewModel = koinNavViewModel()
            SetAreaForSearchScreen(
                screenState = setAreaViewModel.areaScreenState.collectAsStateWithLifecycle(),
                uiState = setAreaViewModel.uiState,
                handleEvent = { setAreaViewModel.handleSetAreaEvent(it) }
            )
        }
    }
}