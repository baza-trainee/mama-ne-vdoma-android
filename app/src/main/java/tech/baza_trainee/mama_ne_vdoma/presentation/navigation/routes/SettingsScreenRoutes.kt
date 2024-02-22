package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes

import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.SETTINGS_PAGE

sealed class SettingsScreenRoutes(override val route: String, override val title: Int): CommonHostRoute(route, SETTINGS_PAGE, title) {
    data object Settings: SettingsScreenRoutes("settings_screen", R.string.title_user_profile)
    data object EditProfile: SettingsScreenRoutes("edit_profile_screen", R.string.title_edit_user_profile)
    data object EditProfilePhoto: SettingsScreenRoutes("edit_profile_photo_screen", R.string.title_edit_user_profile)
    data object VerifyNewEmail: SettingsScreenRoutes("verify_new_email_screen", R.string.title_verify_user_data)
    data object ChildInfo: SettingsScreenRoutes("add_child_screen", R.string.title_add_child_data)
    data object ChildSchedule: SettingsScreenRoutes("add_child_schedule_screen", R.string.title_add_child_schedule)
    data object EditCredentials: SettingsScreenRoutes("edit_credentials_screen", R.string.title_change_credentials)
    data object ChangePasswordConfirm: SettingsScreenRoutes("change_password_confirm_screen", R.string.title_new_password_confirm)
}