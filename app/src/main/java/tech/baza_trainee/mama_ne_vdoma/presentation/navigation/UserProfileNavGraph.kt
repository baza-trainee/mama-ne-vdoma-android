package tech.baza_trainee.mama_ne_vdoma.presentation.navigation

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
    navController: NavHostController
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
                onBack = { navController.navigate(Graphs.Login.route) },
                onNext = {},
                onEditUser = { navController.navigate(UserProfileRoutes.UserInfo.route) },
                onAddChild = { navController.navigate(UserProfileRoutes.ChildInfo.route) },
                onEditChild = { navController.navigate(UserProfileRoutes.ChildSchedule.route) },
                onDelete = { navController.navigate(Graphs.CreateUser.route) }
            )
        }
        composable(UserProfileRoutes.UserInfo.route) {
            val userInfoViewModel: UserInfoViewModel = koinNavViewModel()
            UserInfoScreen(
                screenState = userInfoViewModel.userInfoScreenState.collectAsStateWithLifecycle(),
                uiState = userInfoViewModel.uiState,
                handleEvent = { userInfoViewModel.handleUserInfoEvent(it)},
                onNext = { navController.navigate(UserProfileRoutes.UserLocation.route) },
                onEditPhoto = { navController.navigate(UserProfileRoutes.ImageCrop.route) }
            )
        }
        composable(UserProfileRoutes.ImageCrop.route) {
            val imageCropViewModel: ImageCropViewModel = koinNavViewModel()
            ImageCropScreen(
                imageForCrop = imageCropViewModel.getUserAvatarBitmap(),
                onHandleCropEvent = { imageCropViewModel.saveCroppedImage(it) }
            ) { navController.navigate(UserProfileRoutes.UserInfo.route) }
        }
        composable(UserProfileRoutes.UserLocation.route) {
            val userLocationViewModel: UserLocationViewModel = koinNavViewModel()
            UserLocationScreen(
                screenState = userLocationViewModel.locationScreenState.collectAsStateWithLifecycle(),
                uiState = userLocationViewModel.uiState,
                handleEvent = { userLocationViewModel.handleUserLocationEvent(it) }
            ) { navController.navigate(UserProfileRoutes.ParentSchedule.route) }
        }
        composable(UserProfileRoutes.ParentSchedule.route) {
            val parentScheduleViewModel: ParentScheduleViewModel = koinNavViewModel()
            ParentScheduleScreen(
                screenState = parentScheduleViewModel.parentScheduleViewState.collectAsStateWithLifecycle(),
                uiState = parentScheduleViewModel.uiState,
                handleEvent = { parentScheduleViewModel.handleScheduleEvent(it) },
                onNext = { navController.navigate(UserProfileRoutes.FullProfile.route) },
                onBack = { navController.popBackStack() }
            )
        }
        composable(UserProfileRoutes.ChildInfo.route) {
            val childInfoViewModel: ChildInfoViewModel = koinNavViewModel()
            ChildInfoScreen(
                screenState = childInfoViewModel.childInfoScreenState.collectAsStateWithLifecycle(),
                uiState = childInfoViewModel.uiState,
                handleEvent = { childInfoViewModel.handleChildInfoEvent(it) },
                onNext = { navController.navigate(UserProfileRoutes.ChildSchedule.route,) },
                onBack = { navController.navigate(UserProfileRoutes.FullProfile.route) }
            )
        }
        composable(UserProfileRoutes.ChildSchedule.route) {
            val childScheduleViewModel: ChildScheduleViewModel = koinNavViewModel()
            ChildScheduleScreen(
                screenState = childScheduleViewModel.childScheduleViewState.collectAsStateWithLifecycle(),
                uiState = childScheduleViewModel.uiState,
                comment = childScheduleViewModel.childComment,
                handleEvent = { childScheduleViewModel.handleScheduleEvent(it) },
                onNext = { navController.navigate(UserProfileRoutes.FullProfile.route) }
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
