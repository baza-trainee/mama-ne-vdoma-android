package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes

sealed class SettingsScreenRoutes(val route: String): CommonRoute(route) {
    object Settings: SettingsScreenRoutes("settings_screen")
}