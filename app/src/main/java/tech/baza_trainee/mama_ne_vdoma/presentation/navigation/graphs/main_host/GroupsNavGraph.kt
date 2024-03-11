package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs.main_host

import androidx.compose.runtime.collectAsState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.koin.androidx.compose.navigation.koinNavViewModel
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
    navigation(
        route = Graphs.HostNested.Groups.route,
        startDestination = GroupsScreenRoutes.Groups.route
    ) {
        composable(GroupsScreenRoutes.Groups.route) {
            val viewModel: MyGroupsViewModel = koinNavViewModel()
            MyGroupsScreen(
                screenState = viewModel.viewState.asStateWithLifecycle(),
                uiState = viewModel.uiState.collectAsState(),
                handleEvent = viewModel::handleEvent
            )
        }
        composable(GroupsScreenRoutes.UpdateGroup.route) {
            val viewModel: UpdateGroupViewModel = koinNavViewModel()
            UpdateGroupScreen(
                screenState = viewModel.viewState.asStateWithLifecycle(),
                uiState = viewModel.uiState.collectAsState(),
                handleEvent = viewModel::handleEvent
            )
        }
        composable(GroupsScreenRoutes.UpdateGroupAvatar.route) {
            val navigator = koinInject<PageNavigator>()
            val viewModel: ImageCropViewModel = koinNavViewModel {
                parametersOf(navigator)
            }
            UpdateGroupAvatarScreen(
                screenState = viewModel.viewState.asStateWithLifecycle(),
                handleEvent = viewModel::handleEvent
            )
        }
        composable(
            route = GroupsScreenRoutes.RateUser.ROUTE,
            arguments = GroupsScreenRoutes.RateUser.argumentList
        ) {
            val (userId) = GroupsScreenRoutes.RateUser.parseArguments(it)
            val viewModel: RateUserViewModel = koinNavViewModel {
                parametersOf(userId)
            }
            RateUserScreen(
                screenState = viewModel.viewState.asStateWithLifecycle(),
                uiState = viewModel.uiState.collectAsState(),
                handleEvent = viewModel::handleEvent
            )
        }
        composable(
            route = GroupsScreenRoutes.ViewReviews.ROUTE,
            arguments = GroupsScreenRoutes.ViewReviews.argumentList
        ) {
            val (userId) = GroupsScreenRoutes.ViewReviews.parseArguments(it)
            val viewModel: ViewReviewsViewModel = koinNavViewModel {
                parametersOf(userId)
            }
            ViewReviewsScreen(
                screenState = viewModel.viewState.asStateWithLifecycle(),
                uiState = viewModel.uiState.collectAsState(),
                handleEvent = viewModel::handleEvent
            )
        }
    }
}