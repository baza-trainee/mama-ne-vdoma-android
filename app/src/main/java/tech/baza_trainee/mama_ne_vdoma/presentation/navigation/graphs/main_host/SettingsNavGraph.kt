package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs.main_host

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.koin.androidx.compose.navigation.koinNavViewModel
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.PageNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.Graphs
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.SettingsScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.add_child.ChildInfoViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.child_schedule.ChildScheduleViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.image_crop.ImageCropViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.add_child.ChildInfoScreenInSettings
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.add_child.ChildScheduleScreenInSettings
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.edit.EditProfileScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.edit.EditProfileViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.image_crop.ProfileImageCropScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.main_profile.ProfileSettingsScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.main_profile.ProfileSettingsViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.verify_email.VerifyNewEmailScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.verify_email.VerifyNewEmailViewModel

fun NavGraphBuilder.settingsNavGraph() {
    navigation(
        route = Graphs.HostNested.Settings.route,
        startDestination = SettingsScreenRoutes.Settings.route
    ) {
        composable(SettingsScreenRoutes.Settings.route) {
            val viewModel: ProfileSettingsViewModel = koinNavViewModel()
            ProfileSettingsScreen(
                screenState = viewModel.viewState.collectAsStateWithLifecycle(),
                uiState = viewModel.uiState,
                handleEvent = { viewModel.handleEvent(it) }
            )
        }
        composable(SettingsScreenRoutes.EditProfile.route) {
            val viewModel: EditProfileViewModel = koinNavViewModel()
            EditProfileScreen(
                screenState = viewModel.viewState.collectAsStateWithLifecycle(),
                uiState = viewModel.uiState,
                handleEvent = { viewModel.handleEvent(it) }
            )
        }
        composable(SettingsScreenRoutes.EditProfilePhoto.route) {
            val navigator = koinInject<PageNavigator>()
            val imageCropViewModel: ImageCropViewModel = koinNavViewModel {
                parametersOf(navigator)
            }
            ProfileImageCropScreen(
                screenState = imageCropViewModel.viewState.collectAsStateWithLifecycle(),
                handleEvent = { imageCropViewModel.saveCroppedImage(it) }
            )
        }
        composable(SettingsScreenRoutes.VerifyNewEmail.route) {
            val viewModel: VerifyNewEmailViewModel = koinNavViewModel()
            VerifyNewEmailScreen(
                screenState = viewModel.viewState.collectAsStateWithLifecycle(),
                handleEvent = { viewModel.handleEvent(it) }
            )
        }
        composable(SettingsScreenRoutes.ChildInfo.route) {
            val navigator: PageNavigator = koinInject()
            val childInfoViewModel: ChildInfoViewModel = koinNavViewModel {
                parametersOf(
                    { navigator.goToRoute(SettingsScreenRoutes.ChildSchedule) },
                    { navigator.goToPrevious() }
                )
            }
            ChildInfoScreenInSettings(
                screenState = childInfoViewModel.viewState.collectAsStateWithLifecycle(),
                uiState = childInfoViewModel.uiState,
                handleEvent = { childInfoViewModel.handleChildInfoEvent(it) }
            )
        }
        composable(SettingsScreenRoutes.ChildSchedule.route) {
            val navigator: PageNavigator = koinInject()
            val childScheduleViewModel: ChildScheduleViewModel = koinNavViewModel {
                parametersOf(
                    { navigator.goToRoute(SettingsScreenRoutes.EditProfile) },
                    { navigator.goToPrevious() }
                )
            }
            ChildScheduleScreenInSettings(
                screenState = childScheduleViewModel.viewState.collectAsStateWithLifecycle(),
                uiState = childScheduleViewModel.uiState,
                handleEvent = { childScheduleViewModel.handleScheduleEvent(it) }
            )
        }
    }
}