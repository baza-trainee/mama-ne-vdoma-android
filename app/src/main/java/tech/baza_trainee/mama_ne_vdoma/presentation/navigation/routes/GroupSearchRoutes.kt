package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes

sealed class GroupSearchRoutes(val route: String): CommonRoute(route) {
    data object ChooseChild: GroupSearchRoutes("choose_child_screen")
    data object SetArea: GroupSearchRoutes("set_area_screen")
    data object GroupsFound: GroupSearchRoutes("groups_found_screen")
    data object GroupRequestSearch: GroupSearchRoutes("group_request_screen")
}
