package tech.baza_trainee.mama_ne_vdoma.presentation.navigation

import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.koin.androidx.compose.navigation.koinNavViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.UserProfileRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.ChildInfoScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.ChildScheduleScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.ChildrenInfoScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.FullInfoScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.ImageCropScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.ParentScheduleScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.UserInfoScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.UserLocationScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.model.FullProfileEvent
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.vm.ChildInfoViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.vm.ChildScheduleViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.vm.ChildrenInfoViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.vm.FullInfoViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.vm.ParentScheduleViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.vm.UserInfoViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.vm.UserLocationViewModel
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
        composable(UserProfileRoutes.UserLocation.route) { _ ->
            val userLocationViewModel: UserLocationViewModel = koinNavViewModel()
            UserLocationScreen(
                screenState = userLocationViewModel.locationScreenState.collectAsStateWithLifecycle(),
                onHandleLocationEvent = { userLocationViewModel.handleUserLocationEvent(it) }
            ) {
                navController.navigate(UserProfileRoutes.ChildInfo.route)
            }
        }
        composable(UserProfileRoutes.ChildInfo.route) { _ ->
            val childInfoViewModel: ChildInfoViewModel = koinNavViewModel()
            ChildInfoScreen(
                screenState = childInfoViewModel.childInfoScreenState.collectAsStateWithLifecycle(),
                onHandleChildEvent = { childInfoViewModel.handleChildInfoEvent(it) },
                onNext = { navController.navigate(UserProfileRoutes.ChildSchedule.route,) },
                onBack = { navController.popBackStack() }
            )
        }
        composable(UserProfileRoutes.ChildSchedule.route) { _ ->
            val childScheduleViewModel: ChildScheduleViewModel = koinNavViewModel()
            ChildScheduleScreen(
                screenState = childScheduleViewModel.childScheduleViewState.collectAsStateWithLifecycle(),
                comment = childScheduleViewModel.childComment,
                onHandleScheduleEvent = { childScheduleViewModel.handleScheduleEvent(it) },
                { navController.navigate(UserProfileRoutes.ChildrenInfo.route) },
                { navController.popBackStack() }
            )
        }
        composable(UserProfileRoutes.ChildrenInfo.route) { _ ->
            val childrenInfoViewModel: ChildrenInfoViewModel = koinNavViewModel()
            ChildrenInfoScreen(
                screenState = childrenInfoViewModel.childrenInfoViewState.collectAsStateWithLifecycle(),
                onHandleChildrenInfoEvent = { childrenInfoViewModel.handleChildrenInfoEvent(it) },
                onNext = { navController.navigate(UserProfileRoutes.ParentSchedule.route) },
                onBack = { navController.popBackStack() },
                onEdit = { navController.navigate(UserProfileRoutes.ChildInfo.route) }
            )
        }
        composable(UserProfileRoutes.ParentSchedule.route) { _ ->
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
        composable(UserProfileRoutes.FullProfile.route) { _ ->
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
