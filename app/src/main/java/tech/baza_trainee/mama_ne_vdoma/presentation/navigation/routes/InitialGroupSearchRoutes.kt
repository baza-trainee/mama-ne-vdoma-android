package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes

sealed class InitialGroupSearchRoutes(val route: String): CommonRoute(route) {
    data object ChooseChild: InitialGroupSearchRoutes("choose_child_screen")
    data object SetArea: InitialGroupSearchRoutes("set_area_screen")
    data object GroupsFound: InitialGroupSearchRoutes("groups_found_screen")
}
