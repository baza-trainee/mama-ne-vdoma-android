package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes

import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.SETTINGS_PAGE

sealed class SettingsScreenRoutes(override val route: String, override val title: String): CommonHostRoute(route, SETTINGS_PAGE, "Мій акаунт") {
    data object Settings: SettingsScreenRoutes("settings_screen", "Мій акаунт")
    data object EditProfile: SettingsScreenRoutes("edit_profile_screen", "Редагування акаунту")
    data object EditProfilePhoto: SettingsScreenRoutes("edit_profile_photo_screen", "Редагування акаунту")
    data object VerifyNewEmail: SettingsScreenRoutes("verify_new_email_screen", "Перевірити емейл")
}