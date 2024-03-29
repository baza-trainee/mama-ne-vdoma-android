package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.koin.androidx.compose.navigation.koinNavViewModel
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.Graphs
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.StandaloneGroupsRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.functions.asStateWithLifecycle
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.image_crop.ImageCropViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.choose_child.ChooseChildStandaloneScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.choose_child.ChooseChildStandaloneViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.create_group.CreateGroupScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.create_group.CreateGroupViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.found_group.FoundGroupScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.found_group.FoundGroupsStandaloneViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.image_crop.GroupImageCropScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.set_area.SetAreaForSearchScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.standalone.set_area.SetAreaViewModel

fun NavGraphBuilder.groupStandaloneScreensNavGraph() {
    navigation(
        route = Graphs.FirstGroupSearch.route,
        startDestination = StandaloneGroupsRoutes.ChooseChild().route
    ) {
        composable(
            route = StandaloneGroupsRoutes.ChooseChild().route,
            arguments = StandaloneGroupsRoutes.ChooseChild.argumentList
        ) { entry ->
            val (isForSearch) = StandaloneGroupsRoutes.ChooseChild.parseArguments(entry)
            val chooseChildViewModel: ChooseChildStandaloneViewModel = koinNavViewModel {
                parametersOf(isForSearch)
            }
            ChooseChildStandaloneScreen(
                isForSearch = isForSearch,
                screenState = chooseChildViewModel.viewState.asStateWithLifecycle(),
                uiState = chooseChildViewModel.uiState.asStateWithLifecycle(),
                handleEvent = { chooseChildViewModel.handleEvent(it) }
            )
        }
        composable(StandaloneGroupsRoutes.SetArea.route) {
            val setAreaViewModel: SetAreaViewModel = koinNavViewModel()
            SetAreaForSearchScreen(
                screenState = setAreaViewModel.viewState.asStateWithLifecycle(),
                uiState = setAreaViewModel.uiState.asStateWithLifecycle(),
                handleEvent = { setAreaViewModel.handleEvent(it) }
            )
        }
        composable(StandaloneGroupsRoutes.GroupsFound.route) {
            val foundGroupViewModel: FoundGroupsStandaloneViewModel = koinNavViewModel()
            FoundGroupScreen(
                screenState = foundGroupViewModel.viewState.asStateWithLifecycle(),
                uiState = foundGroupViewModel.uiState.asStateWithLifecycle(),
                handleEvent = { foundGroupViewModel.handleEvent(it) }
            )
        }
        composable(
            route = StandaloneGroupsRoutes.CreateGroup.route
        ) {
            val createGroupViewModel: CreateGroupViewModel = koinNavViewModel()
            CreateGroupScreen(
                screenState = createGroupViewModel.viewState.asStateWithLifecycle(),
                uiState = createGroupViewModel.uiState.asStateWithLifecycle(),
                handleEvent = { createGroupViewModel.handleEvent(it) }
            )
        }
        composable(StandaloneGroupsRoutes.GroupImageCrop.route) {
            val navigator = koinInject<ScreenNavigator>()
            val imageCropViewModel: ImageCropViewModel = koinNavViewModel {
                parametersOf(navigator)
            }
            GroupImageCropScreen(
                screenState = imageCropViewModel.viewState.asStateWithLifecycle(),
                handleEvent = { imageCropViewModel.handleEvent(it) }
            )
        }
    }
}