package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.graphs.main_host

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.navigator.PageNavigator
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.Graphs
import tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.SettingsScreenRoutes
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.composables.functions.asStateWithLifecycle
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.add_child.ChildInfoViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.child_schedule.ChildScheduleViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.common.image_crop.ImageCropViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.add_child.ChildInfoScreenInSettings
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.add_child.ChildScheduleScreenInSettings
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.change_credentials.EditCredentialsScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.change_credentials.EditCredentialsViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.edit.EditProfileScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.edit.EditProfileViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.image_crop.ProfileImageCropScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.main_profile.ProfileSettingsScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.main_profile.ProfileSettingsViewModel
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.verify_email.VerifyNewEmailScreen
import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.settings.verify_email.VerifyNewEmailViewModel

fun NavGraphBuilder.settingsNavGraph() {
    navigation<Graphs.HostNested.Settings>(
        startDestination = SettingsScreenRoutes.Settings
    ) {
        composable<SettingsScreenRoutes.Settings> {
            val viewModel: ProfileSettingsViewModel = koinViewModel()
            ProfileSettingsScreen(
                screenState = viewModel.viewState.asStateWithLifecycle(),
                uiState = viewModel.uiState.asStateWithLifecycle(),
                handleEvent = viewModel::handleEvent
            )
        }
        composable<SettingsScreenRoutes.EditProfile> {
            val viewModel: EditProfileViewModel = koinViewModel()
            EditProfileScreen(
                screenState = viewModel.viewState.asStateWithLifecycle(),
                uiState = viewModel.uiState.asStateWithLifecycle(),
                handleEvent = viewModel::handleEvent
            )
        }
        composable<SettingsScreenRoutes.EditProfilePhoto> {
            val navigator = koinInject<PageNavigator>()
            val imageCropViewModel: ImageCropViewModel = koinViewModel {
                parametersOf(navigator)
            }
            ProfileImageCropScreen(
                screenState = imageCropViewModel.viewState.asStateWithLifecycle(),
                handleEvent = { imageCropViewModel.handleEvent(it) }
            )
        }
        composable<SettingsScreenRoutes.VerifyNewEmail> {
            val viewModel: VerifyNewEmailViewModel = koinViewModel()
            VerifyNewEmailScreen(
                screenState = viewModel.viewState.asStateWithLifecycle(),
                uiState = viewModel.uiState.asStateWithLifecycle(),
                handleEvent = viewModel::handleEvent
            )
        }
        composable<SettingsScreenRoutes.ChildInfo> {
            val navigator: PageNavigator = koinInject()
            val childInfoViewModel: ChildInfoViewModel = koinViewModel {
                parametersOf(
                    { navigator.navigate(SettingsScreenRoutes.ChildSchedule) },
                    { navigator.goToPrevious() }
                )
            }
            ChildInfoScreenInSettings(
                screenState = childInfoViewModel.viewState.asStateWithLifecycle(),
                uiState = childInfoViewModel.uiState.asStateWithLifecycle(),
                handleEvent = { childInfoViewModel.handleChildInfoEvent(it) }
            )
        }
        composable<SettingsScreenRoutes.ChildSchedule> {
            val navigator: PageNavigator = koinInject()
            val childScheduleViewModel: ChildScheduleViewModel = koinViewModel {
                parametersOf(
                    { navigator.navigate(SettingsScreenRoutes.EditProfile) },
                    { navigator.goToPrevious() }
                )
            }
            ChildScheduleScreenInSettings(
                screenState = childScheduleViewModel.viewState.asStateWithLifecycle(),
                uiState = childScheduleViewModel.uiState.asStateWithLifecycle(),
                handleEvent = { childScheduleViewModel.handleScheduleEvent(it) }
            )
        }
        composable<SettingsScreenRoutes.EditCredentials> {
            val viewModel: EditCredentialsViewModel = koinViewModel()
            EditCredentialsScreen(
                screenState = viewModel.viewState.asStateWithLifecycle(),
                uiState = viewModel.uiState.asStateWithLifecycle(),
                handleEvent = viewModel::handleEvent
            )
        }
    }
}