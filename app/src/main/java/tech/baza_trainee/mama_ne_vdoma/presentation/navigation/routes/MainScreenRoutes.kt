package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes

import tech.baza_trainee.mama_ne_vdoma.R
import tech.baza_trainee.mama_ne_vdoma.presentation.utils.MAIN_PAGE

sealed class MainScreenRoutes(override val route: String, override val title: Int): CommonHostRoute(route, MAIN_PAGE, title) {
    data object Main: MainScreenRoutes("main_screen", title = R.string.title_main_page)
    data object Notifications: MainScreenRoutes("notifications_screen", title = R.string.title_notifications)
}