package tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes

sealed class Graphs(val route: String): CommonRoute(route) {
    object Start : Graphs("start_graph")
    object CreateUser : Graphs("create_user_graph")
    object Login : Graphs("login_graph")
    object UserProfile : Graphs("user_profile_graph")
    object GroupSearch : Graphs("group_search_graph")

    sealed class Host {
        object Main : Graphs("main_graph")
        object Groups : Graphs("groups_graph")
        object Search : Graphs("search_graph")
        object Settings : Graphs("settings_graph")
    }
}
