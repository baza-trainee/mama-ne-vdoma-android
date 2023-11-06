package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.koin.androidx.compose.navigation.koinNavViewModel
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.ScreenNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.Graphs
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.StandaloneGroupsRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.UserProfileRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.add_child.ChildInfoViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.child_schedule.ChildScheduleViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.image_crop.ImageCropViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.child_info.ChildInfoScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.full_info.FullInfoScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.full_info.FullInfoViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.image_crop.UserImageCropScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.schedule.child_schedule.ChildScheduleScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.schedule.parent_schedule.ParentScheduleScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.schedule.parent_schedule.ParentScheduleViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.success.UserCreateSuccessScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.user_info.UserInfoScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.user_info.UserInfoViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.user_location.UserLocationScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.user_location.UserLocationViewModel

fun NavGraphBuilder.userProfileGraph() {
    navigation(
        route = Graphs.UserProfile.route,
        startDestination = UserProfileRoutes.FullProfile.route
    ) {
        composable(UserProfileRoutes.FullProfile.route) {
            val fullInfoViewModel: FullInfoViewModel = koinNavViewModel()
            FullInfoScreen(
                screenState = fullInfoViewModel.viewState.collectAsStateWithLifecycle(),
                uiState = fullInfoViewModel.uiState,
                handleEvent = { fullInfoViewModel.handleFullProfileEvent(it) }
            )
        }
        composable(UserProfileRoutes.UserInfo.route) {
            val userInfoViewModel: UserInfoViewModel = koinNavViewModel()
            UserInfoScreen(
                screenState = userInfoViewModel.viewState.collectAsStateWithLifecycle(),
                uiState = userInfoViewModel.uiState,
                handleEvent = { userInfoViewModel.handleUserInfoEvent(it)}
            )
        }
        composable(UserProfileRoutes.ImageCrop.route) {
            val navigator = koinInject<ScreenNavigator>()
            val imageCropViewModel: ImageCropViewModel = koinNavViewModel {
                parametersOf(navigator)
            }
            UserImageCropScreen(
                screenState = imageCropViewModel.viewState.collectAsStateWithLifecycle(),
                handleEvent = { imageCropViewModel.handleEvent(it) }
            )
        }
        composable(UserProfileRoutes.UserLocation.route) {
            val userLocationViewModel: UserLocationViewModel = koinNavViewModel()
            UserLocationScreen(
                screenState = userLocationViewModel.viewState.collectAsStateWithLifecycle(),
                uiState = userLocationViewModel.uiState,
                handleEvent = { userLocationViewModel.handleUserLocationEvent(it) }
            )
        }
        composable(UserProfileRoutes.ParentSchedule.route) {
            val parentScheduleViewModel: ParentScheduleViewModel = koinNavViewModel()
            ParentScheduleScreen(
                screenState = parentScheduleViewModel.viewState.collectAsStateWithLifecycle(),
                uiState = parentScheduleViewModel.uiState,
                handleEvent = { parentScheduleViewModel.handleScheduleEvent(it) }
            )
        }
        composable(UserProfileRoutes.ChildInfo.route) {
            val navigator: ScreenNavigator = koinInject()
            val childInfoViewModel: ChildInfoViewModel = koinNavViewModel {
                parametersOf(
                    { navigator.navigate(UserProfileRoutes.ChildSchedule) },
                    { navigator.navigate(UserProfileRoutes.FullProfile) }
                )
            }
            ChildInfoScreen(
                screenState = childInfoViewModel.viewState.collectAsStateWithLifecycle(),
                events = childInfoViewModel.events,
                handleEvent = { childInfoViewModel.handleChildInfoEvent(it) }
            )
        }
        composable(UserProfileRoutes.ChildSchedule.route) {
            val navigator: ScreenNavigator = koinInject()
            val childScheduleViewModel: ChildScheduleViewModel = koinNavViewModel {
                parametersOf(
                    { navigator.navigate(UserProfileRoutes.FullProfile) },
                    { navigator.navigate(UserProfileRoutes.FullProfile) }
                )
            }
            ChildScheduleScreen(
                screenState = childScheduleViewModel.viewState.collectAsStateWithLifecycle(),
                uiState = childScheduleViewModel.uiState,
                handleEvent = { childScheduleViewModel.handleScheduleEvent(it) }
            )
        }
        composable(
            route = UserProfileRoutes.UserCreateSuccess().route,
            arguments = UserProfileRoutes.UserCreateSuccess.argumentList
        ) { entry ->
            val navigator: ScreenNavigator = koinInject()
            val (name) = UserProfileRoutes.UserCreateSuccess.parseArguments(entry)
            UserCreateSuccessScreen(
                name = name,
                onNext = { navigator.navigate(StandaloneGroupsRoutes.ChooseChild.getDestination(isForSearch = true)) },
                onBack = { navigator.goBack() }
            )
        }
//        composable(UserProfileRoutes.ChildrenInfo.route) {
//            val childrenInfoViewModel: ChildrenInfoViewModel = koinNavViewModel()
//            ChildrenInfoScreen(
//                screenState = childrenInfoViewModel.childrenInfoViewState.collectAsStateWithLifecycle(),
//                onHandleChildrenInfoEvent = { childrenInfoViewModel.handleChildrenInfoEvent(it) },
//                onNext = { navController.navigate(UserProfileRoutes.ParentSchedule.route) },
//                onBack = { navController.popBackStack() },
//                onEdit = { navController.navigate(UserProfileRoutes.ChildInfo.route) }
//            )
//        }
    }
}
