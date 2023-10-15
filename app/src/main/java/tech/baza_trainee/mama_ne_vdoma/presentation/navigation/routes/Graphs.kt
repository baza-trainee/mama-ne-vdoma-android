package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes

sealed class Graphs(val route: String): CommonRoute(route) {
    data object Start : Graphs("start_graph")
    data object CreateUser : Graphs("create_user_graph")
    data object Login : Graphs("login_graph")
    data object UserProfile : Graphs("user_profile_graph")
    data object FirstGroupSearch : Graphs("first_group_search_graph")
    data object Host : Graphs("host_graph")

    sealed class HostNested {
        data object Main : Graphs("host_main_graph")
        data object Groups : Graphs("host_groups_graph")
        data object Search : Graphs("host_search_graph")
        data object Settings : Graphs("host_settings_graph")
    }
}
