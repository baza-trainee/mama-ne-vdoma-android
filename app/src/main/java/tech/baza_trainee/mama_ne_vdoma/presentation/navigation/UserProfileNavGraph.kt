package tech.baza_trainee.mama_ne_vdoma.presentation.navigation

import androidx.compose.ui.graphics.asImageBitmap
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
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.children_info.ChildrenInfoScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.children_info.ChildrenInfoViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.full_info.FullInfoScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.full_info.FullInfoViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.full_info.FullProfileEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.schedule.child_schedule.ChildScheduleScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.schedule.child_schedule.ChildScheduleViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.schedule.parent_schedule.ParentScheduleScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.schedule.parent_schedule.ParentScheduleViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.user_info.ImageCropScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.user_info.UserInfoScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.user_info.UserInfoViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.user_location.UserLocationScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.user_location.UserLocationViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.sharedViewModel

fun NavGraphBuilder.userProfileGraph(
    navController: NavHostController
) {
    navigation(
        route = Graphs.UserProfile.route,
        startDestination = UserProfileRoutes.UserInfo.route
    ) {

        composable(UserProfileRoutes.UserInfo.route) { entry ->
            val userInfoViewModel: UserInfoViewModel = entry.sharedViewModel(navController)
            UserInfoScreen(
                screenState = userInfoViewModel.userInfoScreenState.collectAsStateWithLifecycle(),
                onHandleUserInfoEvent = { userInfoViewModel.handleUserInfoEvent(it)},
                onCreateUser = { navController.navigate(UserProfileRoutes.UserLocation.route) },
                onEditPhoto = { navController.navigate(UserProfileRoutes.ImageCrop.route) },
                onBack = { navController.navigate(Graphs.Start.route) }
            )
        }
        composable(UserProfileRoutes.ImageCrop.route) { entry ->
            val userInfoViewModel: UserInfoViewModel = entry.sharedViewModel(navController)
            ImageCropScreen(
                imageForCrop = userInfoViewModel.getUserAvatarBitmap().asImageBitmap(),
                onHandleCropEvent = { userInfoViewModel.saveUserAvatar(it) }
            ) { navController.navigate(UserProfileRoutes.UserInfo.route) }
        }
        composable(UserProfileRoutes.UserLocation.route) {
            val userLocationViewModel: UserLocationViewModel = koinNavViewModel()
            UserLocationScreen(
                screenState = userLocationViewModel.locationScreenState.collectAsStateWithLifecycle(),
                onHandleLocationEvent = { userLocationViewModel.handleUserLocationEvent(it) }
            ) {
                navController.navigate(UserProfileRoutes.ChildInfo.route)
            }
        }
        composable(UserProfileRoutes.ChildInfo.route) {
            val childInfoViewModel: ChildInfoViewModel = koinNavViewModel()
            ChildInfoScreen(
                screenState = childInfoViewModel.childInfoScreenState.collectAsStateWithLifecycle(),
                onHandleChildEvent = { childInfoViewModel.handleChildInfoEvent(it) },
                onNext = { navController.navigate(UserProfileRoutes.ChildSchedule.route,) },
                onBack = { navController.popBackStack() }
            )
        }
        composable(UserProfileRoutes.ChildSchedule.route) {
            val childScheduleViewModel: ChildScheduleViewModel = koinNavViewModel()
            ChildScheduleScreen(
                screenState = childScheduleViewModel.childScheduleViewState.collectAsStateWithLifecycle(),
                comment = childScheduleViewModel.childComment,
                onHandleScheduleEvent = { childScheduleViewModel.handleScheduleEvent(it) },
                { navController.navigate(UserProfileRoutes.ChildrenInfo.route) },
                { navController.popBackStack() }
            )
        }
        composable(UserProfileRoutes.ChildrenInfo.route) {
            val childrenInfoViewModel: ChildrenInfoViewModel = koinNavViewModel()
            ChildrenInfoScreen(
                screenState = childrenInfoViewModel.childrenInfoViewState.collectAsStateWithLifecycle(),
                onHandleChildrenInfoEvent = { childrenInfoViewModel.handleChildrenInfoEvent(it) },
                onNext = { navController.navigate(UserProfileRoutes.ParentSchedule.route) },
                onBack = { navController.popBackStack() },
                onEdit = { navController.navigate(UserProfileRoutes.ChildInfo.route) }
            )
        }
        composable(UserProfileRoutes.ParentSchedule.route) {
            val parentScheduleViewModel: ParentScheduleViewModel = koinNavViewModel()
            ParentScheduleScreen(
                screenState = parentScheduleViewModel.parentScheduleViewState.collectAsStateWithLifecycle(),
                comment = parentScheduleViewModel.parentComment,
                onHandleScheduleEvent = { parentScheduleViewModel.handleScheduleEvent(it) },
                onNext = {
                    parentScheduleViewModel.handleFullProfileEvent(FullProfileEvent.UpdateFullProfile)
                    navController.navigate(UserProfileRoutes.FullProfile.route)
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(UserProfileRoutes.FullProfile.route) {
            val fullInfoViewModel: FullInfoViewModel = koinNavViewModel()
            FullInfoScreen(
                screenState = fullInfoViewModel.fullInfoViewState.collectAsStateWithLifecycle(),
                onHandleEvent = { fullInfoViewModel.handleFullProfileEvent(it) },
                onBack = { navController.popBackStack() },
                onNext = {},
                onEdit = {}
            )
        }
    }
}
