package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes

import tech.baza_trainee.mama_ne_vdoma.presentation.utils.GROUPS_PAGE

sealed class GroupsScreenRoutes(override val route: String, override val title: String): CommonHostRoute(route, GROUPS_PAGE, "Групи") {
    data object Groups: GroupsScreenRoutes(route = "groups_screen", "Групи")
    data object UpdateGroup: GroupsScreenRoutes(route = "update_group_screen", "Редагування групи")
    data object UpdateGroupAvatar: GroupsScreenRoutes(route = "update_group_avatar_screen", "Редагування зображення групи")
}