package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes

import tech.baza_trainee.mama_ne_vdoma.presentation.ui.screens.main.common.GROUPS_PAGE

sealed class GroupsScreenRoutes(override val route: String): CommonHostRoute(route, GROUPS_PAGE, "Групи") {
    data object Groups: GroupsScreenRoutes(route = "groups_screen")
}