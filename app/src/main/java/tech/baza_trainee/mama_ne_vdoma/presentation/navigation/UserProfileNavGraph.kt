package tech.baza_trainee.mama_ne_vdoma.presentation.navigation

import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.UserProfileRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.ChildInfoScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.ChildScheduleScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.ChildrenInfoScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.ImageCropScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.ParentScheduleScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.UserInfoScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.UserLocationScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.user_profile.vm.UserSettingsViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.decodeBitmap
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.extensions.sharedViewModel

fun NavGraphBuilder.userProfileGraph(
    navController: NavHostController
) {
    navigation(
        route = Graphs.UserProfile.route,
        startDestination = UserProfileRoutes.UserInfo.route
    ) {

        composable(UserProfileRoutes.UserInfo.route) { entry ->
            val userSettingsViewModel: UserSettingsViewModel = entry.sharedViewModel(navController)
            UserInfoScreen(
                screenState = userSettingsViewModel.userInfoScreenState.collectAsStateWithLifecycle(),
                onHandleUserInfoEvent = { userSettingsViewModel.handleUserInfoEvent(it)},
                onCreateUser = { navController.navigate(UserProfileRoutes.UserLocation.route) },
                onEditPhoto = { navController.navigate(UserProfileRoutes.ImageCrop.route) },
                onBack = { navController.navigate(Graphs.Start.route) }
            )
        }
        composable(UserProfileRoutes.ImageCrop.route) { entry ->
            val userSettingsViewModel: UserSettingsViewModel = entry.sharedViewModel(navController)

            val context = LocalContext.current
            val imageBitmap = userSettingsViewModel.uriForCrop.decodeBitmap(context.contentResolver).asImageBitmap()
            ImageCropScreen(
                imageForCrop = imageBitmap,
                onHandleCropEvent = { userSettingsViewModel.saveUserAvatar(it) }
            ) { navController.navigate(UserProfileRoutes.UserInfo.route) }
        }
        composable(UserProfileRoutes.UserLocation.route) { entry ->
            val userSettingsViewModel: UserSettingsViewModel = entry.sharedViewModel(navController)
            UserLocationScreen(
                screenState = userSettingsViewModel.locationScreenState.collectAsStateWithLifecycle(),
                onHandleLocationEvent = { userSettingsViewModel.handleUserLocationEvent(it) }
            ) {
                navController.navigate(UserProfileRoutes.ChildInfo.route)
            }
        }
        composable(UserProfileRoutes.ChildInfo.route) { entry ->
            val userSettingsViewModel: UserSettingsViewModel = entry.sharedViewModel(navController)
            ChildInfoScreen(
                screenState = userSettingsViewModel.childInfoScreenState.collectAsStateWithLifecycle(),
                onHandleChildEvent = { userSettingsViewModel.handleChildInfoEvent(it) },
                onNext = { navController.navigate(UserProfileRoutes.ChildSchedule.route,) },
                onBack = { navController.popBackStack() }
            )
        }
        composable(UserProfileRoutes.ChildSchedule.route) { entry ->
            val userSettingsViewModel: UserSettingsViewModel = entry.sharedViewModel(navController)
            ChildScheduleScreen(
                screenState = userSettingsViewModel.childScheduleViewState.collectAsStateWithLifecycle(),
                comment = userSettingsViewModel.childComment,
                onHandleScheduleEvent = { userSettingsViewModel.handleScheduleEvent(it) },
                { navController.navigate(UserProfileRoutes.ChildrenInfo.route) },
                { navController.popBackStack() }
            )
        }
        composable(UserProfileRoutes.ChildrenInfo.route) { entry ->
            val userSettingsViewModel: UserSettingsViewModel = entry.sharedViewModel(navController)
            ChildrenInfoScreen(
                screenState = userSettingsViewModel.childrenInfoViewState.collectAsStateWithLifecycle(),
                onHandleChildrenInfoEvent = { userSettingsViewModel.handleChildrenInfoEvent(it) },
                onNext = { navController.navigate(UserProfileRoutes.ParentSchedule.route) },
                onBack = { navController.popBackStack() },
                onEdit = { navController.navigate(UserProfileRoutes.ChildInfo.route) }
            )
        }
        composable(UserProfileRoutes.ParentSchedule.route) {
            val userSettingsViewModel: UserSettingsViewModel = it.sharedViewModel(navController)
            ParentScheduleScreen(
                userSettingsViewModel,
                { },
                { navController.popBackStack() }
            )
        }
    }
}
