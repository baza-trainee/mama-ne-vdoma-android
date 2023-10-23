package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes

import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.MAIN_PAGE

sealed class MainScreenRoutes(override val route: String, override val title: String): CommonHostRoute(route, MAIN_PAGE, title) {
    data object Main: MainScreenRoutes("main_screen", title = "Головна")
    data object Notifications: MainScreenRoutes("notifications_screen", title = "Сповіщення")
}