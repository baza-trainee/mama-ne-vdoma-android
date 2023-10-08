package tech.baza_trainee.mama_ne_vdoma.presentation.navigation

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.koin.androidx.compose.navigation.koinNavViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.Graphs
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.UserProfileRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.child_info.ChildInfoScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.child_info.ChildInfoViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.full_info.FullInfoScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.full_info.FullInfoViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.image_crop.ImageCropScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.image_crop.ImageCropViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.schedule.child_schedule.ChildScheduleScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.schedule.child_schedule.ChildScheduleViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.schedule.parent_schedule.ParentScheduleScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.schedule.parent_schedule.ParentScheduleViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.user_info.UserInfoScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.user_info.UserInfoViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.user_location.UserLocationScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.user_location.UserLocationViewModel

fun NavGraphBuilder.userProfileGraph(
    screenNavigator: ScreenNavigator?
) {
    navigation(
        route = Graphs.UserProfile.route,
        startDestination = UserProfileRoutes.FullProfile.route
    ) {
        composable(UserProfileRoutes.FullProfile.route) {
            val fullInfoViewModel: FullInfoViewModel = koinNavViewModel()
            FullInfoScreen(
                screenState = fullInfoViewModel.fullInfoViewState.collectAsStateWithLifecycle(),
                handleEvent = { fullInfoViewModel.handleFullProfileEvent(it) },
                onBack = { screenNavigator?.navigate(Graphs.Login) },
                onNext = {},
                onEditUser = { screenNavigator?.navigate(UserProfileRoutes.UserInfo) },
                onAddChild = { screenNavigator?.navigate(UserProfileRoutes.ChildInfo) },
                onEditChild = { screenNavigator?.navigate(UserProfileRoutes.ChildSchedule) },
                onDelete = { screenNavigator?.navigate(Graphs.CreateUser) }
            )
        }
        composable(UserProfileRoutes.UserInfo.route) {
            val userInfoViewModel: UserInfoViewModel = koinNavViewModel()
            UserInfoScreen(
                screenState = userInfoViewModel.userInfoScreenState.collectAsStateWithLifecycle(),
                uiState = userInfoViewModel.uiState,
                handleEvent = { userInfoViewModel.handleUserInfoEvent(it)},
                onNext = { screenNavigator?.navigate(UserProfileRoutes.UserLocation) },
                onEditPhoto = { screenNavigator?.navigate(UserProfileRoutes.ImageCrop) }
            )
        }
        composable(UserProfileRoutes.ImageCrop.route) {
            val imageCropViewModel: ImageCropViewModel = koinNavViewModel()
            ImageCropScreen(
                imageForCrop = imageCropViewModel.getUserAvatarBitmap(),
                onHandleCropEvent = { imageCropViewModel.saveCroppedImage(it) }
            ) { screenNavigator?.navigate(UserProfileRoutes.UserInfo) }
        }
        composable(UserProfileRoutes.UserLocation.route) {
            val userLocationViewModel: UserLocationViewModel = koinNavViewModel()
            UserLocationScreen(
                screenState = userLocationViewModel.locationScreenState.collectAsStateWithLifecycle(),
                uiState = userLocationViewModel.uiState,
                handleEvent = { userLocationViewModel.handleUserLocationEvent(it) }
            ) { screenNavigator?.navigate(UserProfileRoutes.ParentSchedule) }
        }
        composable(UserProfileRoutes.ParentSchedule.route) {
            val parentScheduleViewModel: ParentScheduleViewModel = koinNavViewModel()
            ParentScheduleScreen(
                screenState = parentScheduleViewModel.parentScheduleViewState.collectAsStateWithLifecycle(),
                uiState = parentScheduleViewModel.uiState,
                handleEvent = { parentScheduleViewModel.handleScheduleEvent(it) },
                onNext = { screenNavigator?.navigate(UserProfileRoutes.FullProfile) },
                onBack = { screenNavigator?.goBack() }
            )
        }
        composable(UserProfileRoutes.ChildInfo.route) {
            val childInfoViewModel: ChildInfoViewModel = koinNavViewModel()
            ChildInfoScreen(
                screenState = childInfoViewModel.childInfoScreenState.collectAsStateWithLifecycle(),
                uiState = childInfoViewModel.uiState,
                handleEvent = { childInfoViewModel.handleChildInfoEvent(it) },
                onNext = { screenNavigator?.navigate(UserProfileRoutes.ChildSchedule) },
                onBack = { screenNavigator?.navigate(UserProfileRoutes.FullProfile) }
            )
        }
        composable(UserProfileRoutes.ChildSchedule.route) {
            val childScheduleViewModel: ChildScheduleViewModel = koinNavViewModel()
            ChildScheduleScreen(
                screenState = childScheduleViewModel.childScheduleViewState.collectAsStateWithLifecycle(),
                uiState = childScheduleViewModel.uiState,
                comment = childScheduleViewModel.childComment,
                handleEvent = { childScheduleViewModel.handleScheduleEvent(it) },
                onNext = { screenNavigator?.navigate(UserProfileRoutes.FullProfile) }
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
