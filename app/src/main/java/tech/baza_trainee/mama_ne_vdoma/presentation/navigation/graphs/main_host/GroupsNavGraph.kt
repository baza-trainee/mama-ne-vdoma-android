package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs.main_host

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import androidx.navigation.navigation
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.PageNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.Graphs
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.GroupsScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.functions.asStateWithLifecycle
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.image_crop.ImageCropViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.image_crop.UpdateGroupAvatarScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.my_groups.MyGroupsScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.my_groups.MyGroupsViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.rate_user.RateUserScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.rate_user.RateUserViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.update_group.UpdateGroupScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.update_group.UpdateGroupViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.view_reviews.ViewReviewsScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.groups.view_reviews.ViewReviewsViewModel

fun NavGraphBuilder.groupNavGraph() {
    navigation<Graphs.HostNested.Groups>(
        startDestination = GroupsScreenRoutes.Groups
    ) {
        composable<GroupsScreenRoutes.Groups> {
            val viewModel: MyGroupsViewModel = koinViewModel()
            MyGroupsScreen(
                screenState = viewModel.viewState.asStateWithLifecycle(),
                uiState = viewModel.uiState.asStateWithLifecycle(),
                handleEvent = viewModel::handleEvent
            )
        }
        composable<GroupsScreenRoutes.UpdateGroup> {
            val viewModel: UpdateGroupViewModel = koinViewModel()
            UpdateGroupScreen(
                screenState = viewModel.viewState.asStateWithLifecycle(),
                uiState = viewModel.uiState.asStateWithLifecycle(),
                handleEvent = viewModel::handleEvent
            )
        }
        composable<GroupsScreenRoutes.UpdateGroupAvatar> {
            val navigator = koinInject<PageNavigator>()
            val viewModel: ImageCropViewModel = koinViewModel {
                parametersOf(navigator)
            }
            UpdateGroupAvatarScreen(
                screenState = viewModel.viewState.asStateWithLifecycle(),
                handleEvent = viewModel::handleEvent
            )
        }
        composable<GroupsScreenRoutes.RateUser> {
            val (userId) = it.toRoute<GroupsScreenRoutes.RateUser>()
            val viewModel: RateUserViewModel = koinViewModel {
                parametersOf(userId)
            }
            RateUserScreen(
                screenState = viewModel.viewState.asStateWithLifecycle(),
                uiState = viewModel.uiState.asStateWithLifecycle(),
                handleEvent = viewModel::handleEvent
            )
        }
        composable<GroupsScreenRoutes.ViewReviews> {
            val (userId) = it.toRoute<GroupsScreenRoutes.ViewReviews>()
            val viewModel: ViewReviewsViewModel = koinViewModel {
                parametersOf(userId)
            }
            ViewReviewsScreen(
                screenState = viewModel.viewState.asStateWithLifecycle(),
                uiState = viewModel.uiState.asStateWithLifecycle(),
                handleEvent = viewModel::handleEvent
            )
        }
    }
}