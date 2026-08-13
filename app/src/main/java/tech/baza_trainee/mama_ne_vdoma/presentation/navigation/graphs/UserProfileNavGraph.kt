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
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.UserProfileRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.functions.asStateWithLifecycle
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
    navigation<Graphs.UserProfile>(
        startDestination = UserProfileRoutes.FullProfile
    ) {
        composable<UserProfileRoutes.FullProfile> {
            val fullInfoViewModel: FullInfoViewModel = koinViewModel()
            FullInfoScreen(
                screenState = fullInfoViewModel.viewState.asStateWithLifecycle(),
                uiState = fullInfoViewModel.uiState.asStateWithLifecycle(),
                handleEvent = { fullInfoViewModel.handleFullProfileEvent(it) }
            )
        }
        composable<UserProfileRoutes.UserInfo> {
            val userInfoViewModel: UserInfoViewModel = koinViewModel()
            UserInfoScreen(
                screenState = userInfoViewModel.viewState.asStateWithLifecycle(),
                uiState = userInfoViewModel.uiState.asStateWithLifecycle(),
                handleEvent = { userInfoViewModel.handleUserInfoEvent(it)}
            )
        }
        composable<UserProfileRoutes.ImageCrop> {
            val navigator = koinInject<ScreenNavigator>()
            val imageCropViewModel: ImageCropViewModel = koinViewModel {
                parametersOf(navigator)
            }
            UserImageCropScreen(
                screenState = imageCropViewModel.viewState.asStateWithLifecycle(),
                handleEvent = { imageCropViewModel.handleEvent(it) }
            )
        }
        composable<UserProfileRoutes.UserLocation> {
            val userLocationViewModel: UserLocationViewModel = koinViewModel()
            UserLocationScreen(
                screenState = userLocationViewModel.viewState.asStateWithLifecycle(),
                uiState = userLocationViewModel.uiState.asStateWithLifecycle(),
                handleEvent = { userLocationViewModel.handleUserLocationEvent(it) }
            )
        }
        composable<UserProfileRoutes.ParentSchedule> {
            val parentScheduleViewModel: ParentScheduleViewModel = koinViewModel()
            ParentScheduleScreen(
                screenState = parentScheduleViewModel.viewState.asStateWithLifecycle(),
                uiState = parentScheduleViewModel.uiState.asStateWithLifecycle(),
                handleEvent = { parentScheduleViewModel.handleScheduleEvent(it) }
            )
        }
        composable<UserProfileRoutes.ChildInfo> {
            val navigator: ScreenNavigator = koinInject()
            val childInfoViewModel: ChildInfoViewModel = koinViewModel {
                parametersOf(
                    { navigator.navigate(UserProfileRoutes.ChildSchedule) },
                    { navigator.navigate(UserProfileRoutes.FullProfile) }
                )
            }
            ChildInfoScreen(
                screenState = childInfoViewModel.viewState.asStateWithLifecycle(),
                uiState = childInfoViewModel.uiState.asStateWithLifecycle(),
                handleEvent = { childInfoViewModel.handleChildInfoEvent(it) }
            )
        }
        composable<UserProfileRoutes.ChildSchedule> {
            val navigator: ScreenNavigator = koinInject()
            val childScheduleViewModel: ChildScheduleViewModel = koinViewModel {
                parametersOf(
                    { navigator.navigate(UserProfileRoutes.FullProfile) },
                    { navigator.navigate(UserProfileRoutes.FullProfile) }
                )
            }
            ChildScheduleScreen(
                screenState = childScheduleViewModel.viewState.asStateWithLifecycle(),
                uiState = childScheduleViewModel.uiState.asStateWithLifecycle(),
                handleEvent = { childScheduleViewModel.handleScheduleEvent(it) }
            )
        }
        composable<UserProfileRoutes.UserCreateSuccess> { entry ->
            val navigator: ScreenNavigator = koinInject()
            val (name) = entry.toRoute<UserProfileRoutes.UserCreateSuccess>()
            UserCreateSuccessScreen(
                name = name,
                onNext = { navigator.navigate(StandaloneGroupsRoutes.ChooseChild(isForSearch = true)) },
                onBack = { navigator.goBack() }
            )
        }
//        composable<UserProfileRoutes.ChildrenInfo> {
//            val childrenInfoViewModel: ChildrenInfoViewModel = koinViewModel()
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
