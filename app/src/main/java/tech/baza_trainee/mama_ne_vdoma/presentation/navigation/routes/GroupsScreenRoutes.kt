package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes

sealed class GroupsScreenRoutes(val route: String): CommonRoute(route) {
    object Groups: GroupsScreenRoutes("groups_screen")
}