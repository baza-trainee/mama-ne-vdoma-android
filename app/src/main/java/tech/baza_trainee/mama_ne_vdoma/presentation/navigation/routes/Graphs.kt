package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes

sealed class Graphs(val route: String): CommonRoute(route) {
    data object Start : Graphs("start_graph")
    data object CreateUser : Graphs("create_user_graph")
    data object Login : Graphs("login_graph")
    data object UserProfile : Graphs("user_profile_graph")
    data object FirstGroupSearch : Graphs("first_group_search_graph")

    sealed class Host {
        data object Main : Graphs("main_graph")
        data object Groups : Graphs("groups_graph")
        data object Search : Graphs("search_graph")
        data object Settings : Graphs("settings_graph")
    }
}
