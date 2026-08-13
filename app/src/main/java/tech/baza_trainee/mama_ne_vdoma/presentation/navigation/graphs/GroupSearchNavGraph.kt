package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import androidx.navigation.navigation
import org.koin.androidx.compose.koinViewModel
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
    navigation<Graphs.FirstGroupSearch>(
        startDestination = StandaloneGroupsRoutes.ChooseChild()
    ) {
        composable<StandaloneGroupsRoutes.ChooseChild> { entry ->
            val (isForSearch) = entry.toRoute<StandaloneGroupsRoutes.ChooseChild>()
            val chooseChildViewModel: ChooseChildStandaloneViewModel = koinViewModel {
                parametersOf(isForSearch)
            }
            ChooseChildStandaloneScreen(
                isForSearch = isForSearch,
                screenState = chooseChildViewModel.viewState.asStateWithLifecycle(),
                uiState = chooseChildViewModel.uiState.asStateWithLifecycle(),
                handleEvent = { chooseChildViewModel.handleEvent(it) }
            )
        }
        composable<StandaloneGroupsRoutes.SetArea> {
            val setAreaViewModel: SetAreaViewModel = koinViewModel()
            SetAreaForSearchScreen(
                screenState = setAreaViewModel.viewState.asStateWithLifecycle(),
                uiState = setAreaViewModel.uiState.asStateWithLifecycle(),
                handleEvent = { setAreaViewModel.handleEvent(it) }
            )
        }
        composable<StandaloneGroupsRoutes.GroupsFound> {
            val foundGroupViewModel: FoundGroupsStandaloneViewModel = koinViewModel()
            FoundGroupScreen(
                screenState = foundGroupViewModel.viewState.asStateWithLifecycle(),
                uiState = foundGroupViewModel.uiState.asStateWithLifecycle(),
                handleEvent = { foundGroupViewModel.handleEvent(it) }
            )
        }
        composable<StandaloneGroupsRoutes.CreateGroup> {
            val createGroupViewModel: CreateGroupViewModel = koinViewModel()
            CreateGroupScreen(
                screenState = createGroupViewModel.viewState.asStateWithLifecycle(),
                uiState = createGroupViewModel.uiState.asStateWithLifecycle(),
                handleEvent = { createGroupViewModel.handleEvent(it) }
            )
        }
        composable<StandaloneGroupsRoutes.GroupImageCrop> {
            val navigator = koinInject<ScreenNavigator>()
            val imageCropViewModel: ImageCropViewModel = koinViewModel {
                parametersOf(navigator)
            }
            GroupImageCropScreen(
                screenState = imageCropViewModel.viewState.asStateWithLifecycle(),
                handleEvent = { imageCropViewModel.handleEvent(it) }
            )
        }
    }
}