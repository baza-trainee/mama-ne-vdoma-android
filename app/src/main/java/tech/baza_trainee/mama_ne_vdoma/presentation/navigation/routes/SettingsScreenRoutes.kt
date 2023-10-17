package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes

sealed class SettingsScreenRoutes(val route: String): CommonRoute(route) {
    data object Settings: SettingsScreenRoutes("settings_screen")
    data object EditProfile: SettingsScreenRoutes("edit_profile_screen")
}