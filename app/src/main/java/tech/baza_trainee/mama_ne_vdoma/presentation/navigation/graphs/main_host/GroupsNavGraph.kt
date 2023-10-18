package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs.main_host

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.koin.androidx.compose.navigation.koinNavViewModel
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.Graphs
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.GroupsScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.image_crop.ImageCropViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.choose_child.ChooseChildScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.choose_child.ChooseChildViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.create_group.CreateGroupScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.create_group.CreateGroupViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.image_crop.GroupImageCropScreen
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
                screenState = myGroupsViewModel.viewState.collectAsStateWithLifecycle(),
                uiState = myGroupsViewModel.uiState,
                handleEvent = { myGroupsViewModel.handleEvent(it) }
            )
        }
        composable(GroupsScreenRoutes.ChooseChild.route) {
            val chooseChildViewModel: ChooseChildViewModel = koinNavViewModel()
            ChooseChildScreen(
                screenState = chooseChildViewModel.viewState.collectAsStateWithLifecycle(),
                uiState = chooseChildViewModel.uiState,
                handleEvent = { chooseChildViewModel.handleEvent(it) }
            )
        }
        composable(
            route = GroupsScreenRoutes.CreateGroup().route,
            arguments = GroupsScreenRoutes.CreateGroup.argumentList
        ) { entry ->
            val (child) = GroupsScreenRoutes.CreateGroup.parseArguments(entry)
            val createGroupViewModel: CreateGroupViewModel = koinNavViewModel {
                parametersOf(child)
            }
            CreateGroupScreen(
                screenState = createGroupViewModel.viewState.collectAsStateWithLifecycle(),
                uiState = createGroupViewModel.uiState,
                handleEvent = { createGroupViewModel.handleEvent(it) }
            )
        }
        composable(GroupsScreenRoutes.ImageCrop.route) {
            val navigator = koinInject<ScreenNavigator>()
            val imageCropViewModel: ImageCropViewModel = koinNavViewModel {
                parametersOf(navigator)
            }
            GroupImageCropScreen(
                imageForCrop = imageCropViewModel.getUserAvatarBitmap(),
                handleEvent = { imageCropViewModel.saveCroppedImage(it) }
            )
        }
    }
}