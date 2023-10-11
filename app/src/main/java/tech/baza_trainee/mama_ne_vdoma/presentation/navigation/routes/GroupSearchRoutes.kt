package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes

sealed class GroupSearchRoutes(val route: String): CommonRoute(route) {
    object ChooseChild: GroupSearchRoutes("choose_child_screen")
    object ChooseArea: GroupSearchRoutes("choose_area_screen")
    object GroupsFound: GroupSearchRoutes("groups_found_screen")
    object GroupRequestSearch: GroupSearchRoutes("group_request_screen")
}
