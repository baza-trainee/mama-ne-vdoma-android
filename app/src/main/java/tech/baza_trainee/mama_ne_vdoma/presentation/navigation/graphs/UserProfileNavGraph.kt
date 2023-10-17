package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.koin.androidx.compose.navigation.koinNavViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.Graphs
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.UserProfileRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.child_info.ChildInfoScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.child_info.ChildInfoViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.full_info.FullInfoScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.full_info.FullInfoViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.image_crop.UserImageCropScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.schedule.child_schedule.ChildScheduleScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.schedule.child_schedule.ChildScheduleViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.schedule.parent_schedule.ParentScheduleScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.schedule.parent_schedule.ParentScheduleViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.user_info.UserInfoScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.user_info.UserInfoViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.user_location.UserLocationScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.user_location.UserLocationViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.sharedViewModel

fun NavGraphBuilder.userProfileGraph(navController: NavHostController) {
    navigation(
        route = Graphs.UserProfile.route,
        startDestination = UserProfileRoutes.FullProfile.route
    ) {
        composable(UserProfileRoutes.FullProfile.route) {
            val fullInfoViewModel: FullInfoViewModel = koinNavViewModel()
            FullInfoScreen(
                screenState = fullInfoViewModel.fullInfoViewState.collectAsStateWithLifecycle(),
                uiState = fullInfoViewModel.uiState,
                handleEvent = { fullInfoViewModel.handleFullProfileEvent(it) }
            )
        }
        composable(UserProfileRoutes.UserInfo.route) {
            val userInfoViewModel: UserInfoViewModel = it.sharedViewModel(navController = navController)
            UserInfoScreen(
                screenState = userInfoViewModel.userInfoScreenState.collectAsStateWithLifecycle(),
                uiState = userInfoViewModel.uiState,
                handleEvent = { userInfoViewModel.handleUserInfoEvent(it)}
            )
        }
        composable(UserProfileRoutes.ImageCrop.route) {
            val userInfoViewModel: UserInfoViewModel = it.sharedViewModel(navController = navController)
            UserImageCropScreen(
                imageForCrop = userInfoViewModel.getUserAvatarBitmap(),
                handleEvent = { userInfoViewModel.handleUserInfoEvent(it) }
            )
        }
        composable(UserProfileRoutes.UserLocation.route) {
            val userLocationViewModel: UserLocationViewModel = koinNavViewModel()
            UserLocationScreen(
                screenState = userLocationViewModel.locationScreenState.collectAsStateWithLifecycle(),
                uiState = userLocationViewModel.uiState,
                handleEvent = { userLocationViewModel.handleUserLocationEvent(it) }
            )
        }
        composable(UserProfileRoutes.ParentSchedule.route) {
            val parentScheduleViewModel: ParentScheduleViewModel = koinNavViewModel()
            ParentScheduleScreen(
                screenState = parentScheduleViewModel.parentScheduleViewState.collectAsStateWithLifecycle(),
                uiState = parentScheduleViewModel.uiState,
                handleEvent = { parentScheduleViewModel.handleScheduleEvent(it) }
            )
        }
        composable(UserProfileRoutes.ChildInfo.route) {
            val childInfoViewModel: ChildInfoViewModel = koinNavViewModel()
            ChildInfoScreen(
                screenState = childInfoViewModel.childInfoScreenState.collectAsStateWithLifecycle(),
                uiState = childInfoViewModel.uiState,
                handleEvent = { childInfoViewModel.handleChildInfoEvent(it) }
            )
        }
        composable(UserProfileRoutes.ChildSchedule.route) {
            val childScheduleViewModel: ChildScheduleViewModel = koinNavViewModel()
            ChildScheduleScreen(
                screenState = childScheduleViewModel.childScheduleViewState.collectAsStateWithLifecycle(),
                uiState = childScheduleViewModel.uiState,
                comment = childScheduleViewModel.childComment,
                handleEvent = { childScheduleViewModel.handleScheduleEvent(it) }
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
